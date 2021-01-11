package com.power.grid.plan.service.coordinate;

import com.power.grid.plan.dto.bo.NodeBo;

import java.util.List;

/**
 * 坐标中心
 * @author yubin
 * @date 2021/1/10 22:56
 */
public class CoordinateCenter {

    public static NodeBo GetCenterPoint(List<NodeBo> nodeBoList) {
        int total = nodeBoList.size();
        double X = 0, Y = 0, Z = 0;
        for (NodeBo node : nodeBoList) {
            double lat, lon, x, y, z;
            lat = node.getLatitude() * Math.PI / 180;
            lon = node.getLongitude() * Math.PI / 180;
            x = Math.cos(lat) * Math.cos(lon);
            y = Math.cos(lat) * Math.sin(lon);
            z = Math.sin(lat);
            X += x;
            Y += y;
            Z += z;
        }
        X = X / total;
        Y = Y / total;
        Z = Z / total;
        double Lon = Math.atan2(Y, X);
        double Hyp = Math.sqrt(X * X + Y * Y);
        double Lat = Math.atan2(Z, Hyp);
        return new NodeBo(Lon * 180 / Math.PI, Lat * 180 / Math.PI);
    }


}
