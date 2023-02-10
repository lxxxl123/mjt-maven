package com.chen.sap.entity.impl;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author chenwh3
 */
@Data
@AllArgsConstructor
public class SapRes<T> {

    private List<List<?>> tables;

    private T params;


}
