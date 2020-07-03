package com.jnxaread.service.impl;

import com.jnxaread.bean.User;
import com.jnxaread.bean.UserExample;
import com.jnxaread.dao.UserMapper;
import com.jnxaread.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 未央
 * @create 2020-05-05 18:05
 */
@Service
public class UserServiceImpl extends BaseUserServiceImpl implements UserService {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Override
    public List<User> getUserList() {
        UserExample example = new UserExample();
        List<User> userList = userMapper.selectByExample(example);
        return userList;
    }

}