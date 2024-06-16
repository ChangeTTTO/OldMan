package com.pn.service.impl;

import com.pn.entity.User;
import com.pn.mapper.UserMapper;
import com.pn.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pn
 * @since 2024-06-12 07:28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private  UserMapper userMapper;
    @Override
    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    @Override
    public void register(String email, String password) {

        userMapper.register(email, password);
    }


    @Override
    public User findUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

}
