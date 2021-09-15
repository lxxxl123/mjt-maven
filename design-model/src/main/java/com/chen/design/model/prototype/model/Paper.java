package com.chen.design.model.prototype.model;

import java.util.List;

/**
 * @author chenwh
 * @date 2021/9/15
 */

public class Paper implements Cloneable{
    public List<ChoiceQuestion> chooseQuestionList;


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
