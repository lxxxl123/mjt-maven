package com.chen.front.end.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * @author chenwh
 * @date 2021/8/2
 */

@RestController
public class CarController {


    @RequestMapping(value = "/cars", method = RequestMethod.GET)
    public String init(@ModelAttribute("model") ModelMap model) {
        ArrayList<String> list = new ArrayList<>();
        list.add("123");
        list.add("234");
        model.addAttribute("carList", list);
        return "index";
    }

}
