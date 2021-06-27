package com.power.grid.plan.service.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.power.grid.plan.Constants;
import com.power.grid.plan.ResponseCodeEnum;
import com.power.grid.plan.dto.bo.*;
import com.power.grid.plan.dto.enums.CabinetCategoryType;
import com.power.grid.plan.exception.BizException;
import com.power.grid.plan.service.CabinetService;
import com.power.grid.plan.service.PlanSchemeService;
import com.power.grid.plan.service.astar.AStar;
import com.power.grid.plan.service.astar.InitRoadMap;
import com.power.grid.plan.service.coordinate.CoordinateCenter;
import com.power.grid.plan.service.coordinate.CoordinateDistance;
import com.power.grid.plan.service.coordinate.LuceneSpatial;
import com.power.grid.plan.util.BaseDataInit;
import com.power.grid.plan.util.MapUtil;
import com.power.grid.plan.util.RoadPointUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * A星计算管理类
 * @author yubin
 * @date 2021/1/10 13:27
 */
@Component
public class CalculateManage {


    private static final Logger LOG = LogManager.getLogger(GridPlanManage.class);

    @Resource
    private InitRoadMap initRoadMap;

    @Resource
    private PlanSchemeService planSchemeService;

    @Resource
    private RoadManage roadManage;

    @Resource
    private CabinetService cabinetService;


    private Cache<String, List<HandleBo>> cache = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.HOURS).build();


    public List<HandleBo> calculate(List<CabinetBo> cabinetBoList, FillRequirementBo fillRequirementBo) {

        List<HandleBo> handleBoList = Lists.newArrayList();
        for (CabinetBo cabinetBo : cabinetBoList) {
            NodeBo cabinet = new NodeBo(cabinetBo.getLongitude(), cabinetBo.getLatitude());
            NodeBo fillRequirement = new NodeBo(fillRequirementBo.getLongitude(), fillRequirementBo.getLatitude());
            CalculateContextBo contextBo = roadManage.context(cabinetBo, fillRequirementBo, cabinetBo.getGridNo());
            //查询环网柜最近路段
            cabinet = RoadPointUtil.getInsinuationRoad(contextBo, cabinet);
            //查询用户包装点最近路段
            fillRequirement = RoadPointUtil.getInsinuationRoad(contextBo, fillRequirement);

            //使用路段节点进行搜索
            contextBo.setStart(cabinet);
            contextBo.setEnd(fillRequirement);

            handleBoList.addAll(calculate(contextBo));
        }

        handleBoList = handleBoList.stream().sorted(Comparator.comparing(HandleBo::getSumPrice)).distinct().collect(Collectors.toList());

        if (handleBoList.size() > 3) {
            handleBoList = handleBoList.subList(0, 3);
        }

        cache.put(fillRequirementBo.getNeedNo(), handleBoList);

        return handleBoList;
    }

    @Transactional
    public void saveScheme(String needNo) {

        List<HandleBo> handleBoList = cache.getIfPresent(needNo);

        if (CollectionUtils.isEmpty(handleBoList)) {
            throw new BizException(ResponseCodeEnum.DEFAULT_ERROR, "规划方案已失效，请重新计算");
        }


        //保存规划环网柜
        savePlanCabinetBo(handleBoList);

        //保存规划方案
        savePlanScheme(handleBoList);

        cache.put(needNo, Lists.newArrayList());
    }

    private void savePlanCabinetBo(List<HandleBo> handleBoList) {

        for (HandleBo handleBo : handleBoList) {
            CalculateContextBo contextBo = handleBo.getContext();
            CabinetBo cabinetBo = contextBo.getCabinetBo();
            if (!cabinetBo.getCabinetCategory().equals(CabinetCategoryType.EXIST_LINE_EXIST_CATEGORY)) {
                PlanCabinetBo planCabinetBo = PlanCabinetBo.builder().caseNo(contextBo.getCaseNo()).cabinetNo(createNo()).gridNo(contextBo.getGridNo()).lineNo(cabinetBo.getLineNo()).stationName(cabinetBo.getStationName()).latitude(cabinetBo.getLatitude()).longitude(cabinetBo.getLongitude()).build();
                cabinetBo.setCabinetNo(planCabinetBo.getCabinetNo());
                cabinetService.savePlanCabinet(planCabinetBo);
            }
        }
    }

    private List<HandleBo> calculate(CalculateContextBo contextBo) {

        NodeBo start = contextBo.getStart();
        NodeBo end = contextBo.getEnd();

        List<RoadBo> roadBoList = contextBo.getRoadBoList();

        Map<String, NodeBo> nodeBoMap = contextBo.getNodeBoMap();
        //初始化距离、成本
        Map<String, AStarRoadHandleBo> roadHandleBoMap = initRoadMap.initRoadMap(roadBoList);

        double[] factors = Constants.FACTORS;
        Set<HandleBo> handleBoSet = Sets.newHashSet();
        for (double factor : factors) {
            AStarMapInfo mapInfo = new AStarMapInfo();
            mapInfo.setRoadHandleBoMap(roadHandleBoMap);
            mapInfo.setNodeBoMap(nodeBoMap);
            mapInfo.setHFactor(factor);
            mapInfo.setStart(new AStarNodeBo(start));
            mapInfo.setEnd(new AStarNodeBo(end));
            AStar aStar = new AStar();
            HandleBo handleBo = aStar.start(mapInfo);
            handleBo.setContext(contextBo);
            handleBo.setSumPrice(getSumPrice(roadHandleBoMap, handleBo.getHandlePath()));
            //计算总路径
            handleBo.setSumDistance(getSumDistance(handleBo));
            LOG.info("查找到路径：" + handleBo.getHandlePath());
            String s = "";
            for (int i = 0; i < handleBo.getHandlePath().size(); i++) {
                s += nodeBoMap.get(String.valueOf(handleBo.getHandlePath().get(i))).getLongitude() + "," + nodeBoMap.get(String.valueOf(handleBo.getHandlePath().get(i))).getLatitude() + ";";
            }
            System.out.println(s);
            LOG.info("总成本：" + handleBo.getSumPrice());
            handleBoSet.add(handleBo);
            if (handleBoSet.size() >= 3) {
                break;
            }
        }

        return Lists.newArrayList(handleBoSet);
    }

    private Double getSumPrice(Map<String, AStarRoadHandleBo> roadHandleBoMap, List<Long> pathList) {
        double price = 0.0;
        for (int i = 0; i < pathList.size() - 1; i++) {
            AStarRoadHandleBo roadHandleBo = roadHandleBoMap.get(String.valueOf(pathList.get(i)));
            price += roadHandleBo.getSumPrice().get(pathList.get(i + 1));
        }
        return price;
    }


    private void savePlanScheme(List<HandleBo> handleBoList) {
        handleBoList.forEach(handleBo -> {
            List<Long> handlePathList = handleBo.getHandlePath();
            Collections.reverse(handlePathList);
            CalculateContextBo contextBo = handleBo.getContext();
            PlanSchemeBo bo = PlanSchemeBo.builder().caseNo(contextBo.getCaseNo()).needNo(contextBo.getFillRequirementBo().getNeedNo()).cabinetNo(contextBo.getCabinetBo().getCabinetNo()).caseCost(handleBo.getSumPrice()).isChoose(2).build();
            Map<String, NodeBo> nodeBoMap = contextBo.getNodeBoMap();
            List<PlanSchemeDetailBo> planSchemeDetailBoList = Lists.newArrayList();
            double sumDistance = 0D;
            for (int i = 0; i < handlePathList.size() - 1; i++) {
                RoadBo roadBo = contextBo.getRoadBoMap().get(handlePathList.get(i) + "_" + handlePathList.get(i + 1));
                double distance = MapUtil.getDistance(nodeBoMap.get(String.valueOf(roadBo.getStartNodeId())), nodeBoMap.get(String.valueOf(roadBo.getEndNodeId())));
                double price = roadBo.getPrice() * distance;
                //总距离
                sumDistance += distance;
                PlanSchemeDetailBo detailBo = PlanSchemeDetailBo.builder().roadNo(contextBo.getFillRequirementBo().getNeedNo()).
                        caseNo(bo.getCaseNo()).roadIndex(i + 1).roadNo(roadBo.getRoadNo()).cableType(roadBo.getRoadType()).
                        pathLength(distance).cost(price).
                        layWay(roadBo.getRodeLay()).casePoints(getCasePoints(nodeBoMap, roadBo)).build();
                planSchemeDetailBoList.add(detailBo);
            }
            bo.setCaseLength(sumDistance);
            bo.setPlanSchemeDetailBoList(planSchemeDetailBoList);
            CabinetBo cabinetBo = contextBo.getCabinetBo();
            //环网柜类型
            if (cabinetBo.getCabinetCategory().equals(CabinetCategoryType.EXIST_LINE_EXIST_CATEGORY)) {
                bo.setCabinetType(CabinetCategoryType.EXIST_LINE_EXIST_CATEGORY.getCode());
            } else {
                bo.setCabinetType(1);
            }
            planSchemeService.insert(bo);
        });
    }

    private String getCasePoints(Map<String, NodeBo> nodeBoMap, RoadBo roadBo) {
        NodeBo start = nodeBoMap.get(String.valueOf(roadBo.getStartNodeId()));
        NodeBo end = nodeBoMap.get(String.valueOf(roadBo.getEndNodeId()));
        return start.getLongitude() + "," + start.getLatitude() + ";" + end.getLongitude() + "," + end.getLatitude();
    }

    private double getSumDistance(HandleBo handleBo) {
        CalculateContextBo contextBo = handleBo.getContext();
        Map<String, NodeBo> nodeBoMap = contextBo.getNodeBoMap();
        List<Long> handlePathList = handleBo.getHandlePath();
        double sumDistance = 0D;
        for (int i = 0; i < handlePathList.size() - 1; i++) {
            RoadBo roadBo = contextBo.getRoadBoMap().get(handlePathList.get(i) + "_" + handlePathList.get(i + 1));

            double distance = MapUtil.getDistance(nodeBoMap.get(String.valueOf(roadBo.getStartNodeId())), nodeBoMap.get(String.valueOf(roadBo.getEndNodeId())));
            //总距离
            sumDistance += distance;
        }
        return sumDistance;
    }

    /**
     * 生成规划环网柜编码
     */
    private String createNo() {
        String strDateFormat1 = "yyyyMMddHHmmss";
        SimpleDateFormat sdf1 = new SimpleDateFormat(strDateFormat1);
        return String.format("%s%s%s", "C", sdf1.format(new Date()), String.valueOf((int) ((Math.random() * 9 + 1) * 100)));
    }

}
