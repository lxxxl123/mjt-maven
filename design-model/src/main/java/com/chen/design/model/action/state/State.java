package com.chen.design.model.action.state;

import lombok.AllArgsConstructor;

/**
 * @author chenwh
 * @date 2021/9/24
 */
@AllArgsConstructor
public abstract class State {

    protected Player player;

    public abstract void play();
}
