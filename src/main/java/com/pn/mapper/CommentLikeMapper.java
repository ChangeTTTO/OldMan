package com.pn.mapper;

import com.pn.entity.CommentLike;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pn.entity.dto.LikedDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pn
 * @since 2024-03-03 16:15
 */

public interface CommentLikeMapper extends BaseMapper<CommentLike> {
    /**
     * 批量删除评论点赞的状态
     * @param list
     */
    int deleteBatchByCid(List<Integer> list);

    /**
     * 根据用户ID和评论ID查询点赞数据
     */
    CommentLike selectCommentLike(LikedDTO likedDTO);

    /**
     * 根据用户ID和评论ID移除点赞数据
     */
    int removeCommentLike(LikedDTO likedDTO);

    /**
     * 根据用户ID和评论ID进行点赞
     */
    int setCommentLike(LikedDTO likedDTO);

}
