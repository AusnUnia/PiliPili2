package com.ausn.user.service.impl;

import com.ausn.entity.UserFollow;
import com.ausn.user.dao.UserFollowDao;
import com.ausn.user.service.UserFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author: Ausn
 * @DateTime: 2023/9/24 18:59
 * @Description:
 */
public class UserFollowServiceImpl extends ServiceImpl<UserFollowDao, UserFollow> implements UserFollowService
{
    @Autowired
    private UserFollowDao userFollowDao;

    @Override
    public List<Long> getAuthorIds(Long followerId)
    {
        return userFollowDao.getAuthorIds(followerId);
    }
}
