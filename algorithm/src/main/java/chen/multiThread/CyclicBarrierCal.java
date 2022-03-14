package chen.multiThread;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * @author chenwh
 * @date 2022/3/14
 */

public class CyclicBarrierCal {

    public static int threadNum = 10;

    public static CyclicBarrier cb = new CyclicBarrier(threadNum);

    public static long  calculate(long start, long end) {
        long sum = 0;
        for (long i = start; i <= end; i++) {
            sum += i;
        }
        return sum;
    }

    public static class Cal implements Callable<Long> {

        private long start;

        private long end;

        public Cal(long start, long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public Long call() throws Exception {
            return calculate(start, end);
        }
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        long l = 0, r = 1000000000;
        ExecutorService exe = Executors.newCachedThreadPool();
        long step = (long)Math.ceil((double) (r - l) / threadNum);
        long i = l;
        ArrayList<Future<Long>> futures = new ArrayList<>();
        while (i <= r) {
            long j = Math.min(i + step, r);
            Future<Long> submit = exe.submit(new Cal(i, j));
            futures.add(submit);
            i = j + 1;
        }
        long res = 0;
        for (Future<Long> future : futures) {
            res += future.get();
        }
        System.out.println(res);
        exe.shutdown();
        long end = System.currentTimeMillis();
        System.out.println(end - start);


        start = System.currentTimeMillis();
        System.out.println("res = " + calculate(l, r));
        end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
