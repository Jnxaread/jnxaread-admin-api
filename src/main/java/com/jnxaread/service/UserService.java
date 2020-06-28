package com.jnxaread.service;

import com.jnxaread.bean.User;

import java.util.List;

/**
 * @author 未央
 * @create 2020-05-05 18:05
 */
public interface UserService extends BaseUserService {

    List<User> getUserList();

}
