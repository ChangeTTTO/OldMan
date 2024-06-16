package com.pn.service;

import com.pn.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pn
 * @since 2024-06-12 07:28
 */
public interface UserService extends IService<User> {
    /**
     * 根据邮箱查询用户
     * @param id
     * @return User
     */
    User getUserById(Integer id);

    void register(String email, String password);

    User findUserByEmail(String email);

}
