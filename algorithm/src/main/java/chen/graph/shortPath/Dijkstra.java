package chen.graph.shortPath;

/**
 * Dijkstra：求某段到原点的最短路径
 * 适用于无向图和有向图
 *
 * 设 白点 为 已确定最短路径的点
 * 蓝点 为 未确定最短路径的点 ， 但已通过相邻白点计算出值
 *
 * 1.设原点 dis(0) = 0，设为白点 ；其他点 dis(i) = 正无穷，为蓝点
 * 2. 先计算上一个白点相连的点，更新dis(i)值
 * 3. 遍历已知蓝点，取dis(i)最小的点，作为新的白点（这里可以用优先队列快速获取），重复2，3
 * 4. 无蓝点则结束
 *
 * O(nmlog(nm)) n为点数，m为边数
 */
public class Dijkstra {

    public static void main(String[] args) {

    }
}
