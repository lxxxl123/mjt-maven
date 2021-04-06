package chen.algorithm;

/**
 * 位运算相关
 * @author chenwh
 * @date 2021/3/22
 */

public class Bit {

    /**
     * 最右1置0
     * @return
     */
    private static int setRightBitZero(int i){
        return i & (i - 1);
    }

    /**
     * 仅获取最右1
     * @return 00100...
     */
    private static int findRightBit(int i) {
        // i & -i
        return i & ~(i - 1);
    }

    public static void main(String[] args) {
        System.out.println(findRightBit());
    }
}
