package chen.javaBasic.reference;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 内存飙升有可能导致来不及回收
 * 应用场景:大数据量场景 ,MMO时及时抛出异常
 * @author chenwh
 * @date 2021/2/23
 */
public class SoftReferenceTest {


    //偶尔会内存溢出
    private void testSofe(){
        int size = 500000;
        SoftReference<LinkedList> ref = new SoftReference<>(new LinkedList<>());

        //char 1b
        //UUID len = 36 , bytes ≈ 68
        //*1Million = 3
        while (size-- > 0) {
            if (ref.get() != null) {
                ref.get().add(new byte[1024]);
            } else {
                System.out.println("弱引用被删除");
                break;
            }
            System.out.println(size);
        }
        System.out.println("end");


    }
    public static void main(String[] args) throws Exception{
        System.out.println("test start");
        new SoftReferenceTest().testSofe();
        while (true) {
            TimeUnit.SECONDS.sleep(3600);
        }
    }
}
