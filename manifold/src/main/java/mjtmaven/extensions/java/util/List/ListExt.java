package mjtmaven.extensions.java.util.List;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Extension
public class ListExt {

  public static <E> void helloWorld(@This List<E>  thiz) {
    System.out.println("hello world!");
  }

  public static <E> boolean isEmp(@This List<E>  thiz) {
    return !(thiz != null && thiz.size() > 0);
  }

  @Extension
  @SafeVarargs
  public static <E> List<E> of(E... elements) {
    return Collections.unmodifiableList(Arrays.asList(elements));
  }
}