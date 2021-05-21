package chen.javaBasic.unsafe;


import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;


public class ByteBufferTest {

    public static void main(String[] args) throws Exception {
        while (true) {
            TimeUnit.SECONDS.sleep(5);
            System.out.println("开始分配100m内存");
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100 * 1024 * 1024);
        }
    }
}
