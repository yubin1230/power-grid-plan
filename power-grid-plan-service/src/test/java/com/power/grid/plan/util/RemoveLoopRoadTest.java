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
        String s = "10970659-21787608-988264-1010923-1022944-1046152-13487093-1004792-1020254-23612405-25236633-1046306-1042637-1030048-1030054-26161046-1011081-1042801-10970962-1042800-1045797-1042803-10970961-1042802-10970959-988299-26161043-1042799-1004782-988292-988291-1016705-1004781-1036150-988286-89387503-89387477-1026760-1016699-1023686-30434677-1016700-47267850-1014596-1042795-12200862-58494310-991325-953259-953262-1023689-59621043-1023690-1013755-1013753-1013754-25876998-38428772-1020249-1020259-1020251-1020258-56922778-56922777-56922756-56922755-56922774-25236637-12200867-1030045-1039455-1010568-38458072-1039343-953219-953216-956384-1030134-1036166-1036173-1030141-47682762-47682765-1033682-38457786-38457775-38457790-38457793-38457784-38457785-38457781-38457780-58192620-1033374-47717935-47717951-47717950-47717938-47717937-47717936-1011146-1011150-1036569-1036570-1011157-47717932-47717930-38384120-609030609-61704545-1026825-26861798-23612612-1008107-23612336-23612340-984888-984850-984870-984891-62569804-984901-984903-25877139-12369552-997421-1046479-1019721-1019722-23205909-23205910-1046488-997430-81524027-58494316-1036305-81524028-81524029-81524030-58694521-1017000-1011175-1011174-26090321-1033142-89387457-1036322-949784-11265977";
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

    @Test
    public void arrivePath(){
        Map<Long, RoadHandleBo> roadHandleBoMap = getRoadHandleBoMap();
        List<Long> initPath = new ArrayList<>();
        List<List<Long>> pathList = new ArrayList<>();
        RemoveLoopRoad.arrivePath(roadHandleBoMap, initPath, pathList, 1L, 8L, 10);
        System.out.println("***"+pathList);
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