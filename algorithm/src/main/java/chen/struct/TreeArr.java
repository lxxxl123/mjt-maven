package chen.struct;


/**
 * 树状数组                           4         1          2         3       4
 * 作用 : 修改后快速求前缀和           100     100-100   100 - 100 +1
 * 概述 : 维护一个数组 c ,原数组为 a , c[i] = a[i-2^k+1] + a[i-2^k+2] + ... + a[i]
 * - 其中 k 为 i 最低位开始连续0的数量
 *
 */
public class TreeArr {



}
