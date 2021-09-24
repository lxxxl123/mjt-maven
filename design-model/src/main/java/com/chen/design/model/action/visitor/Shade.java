package com.chen.design.model.action.visitor;

/**
 * @author chenwh
 * @date 2021/9/24
 */

public interface Shade {

    default void visit(Visitor visitor) {
        visitor.visit(this);

    }
}
