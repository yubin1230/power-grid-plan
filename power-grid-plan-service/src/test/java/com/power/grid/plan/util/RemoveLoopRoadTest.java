package com.power.grid.plan.util;

import com.google.common.collect.Lists;
import com.power.grid.plan.dto.bo.HandleBo;
import com.power.grid.plan.dto.bo.RoadBo;
import com.power.grid.plan.dto.bo.RoadHandleBo;
import com.power.grid.plan.service.CalculateService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RemoveLoopRoadTest {

    @Resource
    private BaseDataInit baseDataInit;

    @Resource
    private CalculateService calculateService;
    @Test
    public void removeLoopRoad() {
        String s = "10970659-21787608-988264-1010923-1022944-1046152-13487093-1004792-1020254-23612405-25236633-1046306-25236632-26161050-63084159-991308-63084158-1030056-988314-988155-988157-988300-13487094-12200859-1035992-1036779-988151-25877125-1036041-991318-991320-988160-988153-59620997-59620999-13487095-988163-1030118-1036042-25877031-13487129-1032655-1039335-988167-988170-991327-1039338-1032653-1032652-1030065-1039345-23612624-1042656-1030157-1016739-991402-991404-23612616-1004467-1004468-1033200-82540262-1033204-1004465-1004466-23612617-991435-47360079-991433-1016741-991426-1016742-61250151-1042668-23612599-991438-23612598-991412-991416-991447-54265715-1042665-47215525-1020422-47215520-47215522-1042815-1042816-1036250-38384146-1046342-38384148-47267883-1046340-1046343-1046345-949771-1036707-949768-89387459-89387458-89387457-1036322-949784-11265977";
        LinkedList<String> sRoad = Lists.newLinkedList(Arrays.asList(s.split("-")));
        LinkedList<Long> road=new LinkedList<>();
        sRoad.forEach(s1->{
            road.add(Long.parseLong(s1));
        });
        List<RoadBo> roadBoList = baseDataInit.getRoadBoList();
        Map<Long, RoadHandleBo> roadHandleBoMap = calculateService.initProbability(roadBoList);
        HandleBo bo=new HandleBo();
        bo.setHandlePath(road);
        HandleBo bestBo = RemoveLoopRoad.removeLoopRoad(roadHandleBoMap, bo);
        System.out.println(bestBo);
    }

    @Test
    public void removeLoopRoad1() {
        Map<Long, RoadHandleBo> roadHandleBoMap = getRoadHandleBoMap();
        LinkedList<Long> path = new LinkedList<>(Arrays.asList(1L, 2L, 5L, 6L, 7L, 3L, 4L, 8L));
        HandleBo handleBo = new HandleBo();
        handleBo.setHandlePath(path);
        handleBo.setSumPrice(8.0);
        HandleBo bestBo = RemoveLoopRoad.removeLoopRoad(roadHandleBoMap, handleBo);
        System.out.println(bestBo);
    }

    private  Map<Long, RoadHandleBo> getRoadHandleBoMap() {
        Map<Long, RoadHandleBo> roadHandleBoMap = new HashMap<>();
        RoadHandleBo bo1 = new RoadHandleBo();
        bo1.setNodeId(1L);
        Map<Long, Double> sumPrice1 = new HashMap<>();
        sumPrice1.put(2L, 1.0);
        bo1.setSumPrice(sumPrice1);

        RoadHandleBo bo2 = new RoadHandleBo();
        bo2.setNodeId(2L);
        Map<Long, Double> sumPrice2 = new HashMap<>();
        sumPrice2.put(3L, 1.0);
        sumPrice2.put(5L, 1.0);
        sumPrice2.put(8L, 10.0);
        bo2.setSumPrice(sumPrice2);

        RoadHandleBo bo3 = new RoadHandleBo();
        bo3.setNodeId(3L);
        Map<Long, Double> sumPrice3 = new HashMap<>();
        sumPrice3.put(4L, 1.0);
        sumPrice3.put(8L, 1.0);
        bo3.setSumPrice(sumPrice3);

        RoadHandleBo bo4 = new RoadHandleBo();
        bo4.setNodeId(4L);
        Map<Long, Double> sumPrice4 = new HashMap<>();
        sumPrice4.put(8L, 1.0);
        bo4.setSumPrice(sumPrice4);

        RoadHandleBo bo5 = new RoadHandleBo();
        bo5.setNodeId(5L);
        Map<Long, Double> sumPrice5 = new HashMap<>();
        sumPrice5.put(6L, 1.0);
        bo5.setSumPrice(sumPrice5);

        RoadHandleBo bo6 = new RoadHandleBo();
        bo6.setNodeId(6L);
        Map<Long, Double> sumPrice6 = new HashMap<>();
        sumPrice6.put(7L, 1.0);
        bo6.setSumPrice(sumPrice6);

        RoadHandleBo bo7 = new RoadHandleBo();
        bo7.setNodeId(7L);
        Map<Long, Double> sumPrice7 = new HashMap<>();
        sumPrice7.put(3L, 1.0);
        bo7.setSumPrice(sumPrice7);

        RoadHandleBo bo8 = new RoadHandleBo();
        bo8.setNodeId(8L);
        Map<Long, Double> sumPrice8 = new HashMap<>();
        bo8.setSumPrice(sumPrice8);

        roadHandleBoMap.put(1L, bo1);
        roadHandleBoMap.put(2L, bo2);
        roadHandleBoMap.put(3L, bo3);
        roadHandleBoMap.put(4L, bo4);
        roadHandleBoMap.put(5L, bo5);
        roadHandleBoMap.put(6L, bo6);
        roadHandleBoMap.put(7L, bo7);
        roadHandleBoMap.put(8L, bo8);
        return roadHandleBoMap;
    }
}