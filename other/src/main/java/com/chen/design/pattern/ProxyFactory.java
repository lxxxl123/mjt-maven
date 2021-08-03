package com.chen.design.pattern;

import com.chen.design.pattern.proxy.model.Cat;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import javax.swing.*;
import java.lang.reflect.Method;

/**
 * @author chenwh
 * @date 2021/7/30
 */

public class ProxyFactory implements MethodInterceptor {

    private Object target;

    public ProxyFactory(Object tar) {
        this.target = tar;
    }

    public Object getProxyInstance(){
        Enhancer en = new Enhancer();
        en.setSuperclass(target.getClass());
        en.setCallback(this);
        return en.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("before");
        Object invoke = method.invoke(target, args);
        System.out.println("after");
        return invoke;
    }


    public static void main(String[] args) {
        Cat cat = new Cat();
        Cat proxyCat = ((Cat) new ProxyFactory(cat).getProxyInstance());
        proxyCat.cry();



    }
}
