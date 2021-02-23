package chen.javaBasic;

import sun.misc.Cleaner;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author chenwh
 * @date 2021/2/23
 */

public class ConcurrentTest {




    public static void main(String[] args) {
        ConcurrentLinkedDeque deque = new ConcurrentLinkedDeque();
        deque.add(1);
        System.out.println(deque.peek());

    }
}
