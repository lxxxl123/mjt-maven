package chen.multiThread;

import com.sun.media.sound.SoftTuning;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author chenwh
 * @date 2022/3/14
 */

public class ForkJoinCalculate extends RecursiveTask<Long> {

    private long start;
    private long end;

    private static long THRESHOLD = 100000000;
//    private static long threadNum = 10;
//    private static long THRESHOLD = 100000000;

    public ForkJoinCalculate(long start, long end) {
        this.start = start;
        this.end = end;
    }


    @Override
    protected Long compute() {
        if (end - start > THRESHOLD) {
            long mid = (end + start) >> 1;
            long r1 = new ForkJoinCalculate(start, mid).fork().join();
            long r2 = new ForkJoinCalculate(mid + 1, end).fork().join();
            return r1 + r2;
        }else {
            return calculate(start, end);
        }
    }

    public static long  calculate(long start, long end) {
        long sum = 0;
        for (long i = start; i <= end; i++) {
            sum += i;
        }
        return sum;
    }

    public static void main(String[] args) {
        long l = 0;
        long r = 1000000000;

        long start = System.currentTimeMillis();
        System.out.println("res = " + calculate(l, r));
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        start = System.currentTimeMillis();
        ForkJoinCalculate task = new ForkJoinCalculate(l, r);
        Long res = new ForkJoinPool().invoke(task);
        System.out.println("res = " + res);
        end = System.currentTimeMillis();
        System.out.println(end - start);

    }
}
