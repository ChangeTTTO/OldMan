package com.pn.entity;

import lombok.Data;
import lombok.Getter;

@Data
public class IpLocationResponse {
    private String status;
    private String info;
    private String infocode;
    private String province;
    private String city;
    private String adcode;
    private String rectangle;

}