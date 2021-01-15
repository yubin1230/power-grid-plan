package com.power.grid.plan.service.algorithm;

import com.power.grid.plan.dto.bo.NodeBo;


/**
 * 角度
 * @author yubin
 * @date 2021/1/14 20:58
 */
public class Angle {

    /**
     * 计算角度
     */
    public static int angle(NodeBo current, NodeBo next, NodeBo end) {

        double currentX = current.getLongitude();
        double currentY = current.getLatitude();
        double nextX = next.getLongitude();
        double nextY = next.getLatitude();
        double endX = end.getLongitude();
        double endY = end.getLatitude();

        double x1=nextX-currentX;
        double x2=endX-currentX;
        double y1=nextY-currentY;
        double y2=endY-currentY;

        //弧度转角度制
        return getRotateAngle(x1,y1,x2,y2);
    }

    public  static  int  getRotateAngle(double x1, double y1, double x2, double y2)
    {
     double epsilon = 1.0e-6;
     double nyPI = Math.acos(-1.0);
        double dist, dot, angle;
        int degree;
        // normalize
        dist = Math.sqrt( x1 * x1 + y1 * y1 );
        x1 /= dist;
        y1 /= dist;
        dist = Math.sqrt( x2 * x2 + y2 * y2 );
        x2 /= dist;
        y2 /= dist;
// dot product
        dot = x1 * x2 + y1 * y2;
        if ( Math.abs(dot-1.0) <= epsilon )
            angle = 0.0;
        else if ( Math.abs(dot+1.0) <= epsilon )
            angle = nyPI;
        else {
            double cross;

            angle = Math.acos(dot);
            //cross product
            cross = x1 * y2 - x2 * y1;
            // vector p2 is clockwise from vector p1
            // with respect to the origin (0.0)
            if (cross < 0 ) {
                angle = 2 * nyPI - angle;
            }
        }
        degree = (int)(angle *  180.0 / nyPI);
        if(degree>180){
            degree=360-degree;
        }
        return degree;
    }

    public static void main(String[] args) {
        NodeBo current = new NodeBo(121.473658, 31.230378);
        NodeBo next = new NodeBo(114.514793, 38.042225);
        NodeBo end = new NodeBo(116.322056, 39.89491);
        System.out.println(angle(current, next, end));
//        System.out.println(getRotateAngle(114.514793-121.473658,38.042225-31.230378,116.322056-121.473658,39.89491-31.230378));
//        System.out.println(getRotateAngle(2,0,0,2));
//        System.out.println(getRotateAngle(2,0,-2,2));
//        System.out.println(getRotateAngle(2,0,2,-2));
//        System.out.println(getRotateAngle(2,0,-2,-2));

    }

}
