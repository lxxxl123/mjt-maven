package com.chen.sap.entity.impl;

import com.chen.sap.anno.SapField;
import com.chen.sap.anno.SapTable;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author chenwh3
 */
@SapTable(name = "OT_RETURN")
@Data
@ToString
public class MatnrExp {

    private String matnr;

    @SapField(name = "ZPDAT8")
    private Integer expDay;

    private Integer mandt;

    private Date datum;

}
