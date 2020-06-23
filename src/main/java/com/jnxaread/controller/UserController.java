package com.jnxaread.controller;

import com.jnxaread.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理Controller
 *
 * @author 未央
 * @create 2020-06-23 17:25
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

}
