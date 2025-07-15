package chen.graph.shortPath.Dijkstra;

import cn.hutool.json.JSONUtil;

import java.util.*;

/**
 * 网络延迟时间
 * Dijkstra算法
 */
public class NetDelay {

    /**
     * @param times [i,j,k] 表示从i到j的时间为k ， 有向
     * @param n     节点数量
     * @param k     起始节点
     * @return
     */
    public int networkDelayTime(int[][] times, int n, int k) {
        List<int[]>[] list = new List[n + 1];
        for (int i = 0; i < n + 1; i++) {
            list[i] = new ArrayList<>();
        }
        for (int[] time : times) {
            list[time[0]].add(new int[]{time[1], time[2]});
        }

        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        int dist[] = new int[n + 1];
        int inf = Integer.MAX_VALUE / 2;
        Arrays.fill(dist, inf);

        boolean[] visit = new boolean[n + 1];

        dist[k] = 0;
        heap.add(new int[]{k, 0});

        while (!heap.isEmpty()) {
            int[] poll = heap.poll();
            int to = poll[0];

            if (visit[to]) {
                continue;
            }

            visit[to] = true;
            for (int[] l : list[to]) {
                int y = l[0];
                int d;
                if (!visit[y] && (d = l[1] + dist[to]) < dist[y]) {
                    dist[y] = d;
                    heap.add(new int[]{y, d});
                }
            }
        }

        int ans = -1;
        for (int i = 1; i <= n; i++) {
            if (dist[i] == inf) {
                return -1;
            }
            ans = Math.max(ans, dist[i]);
        }

        return ans;
    }

    public static void main(String[] args) {
        int[][] array = JSONUtil.parse("[[2,1,1],[2,3,1],[3,4,1]]").toBean(int[][].class);

        int i = new NetDelay().networkDelayTime(array, 4, 2);
        System.out.println(i);
    }

}
