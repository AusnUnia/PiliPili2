package com.ausn.comment.service;

import com.ausn.common.Result;
import com.ausn.entity.UserComment;
import com.ausn.entity.requestEntity.CommentPublishRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserCommentService extends IService<UserComment>
{
    public Result publish(CommentPublishRequest commentPublishRequest);
    public boolean delete(UserComment userComment);
    public List<UserComment> getByBv(String bv);
    public boolean update(UserComment userComment);
}
