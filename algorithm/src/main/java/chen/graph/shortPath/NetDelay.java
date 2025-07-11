package chen.graph.shortPath;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.*;

/**
 * 网络延迟时间
 * Dijkstra算法
 */
public class NetDelay {

    /**
     *
     * @param times [i,j,k] 表示从i到j的时间为k ， 有向
     * @param n     节点数量
     * @param k     起始节点
     * @return
     */
    public int networkDelayTime(int[][] times, int n, int k) {
        List<int[]>[] list = new List[n+1];
        for (int[] time : times) {
            int from = time[0];
            if (list[from] == null) {
                list[from] = new ArrayList<>();
            }
            list[from].add(new int[]{time[1], time[2]});
        }

        PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparing(e -> e[1]));
        int dist[] = new int[n + 1];
        int inf = Integer.MAX_VALUE / 2;
        Arrays.fill(dist, inf);

        dist[0] = -1;
        dist[k] = 0;
        heap.add(new int[]{k, 0});

        while (!heap.isEmpty()) {
            int[] poll = heap.poll();
            int to = poll[0];
            int dis = poll[1];

            if (dist[to] < dis) {
                continue;
            }

            dist[to] = Math.min(dist[to], dis);
            if (list[to] != null) {
                for (int[] l : list[to]) {
                    int y = l[0];
                    int d = l[1] + dist[to];
                    if (d < dist[y]) {
                        heap.add(new int[]{y, d});
                    }
                }
                list[to] = null;
            }
        }

        int res = Arrays.stream(dist).max().orElse(inf);
        return res == inf ? -1 : res;

    }

    public static void main(String[] args) throws JsonProcessingException {
        int[][] array = JSONUtil.parse("[[2,4,10],[5,2,38],[3,4,33],[4,2,76],[3,2,64],[1,5,54],[1,4,98],[2,3,61],[2,1,0],[3,5,77],[5,1,34],[3,1,79],[5,3,2],[1,2,59],[4,3,46],[5,4,44],[2,5,89],[4,5,21],[1,3,86],[4,1,95]]").toBean(int[][].class);

        int i = new NetDelay().networkDelayTime(array, 5, 1);
        System.out.println(i);
    }

}
