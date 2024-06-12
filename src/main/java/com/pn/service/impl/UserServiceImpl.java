package com.pn.service.impl;

import com.pn.entity.User;
import com.pn.mapper.UserMapper;
import com.pn.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
