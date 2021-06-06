package com.power.grid.plan.service.coordinate;

import com.power.grid.plan.dto.bo.NodeBo;

/**
 * 两点之间距离
 * @author yubin
 * @date 2021/1/10 23:11
 */
public class CoordinateDistance {

    public static double GetDistance(NodeBo start, NodeBo end) {
        double Lat1r = ConvertDegreeToRadians(start.getLatitude());
        double Lat2r = ConvertDegreeToRadians(end.getLatitude());
        double Long1r = ConvertDegreeToRadians(start.getLongitude());
        double Long2r = ConvertDegreeToRadians(end.getLongitude());

        double R = 6378100; // Earth's radius (km)
        double d = Math.acos(Math.sin(Lat1r) *
                Math.sin(Lat2r) + Math.cos(Lat1r) *
                Math.cos(Lat2r) *
                Math.cos(Long2r - Long1r)) * R;
        return d;
    }

    public static double GetDistance(double Lon1, double Lat1, double Lon2, double Lat2) {
        double Lat1r = ConvertDegreeToRadians(Lat1);
        double Lat2r = ConvertDegreeToRadians(Lat2);
        double Long1r = ConvertDegreeToRadians(Lon1);
        double Long2r = ConvertDegreeToRadians(Lon2);
        double R = 6378100; // Earth's radius (km)
        double d = Math.acos(Math.sin(Lat1r) *
                Math.sin(Lat2r) + Math.cos(Lat1r) *
                Math.cos(Lat2r) *
                Math.cos(Long2r - Long1r)) * R;
        return d;
    }

    private static double ConvertDegreeToRadians(double degrees) {
        return (Math.PI / 180) * degrees;
    }

    public static void main(String[] args) {

        double lng1 = 114.029055;
        double lat1 = 22.530542;
        double lng2 = 114.099164;
        double lat2 = 22.570018;
        NodeBo start = new NodeBo(lng1, lat1);
        NodeBo end = new NodeBo(lng2, lat2);
        System.out.println(GetDistance(start, end));
        System.out.println(GetDistance(lng1, lat1, lng2, lat2));

    }
}
