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

        double vertexPointX = current.getLongitude();
        double vertexPointY = current.getLatitude();
        double point0X = next.getLongitude();
        double point0Y = next.getLatitude();
        double point1Y = end.getLongitude();
        double point1X = end.getLatitude();

        //向量的点乘
        double vector = (point0X - vertexPointX) * (point1X - vertexPointX) + (point0Y - vertexPointY) * (point1Y - vertexPointY);

        //向量的模乘
        double sqrt = Math.sqrt(
                (Math.abs((point0X - vertexPointX) * (point0X - vertexPointX)) + Math.abs((point0Y - vertexPointY) * (point0Y - vertexPointY)))
                        * (Math.abs((point1X - vertexPointX) * (point1X - vertexPointX)) + Math.abs((point1Y - vertexPointY) * (point1Y - vertexPointY)))
        );

        //反余弦计算弧度
        double radian = Math.acos(vector / sqrt);

        //弧度转角度制
        return (int) (180 * radian / Math.PI);
    }

    /**
     * function ball2xyz(lat, lng, r = 6400) {
     * return {
     * x: r * Math.cos(lat) * Math.cos(lng),
     * y: r * Math.cos(lat) * Math.sin(lng),
     * z: r * Math.sin(lat)
     * };
     * }
     * // https://blog.csdn.net/reborn_lee/article/details/82497577
     * // 将地理经纬度转换成笛卡尔坐标系
     * function geo2xyz({ lat, lng }) {
     * let thera = (Math.PI * lat) / 180;
     * let fie = (Math.PI * lng) / 180;
     * return ball2xyz(thera, fie);
     * }
     * <p>
     * // 计算3个地理坐标点之间的夹角
     * function angleOflocation(l1, l2, l3) {
     * let p1 = geo2xyz(l1);
     * let p2 = geo2xyz(l2);
     * let p3 = geo2xyz(l3);
     * <p>
     * let { x: x1, y: y1, z: z1 } = p1;
     * let { x: x2, y: y2, z: z2 } = p2;
     * let { x: x3, y: y3, z: z3 } = p3;
     * <p>
     * // 计算向量 P2P1 和 P2P3 的夹角 https://www.zybang.com/question/3379a30c0dd3041b3ef966803f0bf758.html
     * let _P1P2 = Math.sqrt((x2 - x1) ** 2 + (y2 - y1) ** 2 + (z2 - z1) ** 2);
     * let _P2P3 = Math.sqrt((x3 - x2) ** 2 + (y3 - y2) ** 2 + (z3 - z2) ** 2);
     * <p>
     * let P = (x1 - x2) * (x3 - x2) + (y1 - y2) * (y3 - y2) + (z1 - z2) * (z3 - z2); //P2P1*P2P3
     * <p>
     * return (Math.acos(P / (_P1P2 * _P2P3)) / Math.PI) * 180;
     * }
     */

    public static void main(String[] args) {
        NodeBo current = new NodeBo(121.473658, 31.230378);
        NodeBo next = new NodeBo(114.514793, 38.042225);
        NodeBo end = new NodeBo(116.322056, 39.89491);
        System.out.println(angle(current, next, end));

    }

}
