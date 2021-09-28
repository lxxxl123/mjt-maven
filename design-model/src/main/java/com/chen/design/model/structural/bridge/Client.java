package com.chen.design.model.structural.bridge;

import java.util.List;

/**
 * @author chenwh
 * @date 2021/9/26
 */

public class Client {

    /**
     * Name: 桥接模式 **
     * Des:
     * Impl
     *  Scene: 设备 , 控制器
     *      impl1: TV , AirCon , TV-Controller , AirCon-BaseController ,
     *      TV-BaseController , AirCon-AdvanceController ,TV-AdvanceController
     *      impl2 : TV, AirCon , BaseController , AdvanceController (√)
     *  Scene: 颜色 , 形状
     *      impl3 : Shade , Color
     * Struct:
     *  1.Client: 整体控制器 , 由多个不同Impl组成
     *  2.Impl*: 具体实现,如形状,颜色等;
     * @param args
     */
    public static void main(String[] args) {
    }
}
