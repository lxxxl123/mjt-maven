package com.chen.design.model.prototype.model;

import lombok.Data;

import java.util.Map;

/**
 * @author chenwh
 * @date 2021/9/15
 */
@Data
public class ChoiceQuestion {

    private String name;
    private Map<String,String> options;
    private String key;
}
