package com.pn.service.impl;

import cn.undraw.util.ConvertUtils;
import cn.undraw.util.FileUtils;
import cn.undraw.util.StrUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.entity.dto.CommentDTO;
import com.pn.entity.Comment;
import com.pn.entity.User;
import com.pn.mapper.CommentMapper;
import com.pn.mapper.UserMapper;
import com.pn.service.CommentService;
import com.pn.service.UserService;
import com.pn.util.CommentKeyUtil;
import com.pn.util.PageInfo;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pn
 * @since 2024-03-03 16:15
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public Comment _save(CommentDTO commentDTO,String city) {
        //获取当前登录用户
        Integer id = commentDTO.getUid();
        User user = userMapper.getUserById(id);
        String comment_img = null;
        if (StrUtils.isNotEmpty(commentDTO.getFiles())) {
            comment_img = FileUtils.upload(commentDTO.getFiles(), "comment_img");
        }
        Comment comment = new Comment(null, commentDTO.getParentId(), commentDTO.getArticleId(),
                id, city, commentDTO.getContent(), 0, null, null, user, null);
        comment.setContentImg(comment_img);
        ConvertUtils.copy(commentDTO, comment);
        if (commentMapper.insert(comment) < 1) {
            throw new RuntimeException();
        }
        return comment;
    }

    @Override
    public List<Comment> pageByAid(int pageNum, int pageSize, Integer articleId) {
        PageHelper.startPage(pageNum, pageSize);
        // 根据文章id查询评论列表(不包含回复)
        List<Comment> parentList = commentMapper.selectByAid(articleId);
        // 添加评论对应的回复
        for (int i = 0; i < parentList.size(); i++) {
            Comment comment = parentList.get(i);
            Integer id = comment.getId();
            /*// 合并redis中的点赞数量
            comment.setLikes(comment.getLikes() + getLikeCount(id));*/
            // 分页查询回复
            PageInfo<Comment> replyPage = replyPage(1, 5, id);
            Comment.Reply reply = new Comment.Reply();
            // 拷贝
            ConvertUtils.copy(replyPage, reply);
            comment.setReply(reply);
        }

        return parentList;
    }

    @Override
    public PageInfo<Comment> replyPage(int pageNum, int pageSize, Integer parentId) {
        PageHelper.startPage(pageNum, pageSize);
        // 通过parentId查询回复列表
        List<Comment> replyList = commentMapper.selectByPid(parentId);
        for (Comment reply : replyList) {
            Integer id = reply.getId();
            // 合并redis中的点赞数量
            reply.setLikes(reply.getLikes() + getLikeCount(id));
        }
        return new PageInfo<>(replyList);
    }

    @Override
    public PageInfo<List<Comment>> page(int pageNum, int pageSize) {
        return null;
    }

    @Override
    public boolean updateBatchById(List<Comment> list) {
        return commentMapper.updateBatchById(list) > 0;
    }

    @Autowired
    private CommentKeyUtil commentKeyUtil;

    /**
     * 获取redis中的评论点赞数量
     * @param commentId
     * @return void
     */
    private int getLikeCount(Integer commentId) {
        long count = commentKeyUtil.getLikeSize(commentId, true);
        long cancelCount = commentKeyUtil.getLikeSize(commentId, false);
        return (int)(count - cancelCount);
    }

}
