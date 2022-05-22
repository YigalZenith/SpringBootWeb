package com.imooc.spark;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HelloBoot {
    @GetMapping("/hello")
    public String hello(){
        return "Hello Spring Boot ...";
    }

    @GetMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("/static_pic")
    public ModelAndView static_pic() {
        return new ModelAndView("static_pic");
    }
}
