package com.pn.mapper;

import com.pn.entity.CommentLike;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
}
