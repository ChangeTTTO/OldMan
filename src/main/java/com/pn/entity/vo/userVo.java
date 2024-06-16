package com.pn.entity.vo;

import lombok.Data;

import java.util.ArrayList;

@Data
public class userVo {
    private Integer id;
    private String username;
    private String avatar;
    private ArrayList<Integer> likeIds;
}
