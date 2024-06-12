package com.pn.mapper;

import com.pn.entity.Comment;
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

public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * 根据文章id查询评论列表(不包含回复)
     * @param articleId
     * @return
     */
    List<Comment> selectByAid(Integer articleId);

    /**
     * 通过parentId查询回复列表
     * @param parentId
     * @return
     */
    List<Comment> selectByPid(Integer parentId);

    /**
     * 批量查询回复
     * 条件: parentId
     * @param list
     * @return
     */
    @Deprecated
    List<Comment> selectBatchByPid(List<Integer> list);

    /**
     * 批量修改点赞数量
     * @param list
     * @return
     */
    int updateBatchById(List<Comment> list);

}
