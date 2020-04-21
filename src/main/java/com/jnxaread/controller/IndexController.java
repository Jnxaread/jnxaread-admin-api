package com.jnxaread.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 索引Controller
 *
 * @author 未央
 * @create 2020-04-21 20:56
 */
@RestController
public class IndexController {

    @RequestMapping("/hello")
    public String hello() {
        return "hello world, hello admin";
    }

}
