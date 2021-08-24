package com.chen.spring.mvc.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author chenwh
 * @date 2021/8/20
 */
@Controller
public class LoginPageController {

    @GetMapping(value = "/v1/login")
    public ModelAndView show(@RequestParam(value = "error" , required = false)String error) {
        var mav = new ModelAndView();
        mav.setViewName("login");
        mav.addObject("error", error);
        mav.addObject("verifyCodeImgPath", "/v1/getCode");
        return mav;
    }
}
