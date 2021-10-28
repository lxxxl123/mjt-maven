package chen.zk.test;



import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


public class Main {


    public static void main(String[] args) {

        ReentrantLock lock = new ReentrantLock();
        CompletableFuture.runAsync(() -> {
            lock.lock();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();
        });

        Thread thread = new Thread(() -> {
            try {
                lock.tryLock(5, TimeUnit.SECONDS);

            } catch (InterruptedException e) {
                System.out.println("int");
            }finally {
                if (lock.isLocked()) {
                    lock.unlock();
                }
            }
            System.out.println(123);
        });
        thread.start();


        thread.interrupt();
        System.out.println("interrupt");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
