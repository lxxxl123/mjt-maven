package chen.struct;

/**
 * @see <a href="https://oi-wiki.org/ds/dsu/"> https://oi-wiki.org/ds/dsu/</a>
 * 并查集  , 用于集合的合并
 *
 * 通常用法，
 * 图，往里面放线，查看联通性
 *
 */
public class UnionSet  {

    int[] fa = new int[1000];
    // 记录每个集合的大小
    int[] sizes = new int[1000];


    /**
     * 父亲设置为自己
     */
    public UnionSet(int n){
        fa = new int[n];
        for (int i = 0; i < n; i++) {
            fa[i] = i;
        }
        sizes = new int[n];


    }

    /**
     * 获取该元素的祖先
     */
    public int find(int x){
        if (fa[x] != x) {
            // 路径压缩，让祖先变父亲
            fa[x] = find(x);
        }
        return fa[x];
    }

    /**
     * 合并两个家族
     */
    public void union(int x, int y) {
        //同祖先 , 不需要合并
        int xx = fa[x], yy = fa[y];
        if (xx == yy) {
            return;
        }
        //yy的size比较小
        if (sizes[xx] <= sizes[yy]) {
            int t = yy;
            yy = xx;
            xx = t;
        }
        fa[yy] = xx;
        sizes[xx] += sizes[yy];
    }
}
