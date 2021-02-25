package chen.javaBasic.reference;

import sun.misc.GC;

import java.io.PipedReader;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

/**
 * 应用场景:ThreadLocal
 * @author chenwh
 * @date 2021/2/24
 */
public class WeakReferenceTest {


    /**
     * 若一个对象只有弱引用关联 , 则会在下一次垃圾回收 被回收
     */
    public static void main(String[] args) {
        byte[] bytes = new byte[1024 * 1024 * 128];
        WeakReference weakReference = new WeakReference(bytes);
//        bytes = null;
        System.gc();
        System.out.println("end");
        try {
            TimeUnit.SECONDS.sleep(3600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
