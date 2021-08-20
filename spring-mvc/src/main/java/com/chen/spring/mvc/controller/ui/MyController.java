package com.chen.spring.mvc.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author chenwh
 * @date 2021/8/2
 */

@Controller
public class MyController {

    @GetMapping(value = "/")
    public String home() {

        return "index";
    }

    @GetMapping(value = "/show")
    public ModelAndView show() {

        var mav = new ModelAndView();

        var now = LocalDateTime.now();
        var formatter = DateTimeFormatter.ISO_DATE_TIME;
        var dateTimeFormatted = formatter.format(now);

        mav.addObject("now", dateTimeFormatted);
        mav.setViewName("show");

        return mav;
    }
}