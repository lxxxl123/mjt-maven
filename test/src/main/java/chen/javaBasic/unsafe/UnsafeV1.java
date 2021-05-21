package chen.javaBasic.unsafe;


import java.lang.reflect.Field;


/**
 * @author chenwh
 * @date 2021/2/23
 */

public class UnsafeV1 {

    public static sun.misc.Unsafe getUnsafe() {
        try {
            Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (sun.misc.Unsafe) field.get(null);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(123);
    }
}
