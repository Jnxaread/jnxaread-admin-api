package com.jnxaread.controller;

import com.jnxaread.bean.Authority;
import com.jnxaread.service.AuthorityService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限信息测试类
 *
 * @author 未央
 * @create 2020-04-21 21:08
 */
@RestController
@RequestMapping("/auth")
public class AuthorityController {

    @Resource
    private AuthorityService authorityService;

    @RequestMapping("/list/auth")
    public List<Authority> getList() {
        return authorityService.getAuthorityList();
    }

}
