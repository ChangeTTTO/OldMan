package com.pn.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "LikedDto", description = "评论点赞相关数据")
public class LikedDTO {
    @ApiModelProperty("评论id")
    private Integer commentId;

    @ApiModelProperty("用户id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer uid;
}
