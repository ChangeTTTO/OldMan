package com.pn.mapper;

import com.pn.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pn.entity.vo.userVo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pn
 * @since 2024-06-12 07:28
 */
public interface UserMapper extends BaseMapper<User> {

    User getUserById(Integer id);

    void register(String email, String password);

    userVo getUserVoById(Integer id);
    User getUserByEmail(String email);
}
