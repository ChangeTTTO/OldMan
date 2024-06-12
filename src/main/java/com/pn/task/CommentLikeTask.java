package com.pn.task;

import com.pn.service.CommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CommentLikeTask {
    @Autowired
    private CommentLikeService commentLikeService;
    // cron在线表达生成器: http://cron.ciding.cc/
    // 秒 分钟 小时 日 月 星期 年

    /**
     * 每天 00::00::00执行
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void task2() {
        // redis同步点赞数量和状态到mysql
        commentLikeService.syncLike();
    }
}