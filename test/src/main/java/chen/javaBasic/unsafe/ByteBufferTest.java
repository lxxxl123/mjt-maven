package chen.javaBasic.unsafe;


import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;


public class ByteBufferTest {

    public static void main(String[] args) throws Exception {
        TimeUnit.SECONDS.sleep(2);
        System.out.println("开始分配100m内存");
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100 * 1024 * 1024);
        TimeUnit.SECONDS.sleep(10000);
    }
}
