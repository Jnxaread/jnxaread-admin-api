package com.jnxaread.service.impl;

import com.jnxaread.bean.User;
import com.jnxaread.bean.UserExample;
import com.jnxaread.dao.wrap.UserMapperWrap;
import com.jnxaread.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 未央
 * @create 2020-05-05 18:05
 */
@Service
public class UserServiceImpl extends BaseUserServiceImpl implements UserService {

    @Resource
    private UserMapperWrap userMapper;

    @Override
    public List<User> getUserList() {
        UserExample example = new UserExample();
        return userMapper.selectByExample(example);
    }

}
