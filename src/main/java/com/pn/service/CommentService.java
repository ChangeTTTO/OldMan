package com.pn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pn.entity.dto.CommentDTO;
import com.pn.entity.Comment;
import com.pn.util.PageInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pn
 * @since 2024-03-03 16:15
 */
public interface CommentService extends IService<Comment> {
    /**
     * 添加评论
     * @param commentDTO
     * @return
     */
    Comment _save(CommentDTO commentDTO,String city);
    /**
     * 根据文章id查询分页评论列表(评论对象里包含回复(reply)的数据)
     * @param pageNum
     * @param pageSize
     * @param articleId 文章id
     * @return
     */
    List<Comment> pageByAid(int pageNum, int pageSize, Integer articleId);

    /**
     * 分页查询回复
     * @param pageNum
     * @param pageSize
     * @param parentId 父评论id
     * @return
     */
    PageInfo<Comment> replyPage(int pageNum, int pageSize, Integer parentId);

    /**
     * 分页查询评论(评论对象里不包含回复(reply)的数据)
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<List<Comment>> page(int pageNum, int pageSize);

    /**
     * 批量修改点赞数量
     * @param list
     * @return
     */
    boolean updateBatchById(List<Comment> list);

}
