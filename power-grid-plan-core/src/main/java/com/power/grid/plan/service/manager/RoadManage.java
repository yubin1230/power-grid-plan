package com.power.grid.plan.service.manager;

import com.power.grid.plan.LogUtil;
import com.power.grid.plan.dto.bo.*;
import com.power.grid.plan.service.NodeService;
import com.power.grid.plan.service.RoadService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 路段管理类
 * @author yubin
 * @date 2021/6/26 23:46
 */
@Component
public class RoadManage {

    private static final Logger LOG = LogManager.getLogger(RoadManage.class);


    @Resource
    private RoadService roadService;

    @Resource
    private NodeService nodeService;

    public CalculateContextBo context(CabinetBo cabinetBo, FillRequirementBo fillRequirementBo, String gridNo) {

        NodeBo cabinet = new NodeBo(cabinetBo.getLongitude(), cabinetBo.getLatitude());
        NodeBo fillRequirement = new NodeBo(fillRequirementBo.getLongitude(), fillRequirementBo.getLatitude());

        //路段信息
        List<RoadBo> roadBoList = roadService.select(gridNo);
        LOG.info("{}:路段信息数量：{}", LogUtil.getRequestNo(), roadBoList.size());
        //节点信息
        List<NodeBo> nodeBoList = nodeService.select(gridNo);
        LOG.info("{}:节点信息数量：{}", LogUtil.getRequestNo(), roadBoList.size());

        LOG.info("位置信息计算初始化完成");
        //过滤无效路段
        roadBoList = filterInvalidNoGoRoad(roadBoList, nodeBoList);

        Map<String, NodeBo> nodeBoMap = nodeBoList.stream().collect(Collectors.toMap(nodeBo -> String.valueOf(nodeBo.getId()), node -> node, (key1, key2) -> key1));

        Map<String, RoadBo> allMap=new HashMap<>();

        Map<String, RoadBo> startRoadBoMap = roadBoList.stream().collect(Collectors.toMap(item -> item.getStartNodeId() + "_" + item.getEndNodeId(), item -> item));
        Map<String, RoadBo> endRoadBoMap = roadBoList.stream().collect(Collectors.toMap(item -> item.getEndNodeId() + "_" + item.getStartNodeId(), item -> item));
        allMap.putAll(startRoadBoMap);
        allMap.putAll(endRoadBoMap);
        return CalculateContextBo.builder().caseNo(createSchemeNo()).cabinetBo(cabinetBo).fillRequirementBo(fillRequirementBo).gridNo(gridNo).start(cabinet).end(fillRequirement).roadBoMap(allMap).roadBoList(roadBoList).nodeBoMap(nodeBoMap).build();
    }


    private List<RoadBo> filterInvalidNoGoRoad(List<RoadBo> roadBoList, List<NodeBo> nodeBoList) {
        Set<Long> nodeBoSet = nodeBoList.stream().map(NodeBo::getId).collect(Collectors.toSet());
        return roadBoList.stream().filter(roadBo -> nodeBoSet.contains(roadBo.getStartNodeId()) && nodeBoSet.contains(roadBo.getEndNodeId())).collect(Collectors.toList());
    }

    /**
     * 生成方案编码
     */
    private String createSchemeNo() {
        String strDateFormat1 = "yyyyMMddHHmmss";
        SimpleDateFormat sdf1 = new SimpleDateFormat(strDateFormat1);
        return String.format("%s%s%s", "C", sdf1.format(new Date()), String.valueOf((int) ((Math.random() * 9 + 1) * 100)));
    }
}
