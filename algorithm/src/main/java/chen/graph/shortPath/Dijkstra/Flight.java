package chen.graph.shortPath.Dijkstra;

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
 *
 * 输入:
 * n = 3, edges = [[0,1,100],[1,2,100],[0,2,500]], src = 0, dst = 2, k = 1
 * 输出: 200
 *
 *
 * ############ 除了Dijk外，还可以使用动态规划和Bellman-Ford算法
 * ## Dijkstra算法
 * 1. 使用visit记录已访问过的节点的k值（即路径数量），允许重复访问，但k值必须小于之前的k值
 *
 * ## 动态规划求解
 * 1. f[k][t] = min(f[k-1][t], f[k-1][t-{last}] + price) ## k表示中转次数（路径数量），t表示节点 f表示费用
 */
public class Flight {

    /**
     * dp解法
     */
    public int findCheapestPriceDp(int n, int[][] flights, int src, int dst, int k) {
        int dp[][] = new int[k + 2][n];
        int inf = Integer.MAX_VALUE / 2;
        for (int i = 0; i < k + 2; i++) {
            Arrays.fill(dp[i], inf);
            dp[i][src] = 0;
        }


        for (int t = 1; t <= k + 1; t++) {
            for (int[] flight : flights) {
                int j = flight[0], i = flight[1], cost = flight[2];
                dp[t][i] = Math.min(dp[t][i], dp[t - 1][j] + cost);
            }
        }

        return dp[k + 1][dst] == inf ? -1 : dp[k + 1][dst];
    }


    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {

        List<int[]>[] list = new List[n];
        for (int i = 0; i < n; i++) {
            list[i] = new ArrayList<>();
        }
        for (int[] flight : flights) {
            list[flight[0]].add(new int[]{flight[1], flight[2]});
        }

        // price , pathNum , to
        int[] visit = new int[n];
        Arrays.fill(visit, Integer.MAX_VALUE);
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);

        pq.add(new int[]{0, -1, src});
        while (!pq.isEmpty()) {
            int[] poll = pq.poll();
            int price = poll[0];
            int pathNum = poll[1];
            int t = poll[2];

            // 允许重新走同一个节点，前提是路径数比之前小
            if (pathNum > visit[t]) {
                continue;
            }
            if (t == dst) {
                return price;
            }
            visit[t] = pathNum;

            for (int[] next : list[t]) {
                int nextT = next[0];
                int nextPrice = next[1];

                if(pathNum + 1 > k)
                    continue;

                pq.add(new int[]{price + nextPrice, pathNum + 1, nextT});
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int[][] array = JSONUtil.parse("[[0,1,2],[1,2,1],[2,0,10]]").toBean(int[][].class);

        int i = new Flight().findCheapestPriceDp(3, array, 1, 2,1);
        System.out.println(i);
    }

}
