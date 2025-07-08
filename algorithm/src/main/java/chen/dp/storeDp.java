package chen.dp;


/**
 * 碎石dp：最后一块石头的重量
 * 有一堆石头，任选两块，撞击成一块，生成一块新的重量为两块的重量的差的绝对值，之后最后剩下一块
 * 本质是0-1背包
 *
 * 1.设sum  = sum(stone[i])
 * 2. 首先忽略中间的过程，完美的情况是，正确的答案始终是一堆大石头 减去 一堆小石头
 * 4.设右边的石头堆为 rSum, 特点是左边的石头的最大值永远大于右边的石头 , 左边的石头堆为 sum - rSum
 * 5. res = sum - 2*rSum ， 因此， rSum 必然尽可能接近 sum/2 则可获得最小
 *
 */
public class storeDp {

    /**
     * i : 0 ~ size-1
     * j : 0 ~ sum/2
     *
     * dp[i][j] : 最大石头重量 ， 且小于sum/2
     *
     * dp[i][j] = max( dp[i-1][j], dp[i-1][j-stone[i]] + stone[i] )
     */
    public int lastStoneWeightII(int[] stones) {
        return 0;

    }
}
