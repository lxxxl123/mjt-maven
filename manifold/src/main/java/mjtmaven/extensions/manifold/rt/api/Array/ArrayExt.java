package mjtmaven.extensions.manifold.rt.api.Array;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.Self;
import manifold.ext.rt.api.This;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author chenwh3
 */
@Extension
public class ArrayExt {
  public static void helloWorld(@This Object thiz) {
    System.out.println("hello world!");
  }

  public static List<@Self(true) Object> toList(@This Object thiz) {
    if (thiz.getClass().getComponentType().isPrimitive()) {
      int length = Array.getLength(thiz);
      List<Object> list = new ArrayList<>();
      for (int i = 0; i < length; i++) {
        list.add(Array.get(thiz, i));
      }
      return list;
    }
    return Arrays.asList((Object[]) thiz);
  }

}