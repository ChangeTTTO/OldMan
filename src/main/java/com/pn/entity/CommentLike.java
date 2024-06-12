package com.pn.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author pn
 * @since 2024-03-03 16:15
 */
@TableName("comment_like")
@ApiModel(value = "CommentLike对象", description = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentLike implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("评论点赞ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    private Integer uid;

    @ApiModelProperty("评论id")
    private Integer commentId;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}