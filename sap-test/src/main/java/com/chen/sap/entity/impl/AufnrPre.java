package com.chen.sap.entity.impl;

import com.chen.sap.anno.SapField;
import com.chen.sap.anno.SapTable;
import lombok.Data;
import lombok.ToString;

/**
 * @author chenwh3
 */
@SapTable(name = "TB_RETURN")
@ToString
@Data
public class AufnrPre {

    private String aufnr;

    private String matnr;

    @SapField(name = "MAKTX")
    private String ktextmat;

    @SapField(name = "WERKS")
    private String werk;

    private String arbid;

    private String vornr;

    @SapField(name = "KTEXT1")
    private String cpdLine;

}
