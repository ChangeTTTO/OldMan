package com.pn.controller;

import com.pn.mapper.UserMapper;
import com.pn.util.R;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author pn
 * @since 2024-06-12 07:28
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserMapper userMapper;

    @GetMapping("{id}")
    @ApiOperation("根据ID查用户")
    public R getUserById(@PathVariable Long id){
        return R.success(userMapper.selectById(id));
    }
}
