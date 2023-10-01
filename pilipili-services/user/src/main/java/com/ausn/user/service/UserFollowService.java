package com.ausn.user.service;

import com.ausn.entity.UserFollow;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Ausn
 * @DateTime: 2023/9/24 18:59
 * @Description:
 */

@Service
public interface UserFollowService extends IService<UserFollow>
{
    List<Long> getAuthorIds(Long followerId);
}
