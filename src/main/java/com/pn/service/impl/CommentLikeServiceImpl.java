package com.pn.service.impl;

import cn.undraw.util.ConvertUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.entity.dto.LikedDTO;
import com.pn.entity.Comment;
import com.pn.entity.CommentLike;
import com.pn.mapper.CommentLikeMapper;
import com.pn.mapper.CommonMapper;
import com.pn.service.CommentLikeService;
import com.pn.service.CommentService;
import com.pn.util.CommentKeyUtil;
import com.pn.util.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pn
 * @since 2024-03-03 16:15
 */
@Service
public class CommentLikeServiceImpl extends ServiceImpl<CommentLikeMapper, CommentLike> implements CommentLikeService {
    @Autowired
    private CommentLikeMapper commentLikeMapper;
    
    @Autowired
    private CommentService commentService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CommentKeyUtil commentKeyUtil;

    @Resource
    private CommonMapper commonMapper;

    @Override
    public boolean removeBatchByCid(List<Integer> cList) {
        return commentLikeMapper.deleteBatchByCid(cList) > 0;
    }


    /**
     * 获取mysql点赞状态
     * @param likedDTO
     * @return com.pn.entity.CommentLike
     */
    private CommentLike getByLikedDTO(LikedDTO likedDTO) {
        // 获取mysql中的点赞状态
        return commentLikeMapper.selectOne(
                new QueryWrapper<CommentLike>()
                        .eq("uid", likedDTO.getUid())
                        .eq("comment_id", likedDTO.getCommentId())
        );
    }

    @Override
    public List<Integer> cidListByUid(Integer uid) {
        List<Integer> list = new ArrayList<>();

        // 获取redis中评论点赞评论Id列表
        List<Integer> trueCidList = cidListByUid(uid, true);
        // 获取redis中取消评论点赞评论Id列表
        List<Integer> falseCidList = cidListByUid(uid, false);
        // mysql中的评论id
        List<CommentLike> mysqlCLikeList = commentLikeMapper.selectList(
                new QueryWrapper<CommentLike>()
                        .eq("uid", uid)
        );
        // 去除mysql在redis中取消点赞对应的评论id
        List<Integer> mysqlCidList = mysqlCLikeList.stream().map(t -> t.getCommentId()).filter(t -> !falseCidList.contains(t)).collect(Collectors.toList());

        list.addAll(trueCidList);
        list.addAll(mysqlCidList);
        return list;
    }

    @Override
    @Transactional
    public boolean liked(LikedDTO likedDTO) {
        //1.根据UID和评论ID生成Key
       String key = "COMMENT_LIKE:"+likedDTO.getCommentId();
        //2.查看redis中是否已经点过赞
        boolean state = redisUtil.sHasKey(key, likedDTO.getUid());
        //3redis中没有，查数据库
        CommentLike commentLike = commentLikeMapper.selectCommentLike(likedDTO);
        //如果已经点过赞
        if (state && commentLike != null){
            //从数据库中移除对应的点赞信息
            commentLikeMapper.removeCommentLike(likedDTO);
            //从redis中移除对应的点赞信息
            redisUtil.sRemove(key, likedDTO.getUid());
            //从数据库移除点赞数
            commonMapper.decrementCount("comment", String.valueOf(likedDTO.getCommentId()),"likes");

        }else{
            //如果没有点过赞，那么进行点赞,数据存入MySQL
            commentLikeMapper.setCommentLike(likedDTO);
            //从数据库增加点赞数
            commonMapper.incrementCount("comment", String.valueOf(likedDTO.getCommentId()),"likes");
            //数据存入Redis
            redisUtil.sSet(key, likedDTO.getUid());
        }

      /*  // 查询用户是否点赞
        boolean state = this.isLiked(likedDTO);
        if (state) {
            //如果已点赞，移除点赞信息，正常情况是返回true
            boolean state2 = commentKeyUtil.remLike(likedDTO, true);
            //如果移除失败，手动更新点赞状态
            if (!state2) {
                commentKeyUtil.setLike(likedDTO, false);
            }
        } else {
            //如果用户没有点赞，则进行点赞
            commentKeyUtil.setLike(likedDTO, true);
        }
       */
        return true;
    }

    @Override
    public boolean isLiked(LikedDTO likedDTO) {
        // 查询redis中是否点赞，查看是否有值
        boolean state = commentKeyUtil.isLike(likedDTO, true);
        if (state) {
            return true;
        } else {
            // redis中没有点赞，查询redis是否取消点赞
            boolean cancel = commentKeyUtil.isLike(likedDTO, false);
            // 如果没取消点赞，查询mysql是否点赞
            if (!cancel) {
                CommentLike commentLike = this.getByLikedDTO(likedDTO);
                // commentLike不等于null，mysql点赞了
                return commentLike != null;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public boolean syncLike() {
        like();
        cancelLike();
        return true;
    }

    /**
     * 点赞同步
     */
    private void like() {
        // 获取所有评论点赞key值列表
        List<String> keyList = commentKeyUtil.getLikeList(true);
        // 评论点赞用户id列表
        List<CommentLike> commentLikes = new ArrayList<>();
        // 评论点赞数量列表
        List<Comment> commentList = new ArrayList<>();

        for (String key : keyList) {
            String[] split = key.split(":");
            Integer articleId = ConvertUtils.toInteger(split[2]);
            // 获取评论点赞用户id列表
            Set<Integer> uidList = redisUtil.sGet(key);
            for (Integer uid : uidList) {
                CommentLike commentLike = new CommentLike();
                commentLike.setCommentId(articleId);
                commentLike.setUid(ConvertUtils.toInteger(uid));
                commentLikes.add(commentLike);
            }

            // 获取评论点赞数量
            long count = redisUtil.sSize(key);
            Comment comment = new Comment();
            comment.setId(articleId);
            comment.setLikes((int)count);
            commentList.add(comment);
        }

        //添加点赞状态
        this.saveBatch(commentLikes);
        //修改点赞数量
        commentService.updateBatchById(commentList);
        // 清空redis点赞数据
        redisUtil.delByMatch(CommentKeyUtil.COMMENT_LIKE + "*");
    }

    /**
     * 取消点赞同步
     * @return void
     */
    private void cancelLike() {
        // 获取所有取消评论点赞key值列表
        List<String> keyList = commentKeyUtil.getLikeList(false);
        // 取消评论点赞评论id列表
        List<Integer> cCancelLikes = new ArrayList<>();
        // 取消评论点赞数量列表
        List<Comment> cCancelList = new ArrayList<>();

        for (String key : keyList) {
            String[] split = key.split(":");
            Integer articleId = ConvertUtils.toInteger(split[2]);

            // 获取评论点赞数量
            long count = redisUtil.sSize(key);
            Comment comment = new Comment();
            comment.setId(articleId);
            comment.setLikes(-(int)count);
            cCancelList.add(comment);
            cCancelLikes.add(articleId);
        }

        //删除点赞状态
        this.removeBatchByCid(cCancelLikes);
        //修改点赞数量
        commentService.updateBatchById(cCancelList);
        // 清空redis取消点赞数据
        redisUtil.delByMatch(CommentKeyUtil.COMMENT_CANCEL_LIKE + "*");
    }

    /**
     * 根据key获取点赞或取消点赞的评论id列表
     * @param uid
     * @param state 是否点赞
     * @return java.util.List<java.lang.Integer>
     */
    private List<Integer> cidListByUid(Integer uid, boolean state) {
        List<Integer> cidList = new ArrayList();
        List<String> keyList = commentKeyUtil.getLikeList(state);

        for (String str : keyList) {
            String commentId = str.split(":")[2];
            // 获取redis中的评论点赞用户id列表
            Set<Integer> uidSet = redisUtil.sGet(str);
            for (Integer uid2 : uidSet) {
                if (uid2 == uid) {
                    cidList.add(ConvertUtils.toInteger(commentId));
                }
            }
        }
        return cidList;
    }

}
