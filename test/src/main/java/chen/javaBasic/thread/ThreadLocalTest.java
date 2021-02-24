package chen.javaBasic.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author chenwh
 * @date 2021/2/24
 */

public class ThreadLocalTest {


    public static void main(String[] args) {

        ThreadLocal threadLocal1 = new ThreadLocal();
        ThreadLocal threadLocal2 = new ThreadLocal();
        threadLocal1.set(1);
        System.out.println(threadLocal1.get());
        System.out.println(threadLocal2.get());

    }
}
