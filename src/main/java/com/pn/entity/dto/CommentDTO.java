package com.pn.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
@ApiModel(value = "CommentDto", description = "")
public class CommentDTO {
    @ApiModelProperty("用户id")
    private Integer uid;

    @ApiModelProperty("文章id")
    private Integer articleId;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("评论父id")
    private Integer parentId;

    @ApiModelProperty("上传的文件")
    private MultipartFile[] files;
}
