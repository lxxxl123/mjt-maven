package com.chen.sap.entity.impl;

import com.chen.sap.anno.SapField;
import com.chen.sap.anno.SapTable;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author chenwh3
 */
@SapTable(name = "T_QALS")
@Data
public class SapYdQals {

    private String prueflos;
    private String art;
    private Date enstehdat;
    private Boolean sflag;
    private Boolean dflag;
    private String stats;

}
