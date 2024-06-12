package com.pn.service;

import com.pn.dto.LikedDTO;
import com.pn.entity.CommentLike;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pn
 * @since 2024-03-03 16:15
 */
public interface CommentLikeService extends IService<CommentLike> {
    /**
     * 批量删除评论点赞的状态
     * @param cList
     */
    boolean removeBatchByCid(List<Integer> cList);

    /**
     * 根据用户id获取点赞的评论id列表
     * @param uid
     * @return
     */
    List<Integer> cidListByUid(Integer uid);

    /**
     * 评论点赞
     * @param likedDTO
     */
    boolean liked(LikedDTO likedDTO);


    /**
     *  查询用户是否点赞
     * @param likedDTO
     * @return boolean
     */
    boolean isLiked(LikedDTO likedDTO);

    /**
     * redis同步点赞数量和状态到mysql
     */
    boolean syncLike();
}
