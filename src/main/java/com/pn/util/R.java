package com.pn.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class R {
    private Integer code;//响应码，1 代表成功; 0 代表失败
    private String msg;  //响应信息 描述字符串
    private Object data; //返回的数据

    //增删改 成功响应
    public static R success(){
        return new R(1,"success",null);
    }
    //查询 成功响应
    public static R success(Object data){
        return new R(1,"success",data);
    }
    //失败响应
    public static R error(String msg){
        return new R(0,msg,null);
    }
}
