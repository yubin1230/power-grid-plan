package com.power.grid.plan.service.impl;

import com.google.common.collect.Lists;
import com.power.grid.plan.JsonUtil;
import com.power.grid.plan.LogUtil;
import com.power.grid.plan.dto.bo.*;
import com.power.grid.plan.dto.enums.AreaType;
import com.power.grid.plan.dto.enums.PowerType;
import com.power.grid.plan.mapper.CabinetMapper;
import com.power.grid.plan.pojo.CabinetPo;
import com.power.grid.plan.service.CabinetService;
import com.power.grid.plan.util.LinePointUtil;
import com.power.grid.plan.util.MapUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

    @Override
    public List<CabinetBo> queryCabinet(List<LineBo> lineBoList, ContextBo contextBo) {


        List<String> loneNoList = lineBoList.stream().map(LineBo::getLineNo).collect(Collectors.toList());
        LOG.info("requestNo：{} 查询线路下所有环网柜：{}", LogUtil.getRequestNo(), JsonUtil.tryToString(loneNoList));
        //查询所有环网柜
        List<CabinetBo> cabinetBoList = queryCabinetListByLineNoList(loneNoList);
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
            return cabinetLoadCapacityUserFilter(cabinetBo, contextBo);
        }).collect(Collectors.toList());
        LOG.info("requestNo：{} 环网柜过滤成功：{}", LogUtil.getRequestNo(), JsonUtil.tryToString(filterCabinetBoList));

        if (CollectionUtils.isEmpty(filterCabinetBoList)) {
            return Lists.newArrayList();
        }

        //对查询环网柜进行排序
        List<CabinetBo> orderCabinetList = orderCabinet(filterCabinetBoList, contextBo);

        //并且判断是否满足最短距离
        return cabinetDistanceFilter(lineBoList, orderCabinetList, contextBo);
    }

    @Override
    public List<CabinetBo> queryCabinetListByGridNo(String gridNo) {
        List<CabinetPo> cabinetPoList = cabinetMapper.selectListByCondition(CabinetPo.builder().gridNo(gridNo).build());
        return cabinetPoList.stream().map(c -> {
            CabinetBo bo = CabinetBo.builder().build();
            BeanUtils.copyProperties(c, bo);
            return bo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CabinetBo> queryCabinetListByLineNo(String lineNo) {
        List<CabinetPo> cabinetPoList = cabinetMapper.selectListByCondition(CabinetPo.builder().lineNo(lineNo).build());
        return cabinetPoList.stream().map(c -> {
            CabinetBo bo = CabinetBo.builder().build();
            BeanUtils.copyProperties(c, bo);
            return bo;
        }).collect(Collectors.toList());
    }

    private List<CabinetBo> queryCabinetListByLineNoList(List<String> lineNoList) {
        List<CabinetPo> cabinetPoList = cabinetMapper.selectListByLineNoList(lineNoList);
        return cabinetPoList.stream().map(c -> {
            CabinetBo bo = CabinetBo.builder().build();
            BeanUtils.copyProperties(c, bo);
            return bo;
        }).collect(Collectors.toList());
    }


    private boolean cabinetLoadCapacityUserFilter(CabinetBo cabinetBo, ContextBo contextBo) {
        FillRequirementBo fillRequirementBo = contextBo.getFillRequirementBo();
        RequirementParamBo requirementParamBo = contextBo.getRequirementParamBo();
        double load = 0;
        double loadRatio = 0;
        int capacity = 0;
        double capacityRatio = 0;
        double users = 0;
        AreaType areaType = AreaType.getAreaTypeMap(fillRequirementBo.getArea());
        PowerType powerType = PowerType.getPowerTypeMap(fillRequirementBo.getType());

        switch (areaType) {
            case A1:
                switch (powerType) {
                    case INDUSTRY:
                        load = requirementParamBo.getA1IndustryLoad();
                        loadRatio = requirementParamBo.getA1IndustryLoadRatio();
                        capacity = requirementParamBo.getA1IndustryCapacity();
                        capacityRatio = requirementParamBo.getA1IndustryCapacityRatio();
                        users = requirementParamBo.getA1IndustryUsers();
                        break;
                    case BUSINESS:
                        load = requirementParamBo.getA1BusinessLoad();
                        loadRatio = requirementParamBo.getA1BusinessLoadRatio();
                        capacity = requirementParamBo.getA1BusinessCapacity();
                        capacityRatio = requirementParamBo.getA1BusinessCapacityRatio();
                        users = requirementParamBo.getA1BusinessUsers();
                        break;
                    case RESIDENT:
                        load = requirementParamBo.getA1ResidentLoad();
                        loadRatio = requirementParamBo.getA1ResidentLoadRatio();
                        capacity = requirementParamBo.getA1ResidentCapacity();
                        capacityRatio = requirementParamBo.getA1ResidentCapacityRatio();
                        users = requirementParamBo.getA1ResidentUsers();
                        break;
                }
            case A2:
            case B:
                switch (powerType) {
                    case INDUSTRY:
                        load = requirementParamBo.getA2bIndustryLoad();
                        loadRatio = requirementParamBo.getA2bIndustryLoadRatio();
                        capacity = requirementParamBo.getA2bIndustryCapacity();
                        capacityRatio = requirementParamBo.getA2bIndustryCapacityRatio();
                        users = requirementParamBo.getA2bIndustryUsers();
                        break;
                    case BUSINESS:
                        load = requirementParamBo.getA2bBusinessLoad();
                        loadRatio = requirementParamBo.getA2bBusinessLoadRatio();
                        capacity = requirementParamBo.getA2bBusinessCapacity();
                        capacityRatio = requirementParamBo.getA2bBusinessCapacityRatio();
                        users = requirementParamBo.getA2bBusinessUsers();
                        break;
                    case RESIDENT:
                        load = requirementParamBo.getA2bResidentLoad();
                        loadRatio = requirementParamBo.getA2bResidentLoadRatio();
                        capacity = requirementParamBo.getA2bResidentCapacity();
                        capacityRatio = requirementParamBo.getA2bResidentCapacityRatio();
                        users = requirementParamBo.getA2bResidentUsers();
                        break;
                }
        }
        //环网柜负荷≤负荷*调整系数，环网柜容量≤容量 *调整系数，用户≤用户数
        return cabinetBo.getClValue() <= load * loadRatio && cabinetBo.getCapacity() <= capacity * capacityRatio && 0 < users;
    }

    private List<CabinetBo> orderCabinet(List<CabinetBo> cabinetBoList, ContextBo contextBo) {
        FillRequirementBo fillRequirementBo = contextBo.getFillRequirementBo();
        return cabinetBoList.stream().sorted((o1, o2) -> {
            NodeBo start1 = new NodeBo(o1.getLongitude(), o1.getLatitude());
            NodeBo start2 = new NodeBo(o2.getLongitude(), o2.getLatitude());
            NodeBo end = new NodeBo(fillRequirementBo.getLongitude(), fillRequirementBo.getLatitude());
            if (MapUtil.getDistance(start1, end) - MapUtil.getDistance(start2, end) > 0) return 1;
            else return -1;
        }).collect(Collectors.toList());
    }

    private List<CabinetBo> cabinetDistanceFilter(List<LineBo> lineBoList, List<CabinetBo> cabinetBoList, ContextBo contextBo) {
        RequirementParamBo requirementParamBo = contextBo.getRequirementParamBo();
        FillRequirementBo fillRequirementBo = contextBo.getFillRequirementBo();
        CabinetBo minCabinetBo = cabinetBoList.get(0);
        if (MapUtil.getDistance(new NodeBo(minCabinetBo.getLongitude(), minCabinetBo.getLatitude()), new NodeBo(fillRequirementBo.getLongitude(), fillRequirementBo.getLatitude())) > requirementParamBo.getLineThreshold()) {
            PointBo pointBo = LinePointUtil.getPedalSimplexAndDistance(lineBoList, new NodeBo(fillRequirementBo.getLongitude(), fillRequirementBo.getLatitude())).get(0);
            CabinetBo cabinetBo = CabinetBo.builder().longitude(pointBo.getLongitude()).latitude(pointBo.getLatitude()).build();
            cabinetBoList.add(0, cabinetBo);
        }
        return cabinetBoList.subList(0, CABINET_NUM);
    }

}
