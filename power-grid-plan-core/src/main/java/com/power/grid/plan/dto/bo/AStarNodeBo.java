package com.power.grid.plan.dto.bo;

import lombok.Data;

/**
 * ClassName: Node
 * @author kesar
 * @Description: 路径结点
 */
@Data
public class AStarNodeBo implements Comparable<AStarNodeBo> {

    private NodeBo nodeBo; // 坐标
    private AStarNodeBo parent; // 父结点
    private Double G; // G：是个准确的值，是起点到当前结点的代价
    private Double H; // H：是个估值，当前结点到目的结点的估计代价

    public AStarNodeBo(NodeBo nodeBo) {
        this.nodeBo = nodeBo;
        this.G=0.0;
        this.H=0.0;
    }

    public AStarNodeBo(NodeBo nodeBo, AStarNodeBo parent, Double g, Double h) {
        this.nodeBo = nodeBo;
        this.parent = parent;
        G = g;
        H = h;
    }

    @Override
    public int compareTo(AStarNodeBo o) {
        if (o == null) return -1;
        if (G + H > o.G + o.H)
            return 1;
        else if (G + H < o.G + o.H) return -1;
        return 0;
    }
}
