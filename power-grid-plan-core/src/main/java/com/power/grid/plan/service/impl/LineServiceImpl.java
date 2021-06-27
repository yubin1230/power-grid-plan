package com.power.grid.plan.service.impl;

import com.power.grid.plan.dto.bo.*;
import com.power.grid.plan.dto.enums.CabinetCategoryType;
import com.power.grid.plan.dto.enums.LineType;
import com.power.grid.plan.mapper.LineMapper;
import com.power.grid.plan.pojo.LinePo;
import com.power.grid.plan.service.CabinetService;
import com.power.grid.plan.service.LineService;
import com.power.grid.plan.util.LinePointUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 线路实现类
 * @author yubin
 * @date 2021/6/6 12:17
 */
@Service
public class LineServiceImpl implements LineService {

    /**
     * 线路存在过载
     */
    private static final int OVERLOAD = 1;

    /**
     * 三遥
     */
    private static final int THREE_REMOTE = 1;

    /**
     * 环网柜数量
     */
    private static final int CABINET_NUM = 4;


    @Resource
    private LineMapper lineMapper;

    @Resource
    private CabinetService cabinetService;

    @Override
    public List<LineBo> queryLine(CabinetContextBo cabinetContextBo) {

        List<LineBo> queryLineList = queryLineList(cabinetContextBo.getGridL2Bo().getGridNo());

        return filterRule(queryLineList, cabinetContextBo);
    }

    @Override
    public List<LineBo> queryLineList(String gridNo) {
        List<LinePo> linePoList = lineMapper.selectListByCondition(LinePo.builder().gridNo(gridNo).build());
        return linePoList.stream().map(l -> {
            LineBo bo = LineBo.builder().build();
            BeanUtils.copyProperties(l, bo);
            return bo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CabinetBo> newCabinetExistLine(CabinetContextBo cabinetContextBo) {

        List<LineBo> queryLineList = queryLineList(cabinetContextBo.getGridL2Bo().getGridNo());

        //过滤线路规则
        List<LineBo> filterList = queryLineList.stream().filter(lineBo -> lineBo.getLineType().equals(LineType.ALREADY_EXISTING.getCode())).collect(Collectors.toList());
        filterList = filterCabinetExistLine(filterList, cabinetContextBo);

        return getNewCabinetList(filterList, cabinetContextBo, CabinetCategoryType.EXIST_LINE_PLAN_CATEGORY);
    }

    @Override
    public List<CabinetBo> newCabinetPlanLine(CabinetContextBo cabinetContextBo) {

        List<LineBo> queryLineList = queryLineList(cabinetContextBo.getGridL2Bo().getGridNo());

        List<LineBo> filterList = queryLineList.stream().filter(lineBo -> lineBo.getLineType().equals(LineType.PLAN.getCode())).collect(Collectors.toList());

        return getNewCabinetList(filterList, cabinetContextBo, CabinetCategoryType.PLAN_LINE_PLAN_CATEGORY);
    }


    private List<LineBo> filterRule(List<LineBo> queryLineList, CabinetContextBo cabinetContextBo) {
        return queryLineList.stream().filter(lineBo -> {
            //线路已有
            if (!lineBo.getLineType().equals(LineType.ALREADY_EXISTING.getCode())) {
                return false;
            }
            FillRequirementBo fillRequirementBo = cabinetContextBo.getFillRequirementBo();
            ParamBo paramBo = cabinetContextBo.getParamBo();
            //线路容量+负荷需求≤线路最大总容量*调整系数
            //线路容量=已使用容量+规划用量
            int useCapacity = lineBo.getLinePowerUsed() + lineBo.getPlanLinePowerUsed() + fillRequirementBo.getNeedCapacity() * paramBo.getConversionRatio() / 100;
            if (useCapacity > paramBo.getMaximumCapacity() * paramBo.getLineThresholdRatio()) {
                return false;
            }

            //线路自动化要求
            double requestRatio = paramBo.getAutomationRatio().doubleValue() / 100;
            if (queryAutomationRatio(lineBo.getLineNo()) < requestRatio) {
                return false;
            }

            //线路所属变电站高峰负荷期间不存在重过载
            if (lineBo.getOverload().equals(OVERLOAD)) {
                return false;
            }

            return true;
        }).collect(Collectors.toList());
    }


    private double queryAutomationRatio(String lineNo) {
        List<CabinetBo> cabinetBoList = cabinetService.queryCabinetListByLineNo(lineNo);
        if (CollectionUtils.isEmpty(cabinetBoList)) {
            return 0;
        }
        List<CabinetBo> threeRemote = cabinetBoList.stream().filter(cabinetBo -> cabinetBo.getAutomation().equals(THREE_REMOTE)).collect(Collectors.toList());
        return threeRemote.size() / cabinetBoList.size();
    }


    private List<LineBo> filterCabinetExistLine(List<LineBo> queryLineList, CabinetContextBo cabinetContextBo) {
        ParamBo paramBo = cabinetContextBo.getParamBo();
        return queryLineList.stream().filter(lineBo -> {
            List<CabinetBo> cabinetBoList = cabinetService.queryCabinetListByLineNo(lineBo.getLineNo());
            return cabinetBoList.size() < paramBo.getNodeNum();
        }).collect(Collectors.toList());
    }

    private List<CabinetBo> getNewCabinetList(List<LineBo> queryLineList, CabinetContextBo cabinetContextBo, CabinetCategoryType cabinetCategoryType) {
        FillRequirementBo fillRequirementBo = cabinetContextBo.getFillRequirementBo();
        List<PointBo> pointBoList = LinePointUtil.getPedalSimplexAndDistance(queryLineList, new NodeBo(fillRequirementBo.getLongitude(), fillRequirementBo.getLatitude()));
        List<CabinetBo> cabinetBoList = pointBoList.stream().map(pointBo -> CabinetBo.builder().gridNo(pointBo.getLineBo().getGridNo()).lineNo(pointBo.getLineBo().getLineNo()).stationName(pointBo.getLineBo().getStationName()).longitude(pointBo.getLongitude()).latitude(pointBo.getLatitude()).cabinetCategory(cabinetCategoryType).build()).collect(Collectors.toList());

        if (pointBoList.size() > CABINET_NUM) return cabinetBoList.subList(0, CABINET_NUM);

        return cabinetBoList;
    }


}
