package com.pn.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.pn.entity.IpLocationResponse;

import javax.servlet.http.HttpServletRequest;

public class IpUtils {

    public static String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    public static IpLocationResponse getCity(String ip){
        String res= HttpUtil.get("https://restapi.amap.com/v3/ip?key=fc2545ac287956284ecf925ec623ed1b&ip="+ip);
        IpLocationResponse bean = JSONUtil.toBean(res, IpLocationResponse.class);
        System.out.println(res);
        return bean;
    }
}
