package com.pn.controller;


import cn.undraw.util.result.R;
import com.pn.entity.IpLocationResponse;
import com.pn.entity.dto.CommentDTO;
import com.pn.entity.dto.LikedDTO;
import com.pn.entity.Comment;
import com.pn.service.CommentLikeService;
import com.pn.service.CommentService;
import com.pn.util.IpUtils;
import com.pn.util.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author pn
 * @since 2024-03-03 16:15
 */
@RestController
@Api(tags = "评论管理")
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;

    @Resource
    private CommentLikeService commentLikeService;


    @ApiOperation("分页查询评论列表")
    @GetMapping("/page/{pageNum}/{pageSize}")
    public R<List<Comment>> page(@PathVariable int pageNum, @PathVariable int pageSize, Integer articleId) {
        return R.ok(commentService.pageByAid(pageNum, pageSize, articleId));
    }

    @ApiOperation("回复分页查询")
    @GetMapping("/replyPage/{pageNum}/{pageSize}")
    public R<PageInfo<Comment>> replyPage(@PathVariable int pageNum, @PathVariable int pageSize, Integer parentId) {
        return R.ok(commentService.replyPage(pageNum, pageSize, parentId));
    }

    @ApiOperation("添加评论")
    @PostMapping("/save")
    public R<?> save(HttpServletRequest request, @RequestBody CommentDTO commentDTO) {
        // 获取客户端IP地址
        String ip = IpUtils.getClientIpAddress(request);
        System.out.println("IP地址是:"+ip);
        // 获取城市信息
        IpLocationResponse bean = IpUtils.getCity(ip);
        String province = bean.getProvince();
        System.out.println("城市是:"+province);
        // 保存评论
        return R.ok(commentService._save(commentDTO,province));
    }

    @ApiOperation("删除评论")
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable Integer id) {
        return R.ok(commentService.removeById(id));
    }

    @ApiOperation("评论点赞")
    @PostMapping("/liked")
    public R<?> liked(@RequestBody LikedDTO likedDTO) {
        likedDTO.setUid(1);
        commentLikeService.liked(likedDTO);
        return R.ok();
    }

    @ApiOperation("查询当前用户的点赞的评论id")
    @GetMapping("/cidList/{uid}")
    public R<?> cidList(@PathVariable Integer uid) {
        return R.ok(commentLikeService.cidListByUid(uid));
    }
}



