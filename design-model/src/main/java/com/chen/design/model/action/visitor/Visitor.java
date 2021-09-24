package com.chen.design.model.action.visitor;

/**
 * @author chenwh
 * @date 2021/9/24
 */

public class Visitor {

    public void visit(Shade shade) {
        if (shade instanceof Circular) {
            System.out.println("this is circular");
        } else if (shade instanceof Square) {
            System.out.println("this is square");
        }
    }

    public void export(Shade... shades) {
        for (int i = 0; i < shades.length; i++) {
            shades[i].visit(this);
        }
    }

    /**
     * 对图形增加导出功能完全不需要改变原来的类,  只需要在接口增加visit方法
     * @param args
     */
    public static void main(String[] args) {
        new Visitor().export(new Circular(), new Square());
    }
}
