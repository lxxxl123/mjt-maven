package chen.dp;


/**
 * 戳气球 ，有一排气球，错破编号i的话可获得 score[i-1] * score[i] * score[i+1] 的分数 , 且score[i]会消失。
 *
 * 1.戳气球转换为填气球
 * 2.状态转移方程 = dp[i][j] = for k in range(i,j): max( dp[k+1][j] + dp[i][k-1] + score[k]*score[k-1]*score[k+1] )
 */
public class balloonDp {

    /**
     */
    public int minimumEffortPath(int[][] heights) {

        return 0;



    }
}
