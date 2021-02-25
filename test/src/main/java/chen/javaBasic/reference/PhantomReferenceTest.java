package chen.javaBasic.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

/**
 *
 * @see <a href="https://zhuanlan.zhihu.com/p/55114919">https://zhuanlan.zhihu.com/p/55114919</a>
 * 虚引用 , gc不会回收 , 但会收到通知
 * 必须在clear 或者把引用置null 后才会回收
 * 应用场景: 数据库连接池
 */
public class PhantomReferenceTest {


   public static void main(String[] args)
      throws Exception {
      byte[] bytes = new byte[1024 * 1024 * 128];
      ReferenceQueue rq = new ReferenceQueue();
      PhantomReference pr = new PhantomReference (bytes , rq);
      bytes = null;
      // 取出虚引用所引用的对象，并不能通过虚引用获取被引用的对象，所以此处输出null
//      System.out.println(pr.get());
      // 强制垃圾回收
      TimeUnit.SECONDS.sleep(2);
      System.out.println("开始垃圾回收");
      System.gc();
      System.runFinalization();
      // 垃圾回收之后，虚引用将被放入引用队列中
      System.out.println("垃圾回收结束");
      TimeUnit.SECONDS.sleep(2);

      // 取出引用队列中最先进入队列中的引用与pr进行比较
      System.out.println(rq.poll() == pr);

      TimeUnit.SECONDS.sleep(4);

      pr.clear();
      System.out.println("开始垃圾回收2");
      System.gc();
      System.runFinalization();
      // 垃圾回收之后，虚引用将被放入引用队列中
      System.out.println("垃圾回收结束2");
      System.out.println(pr == null);

      System.out.println("end");

      TimeUnit.SECONDS.sleep(10);







   }
}