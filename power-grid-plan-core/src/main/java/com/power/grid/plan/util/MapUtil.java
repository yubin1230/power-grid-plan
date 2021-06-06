package com.power.grid.plan.util;

import com.power.grid.plan.dto.bo.NodeBo;
import com.power.grid.plan.dto.bo.PointBo;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/**
 * 地图工具类
 * @author yubin
 * @date 2021/5/30 12:15
 */
public class MapUtil {

    public static boolean isContain(Coordinate[] coordinates, double longitude, double latitude) {

        if (coordinates.length < 3) {
            return false;
        }
        GeometryFactory factory = new GeometryFactory();//几何图形工厂

        LinearRing shell = factory.createLinearRing(coordinates);//创建多边形线
        Polygon polygon;
        polygon = factory.createPolygon(shell, null);//创建一个多边形

        return polygon.contains(factory.createPoint(new Coordinate(longitude, latitude)));
    }

    /**
     * 判断坐标是否有效
     * @param longitude
     * @param latitude
     * @return
     */
    public static boolean isValid(Double longitude, Double latitude) {
        if (longitude == null || latitude == null) {
            return false;
        }
        if (longitude > 73.55 && longitude < 135.08333333333333333 && latitude > 3.85 && latitude < 53.55) {
            return true;
        }
        return false;
    }

    /**
     * 获取两点距离
     * @param start 开始
     * @param end   结束
     * @return double
     */
    public static double getDistance(NodeBo start, NodeBo end) {
        double Lat1r = convertDegreeToRadians(start.getLatitude());
        double Lat2r = convertDegreeToRadians(end.getLatitude());
        double Long1r = convertDegreeToRadians(start.getLongitude());
        double Long2r = convertDegreeToRadians(end.getLongitude());

        double R = 6378100; // Earth's radius (km)
        double d = Math.acos(Math.sin(Lat1r) *
                Math.sin(Lat2r) + Math.cos(Lat1r) *
                Math.cos(Lat2r) *
                Math.cos(Long2r - Long1r)) * R;
        return d;
    }

    private static double convertDegreeToRadians(double degrees) {
        return (Math.PI / 180) * degrees;
    }

    public static PointBo pointToLine(NodeBo start, NodeBo end, NodeBo nodeBo) {

        double x1 = start.getLongitude();
        double y1 = start.getLatitude();
        double x2 = end.getLongitude();
        double y2 = end.getLatitude();
        double x0 = nodeBo.getLongitude();
        double y0 = nodeBo.getLatitude();

        double a, b, c;

        a = lineSpace(x1, y1, x2, y2);// 线段的长度

        b = lineSpace(x1, y1, x0, y0);// (x1,y1)到点的距离

        c = lineSpace(x2, y2, x0, y0);// (x2,y2)到点的距离

        if (c + b == a) {//点在线段上

            return new PointBo(x0, y0, 0D);

        }

        if (c * c >= a * a + b * b) { //组成直角三角形或钝角三角形，(x1,y1)为直角或钝角

            return new PointBo(x1, y1, b);

        }

        if (b * b >= a * a + c * c) {//组成直角三角形或钝角三角形，(x2,y2)为直角或钝角

            return new PointBo(x2, y2, c);

        }
        //组成锐角三角形，则求三角形的高
        double p = (a + b + c) / 2;// 半周长

        double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积

        double space = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）

        double A = y2 - y1;     //y2-y1
        double B = x1 - x2;     //x1-x2;
        double C = x2 * y1 - x1 * y2;     //x2*y1-x1*y2
        double x = (B * B * x0 - A * B * y0 - A * C) / (A * A + B * B);
        double y = (-A * B * x0 + A * A * y0 - B * C) / (A * A + B * B);

        return new PointBo(x, y, space);

    }


    // 计算两点之间的距离，平面计算方式
    private static double lineSpace(double x1, double y1, double x2, double y2) {

        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2)

                * (y1 - y2));
    }
}
