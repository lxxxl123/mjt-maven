package chen.graph.shortPath;

import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * K 站中转内最便宜的航班 787
 * 有 n 个城市通过一些航班连接。给你一个数组 flights ，其中 flights[i] = [fromi, toi, pricei] ，表示该航班都从城市 fromi 开始，以价格 pricei 抵达 toi。
 *
 * 现在给定所有的城市和航班，以及出发城市 src 和目的地 dst，你的任务是找到出一条最多经过 k 站中转的路线，使得从 src 到 dst 的 价格最便宜 ，并返回该价格。 如果不存在这样的路线，则输出 -1。
 */
public class Flight {

    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {

        return -0;
    }

    public static void main(String[] args) {
        int[][] array = JSONUtil.parse("[[2,1,1],[2,3,1],[3,4,1]]").toBean(int[][].class);

        int i = new Flight().findCheapestPrice(10, array, 4, 2,5);
        System.out.println(i);
    }

}
