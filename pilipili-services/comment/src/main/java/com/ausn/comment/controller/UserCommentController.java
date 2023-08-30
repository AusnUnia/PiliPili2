package com.ausn.comment.controller;


import com.ausn.comment.service.UserCommentService;
import com.ausn.common.Result;
import com.ausn.common.ResultCode;
import com.ausn.entity.UserComment;
import com.ausn.entity.requestEntity.CommentPublishRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class UserCommentController
{
    @Autowired
    private UserCommentService userCommentService;

    //加载对应视频下的评论

    @GetMapping("/BV{bv}")
    public Result getByBv(@PathVariable String bv)
    {
        List<UserComment> userComments=userCommentService.getByBv(bv);
        System.out.println(userComments);
        return new Result(ResultCode.GET_OK,"",userComments);
    }

    //发布评论
    @PostMapping("/publish")
    public Result publish(@RequestBody CommentPublishRequest commentPublishRequest)
    {
        return userCommentService.publish(commentPublishRequest);
    }

    //删除评论
    @DeleteMapping("/BV{bv}")
    public Result delete(@PathVariable String bv,@RequestBody UserComment userComment)
    {
        boolean flag=userCommentService.delete(userComment);
        return new Result(123,"",flag);
    }
}
