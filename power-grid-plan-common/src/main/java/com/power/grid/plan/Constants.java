package com.power.grid.plan;

/**
 * 配置实体类
 * @author yubin
 * @date 2021/1/17 21:22
 */
public class Constants {

    /**
     * 蚂蚁数量
     */
    public static final int ANT_NUM = 100;

    /**
     * 循环次数
     */
    public static final int LOOP = 200;


    /**
     * 以直线距离1/2为半径，向外延伸距离，单位KM
     */
    public static final double RADIUS_ADD = 0.5;

    /**
     * 信息素浓度的影响因子
     */
    public static final int ALPHA = 1;

    /**
     * 挥发因子
     */
    public static final double RHO = 0.9;//信息素挥发因子

    /**
     * 每次成功路线，信息素释放单位
     */
    public static final double PHEROMONE_RELEASING_UNIT = 0.5;

    /**
     * 设置为5
     */
    public static final int BETA = 5;
    /**
     * 轮盘赌因子，越大循环次数越高，准确率高
     */
    public static final int ROULETTE_FACTOR = 15;
}
