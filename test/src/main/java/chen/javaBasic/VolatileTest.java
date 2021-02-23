package chen.javaBasic;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author chenwh
 * @date 2021/2/23
 */

public class VolatileTest {
    int a = 2;
    int b = 1;

    private void change(){
        a = 3;
        b = 5;
    }

    /**
     * 正常 :
     * a = 2 , b = 1
     * a = 3 , b = 3
     * 原子性 change 和 print间原子性的问题 : change 执行了一半 , print执行完
     * a = 3 , b = 1 (这个好像不会出现)
     * 线程可见性 : a 在 其他线程赋值 , 但在当前线程无法读取
     * a = 2 , b = 3
     */
    private void print(){
        System.out.println(String.format("a = %s , b = %s", a, b));
    }

    public static void main(String[] args) throws Exception{
        while (true){
            VolatileTest volatileTest = new VolatileTest();
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                volatileTest.change();
            });
            CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                volatileTest.print();
            });
            CompletableFuture.allOf(future, future1).get();
        }

    }
}
