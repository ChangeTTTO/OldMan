package com.pn.controller;

import cn.hutool.json.JSONUtil;
import com.pn.entity.User;
import com.pn.entity.dto.UserLoginDto;
import com.pn.entity.dto.userRegisterDto;
import com.pn.mapper.UserMapper;
import com.pn.service.UserService;
import com.pn.util.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

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
    @Resource
    private UserService userService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @PostMapping("/login")
    @ApiOperation( "登录")
    public Result login(@RequestBody UserLoginDto user, HttpServletRequest request){

        User dbUser = userService.findUserByEmail(user.getEmail());
        if (dbUser==null){
            return  Result.error("用户不存在");
        }

        String id = String.valueOf(dbUser.getId());
        String jsonStr = JSONUtil.toJsonStr(dbUser);
        //校验完成，登陆成功,将用户信息保存到redis
        stringRedisTemplate.opsForValue().set(id,jsonStr,30, TimeUnit.MINUTES);

        return Result.success(dbUser);
    }
    @PostMapping("/register")
    @ApiOperation("注册")
    public Result register(@RequestBody userRegisterDto userRegisterDTO){
        User userByEmail = userService.findUserByEmail(userRegisterDTO.getEmail());
        if (userByEmail!=null){
            return Result.error("该账号已存在");
        }
        userService.register(userRegisterDTO.getEmail(),userRegisterDTO.getPassword());
        return Result.success();
    }
    @GetMapping("/{id}")
    @ApiOperation("根据ID查用户")
    public Result getUserById(@PathVariable Integer id){
        return Result.success(userMapper.getUserVoById(id));
    }

    @GetMapping("/logout/{id}")
    @ApiOperation("退出登录")
    Result logout(@PathVariable String id){
        stringRedisTemplate.delete(id);
        return Result.success();
    }
}
