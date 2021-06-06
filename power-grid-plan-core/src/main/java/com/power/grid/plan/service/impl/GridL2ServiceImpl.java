package com.power.grid.plan.service.impl;

import com.power.grid.plan.dto.bo.CabinetBo;
import com.power.grid.plan.dto.bo.FillRequirementBo;
import com.power.grid.plan.dto.bo.GridL2Bo;
import com.power.grid.plan.dto.bo.LineBo;
import com.power.grid.plan.mapper.CabinetMapper;
import com.power.grid.plan.mapper.GridL2Mapper;
import com.power.grid.plan.mapper.LineMapper;
import com.power.grid.plan.pojo.CabinetPo;
import com.power.grid.plan.pojo.GridL2Po;
import com.power.grid.plan.pojo.LinePo;
import com.power.grid.plan.service.GridL2Service;
import com.power.grid.plan.util.MapUtil;
import com.vividsolutions.jts.geom.Coordinate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GIS展示实现类
 * @author yuhin
 * @date 2021/5/30 12:11
 */
@Service
public class GridL2ServiceImpl implements GridL2Service {

    @Resource
    private GridL2Mapper gridL2Mapper;

    @Override
    public GridL2Bo queryGridL2(FillRequirementBo bo) {

        List<GridL2Po> gridL2PoList = gridL2Mapper.selectList();

        for (GridL2Po g : gridL2PoList) {
            String[] points = g.getGridEdge().split(";");
            Coordinate[] coordinates = new Coordinate[points.length];
            for (int i = 0; i < points.length; i++) {
                String[] xys = points[i].split(",");
                coordinates[i] = new Coordinate(Double.parseDouble(xys[0]), Double.parseDouble(xys[1]));
            }
            if (MapUtil.isContain(coordinates, bo.getLongitude(), bo.getLatitude())) {
                GridL2Bo gridL2Bo = new GridL2Bo();
                BeanUtils.copyProperties(g, gridL2Bo);
                return gridL2Bo;
            }
        }
        return null;
    }


}
