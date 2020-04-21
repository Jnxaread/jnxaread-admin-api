package com.jnxaread.controller;

import com.jnxaread.bean.Authority;
import com.jnxaread.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限信息测试类
 * @author 未央
 * @create 2020-04-21 21:08
 */
@RestController
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;

    @RequestMapping("/getList")
    public List<Authority> getList(){
        List<Authority> authorityList = authorityService.getAuthorityList();
        return authorityList;
    }

}
