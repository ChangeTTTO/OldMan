package com.pn.entity.dto;

import lombok.Data;

@Data
public class UserLoginDto {
    private Integer id;
    /**
     * 账号
     */
    private String email;
    /**
     * 密码
     */
    private String password;
}
