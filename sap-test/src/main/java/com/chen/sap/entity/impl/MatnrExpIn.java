package com.chen.sap.entity.impl;

import com.chen.sap.anno.SapTable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author chenwh3
 */
@SapTable(name = "IT_MATNR")
@Data
@AllArgsConstructor
public class MatnrExpIn {

    private String matnr;

}
