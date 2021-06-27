package com.power.grid.plan.service.impl;

import com.google.common.collect.Lists;
import com.power.grid.plan.JsonUtil;
import com.power.grid.plan.LogUtil;
import com.power.grid.plan.ResponseCodeEnum;
import com.power.grid.plan.dto.bo.*;
import com.power.grid.plan.dto.enums.CabinetCategoryType;
import com.power.grid.plan.exception.BizException;
import com.power.grid.plan.mapper.CabinetMapper;
import com.power.grid.plan.mapper.PlanCabinetMapper;
import com.power.grid.plan.pojo.CabinetPo;
import com.power.grid.plan.pojo.PlanCabinetPo;
import com.power.grid.plan.service.CabinetService;
import com.power.grid.plan.util.LinePointUtil;
import com.power.grid.plan.util.MapUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 环网柜实现类
 * @author yubin
 * @date 2021/6/6 12:15
 */
@Service
public class CabinetServiceImpl implements CabinetService {

    private static final Logger LOG = LogManager.getLogger(CabinetServiceImpl.class);

    private static final int CABINET_NUM = 5;


    @Resource
    private CabinetMapper cabinetMapper;

    @Resource
    private PlanCabinetMapper planCabinetMapper;

    @Override
    public List<CabinetBo> queryCabinet(List<LineBo> lineBoList, CabinetContextBo cabinetContextBo) {


        List<String> lineNoList = lineBoList.stream().map(LineBo::getLineNo).collect(Collectors.toList());
        LOG.info("requestNo：{} 查询线路下所有环网柜：{}", LogUtil.getRequestNo(), JsonUtil.tryToString(lineNoList));
        //查询所有环网柜
        List<CabinetBo> cabinetBoList = queryCabinetListByLineNoList(lineNoList);
        LOG.info("requestNo：{} 查询环网柜成功：{}", LogUtil.getRequestNo(), JsonUtil.tryToString(cabinetBoList));

        //根据条件过滤环网柜
        List<CabinetBo> filterCabinetBoList = cabinetBoList.stream().filter(cabinetBo -> {
            //是否有富裕备用角
            List<String> total = Arrays.asList(cabinetBo.getOutputNo().split(","));
            List<String> used = Arrays.asList(cabinetBo.getOutputUsedNo().split(","));
            if (used.containsAll(total)) {
                return false;
            }
            //负荷、容量、用户是否满足
            return cabinetLoadCapacityUserFilter(cabinetBo, cabinetContextBo);
        }).collect(Collectors.toList());
        LOG.info("requestNo：{} 环网柜过滤成功：{}", LogUtil.getRequestNo(), JsonUtil.tryToString(filterCabinetBoList));

        if (CollectionUtils.isEmpty(filterCabinetBoList)) {
            return Lists.newArrayList();
        }

        //对查询环网柜进行排序
        List<CabinetBo> orderCabinetList = orderCabinet(filterCabinetBoList, cabinetContextBo);

        //并且判断是否满足最短距离
        return cabinetDistanceFilter(lineBoList, orderCabinetList, cabinetContextBo);
    }

    @Override
    public List<CabinetBo> queryCabinetListByGridNo(String gridNo) {
        List<CabinetPo> cabinetPoList = cabinetMapper.selectListByCondition(CabinetPo.builder().gridNo(gridNo).build());
        return cabinetPoList.stream().map(c -> {
            CabinetBo bo = CabinetBo.builder().cabinetCategory(CabinetCategoryType.EXIST_LINE_EXIST_CATEGORY).build();
            BeanUtils.copyProperties(c, bo);
            return bo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CabinetBo> queryCabinetListByLineNo(String lineNo) {
        List<CabinetPo> cabinetPoList = cabinetMapper.selectListByCondition(CabinetPo.builder().lineNo(lineNo).build());
        return cabinetPoList.stream().map(c -> {
            CabinetBo bo = CabinetBo.builder().cabinetCategory(CabinetCategoryType.EXIST_LINE_EXIST_CATEGORY).build();
            BeanUtils.copyProperties(c, bo);
            return bo;
        }).collect(Collectors.toList());
    }

    @Override
    public int savePlanCabinet(PlanCabinetBo planCabinetBo) {
        PlanCabinetPo po = new PlanCabinetPo();
        BeanUtils.copyProperties(planCabinetBo, po);
        return planCabinetMapper.insert(po);
    }

    @Override
    public CabinetBo queryCabinetListByNo(String cabinetNo, CabinetCategoryType type) {

        //已有环网柜
        if (CabinetCategoryType.EXIST_LINE_EXIST_CATEGORY.equals(type)) {
            return queryExistCabinet(cabinetNo);
        }
        //规划环网柜
        return queryPlanCabinet(cabinetNo);
    }

    private CabinetBo queryPlanCabinet(String cabinetNo) {
        PlanCabinetPo planCabinetPo = planCabinetMapper.select(cabinetNo);

        return CabinetBo.builder().cabinetNo(planCabinetPo.getCabinetNo()).gridNo(planCabinetPo.getGridNo()).
                lineNo(planCabinetPo.getLineNo()).stationName(planCabinetPo.getStationName()).longitude(planCabinetPo.getLongitude()).
                latitude(planCabinetPo.getLatitude()).build();
    }


    private CabinetBo queryExistCabinet(String cabinetNo) {
        List<CabinetPo> cabinetPoList = cabinetMapper.selectListByCondition(CabinetPo.builder().cabinetNo(cabinetNo).build());
        List<CabinetBo> cabinetBoList = cabinetPoList.stream().map(c -> {
            CabinetBo bo = CabinetBo.builder().cabinetCategory(CabinetCategoryType.EXIST_LINE_EXIST_CATEGORY).build();
            BeanUtils.copyProperties(c, bo);
            return bo;
        }).collect(Collectors.toList());
        if (cabinetBoList.size() != 1) throw new BizException(ResponseCodeEnum.DEFAULT_ERROR, "查询环网柜异常");
        return cabinetBoList.get(0);
    }

    private List<CabinetBo> queryCabinetListByLineNoList(List<String> lineNoList) {
        List<CabinetPo> cabinetPoList = cabinetMapper.selectListByLineNoList(lineNoList);
        return cabinetPoList.stream().map(c -> {
            CabinetBo bo = CabinetBo.builder().build();
            BeanUtils.copyProperties(c, bo);
            bo.setCabinetCategory(CabinetCategoryType.EXIST_LINE_EXIST_CATEGORY);
            return bo;
        }).collect(Collectors.toList());
    }


    private boolean cabinetLoadCapacityUserFilter(CabinetBo cabinetBo, CabinetContextBo cabinetContextBo) {
        ParamBo paramBo = cabinetContextBo.getParamBo();
        double load = paramBo.getLoad();
        double loadRatio = paramBo.getLoadRatio();
        int capacity = paramBo.getCapacity();
        double capacityRatio = paramBo.getCapacityRatio();
        //环网柜负荷≤负荷*调整系数，环网柜容量≤容量 *调整系数，用户≤用户数
        return cabinetBo.getClValue() <= load * loadRatio && cabinetBo.getCapacity() <= capacity * capacityRatio;
    }

    private List<CabinetBo> orderCabinet(List<CabinetBo> cabinetBoList, CabinetContextBo cabinetContextBo) {
        FillRequirementBo fillRequirementBo = cabinetContextBo.getFillRequirementBo();
        return cabinetBoList.stream().sorted((o1, o2) -> {
            NodeBo start1 = new NodeBo(o1.getLongitude(), o1.getLatitude());
            NodeBo start2 = new NodeBo(o2.getLongitude(), o2.getLatitude());
            NodeBo end = new NodeBo(fillRequirementBo.getLongitude(), fillRequirementBo.getLatitude());
            if (MapUtil.getDistance(start1, end) - MapUtil.getDistance(start2, end) > 0) return 1;
            else return -1;
        }).collect(Collectors.toList());
    }

    private List<CabinetBo> cabinetDistanceFilter(List<LineBo> lineBoList, List<CabinetBo> cabinetBoList, CabinetContextBo cabinetContextBo) {
        ParamBo paramBo = cabinetContextBo.getParamBo();
        FillRequirementBo fillRequirementBo = cabinetContextBo.getFillRequirementBo();
        CabinetBo minCabinetBo = cabinetBoList.get(0);
        if (MapUtil.getDistance(new NodeBo(minCabinetBo.getLongitude(), minCabinetBo.getLatitude()), new NodeBo(fillRequirementBo.getLongitude(), fillRequirementBo.getLatitude())) > paramBo.getLineThreshold()) {
            PointBo pointBo = LinePointUtil.getPedalSimplexAndDistance(lineBoList, new NodeBo(fillRequirementBo.getLongitude(), fillRequirementBo.getLatitude())).get(0);
            CabinetBo cabinetBo = CabinetBo.builder().gridNo(pointBo.getLineBo().getGridNo()).lineNo(pointBo.getLineBo().getLineNo()).stationName(pointBo.getLineBo().getStationName()).longitude(pointBo.getLongitude()).latitude(pointBo.getLatitude()).cabinetCategory(CabinetCategoryType.EXIST_LINE_PEDAL_PLAN_CATEGORY).build();
            cabinetBoList.add(0, cabinetBo);
        }
        if (cabinetBoList.size() > CABINET_NUM) return cabinetBoList.subList(0, CABINET_NUM);
        return cabinetBoList;
    }

}
