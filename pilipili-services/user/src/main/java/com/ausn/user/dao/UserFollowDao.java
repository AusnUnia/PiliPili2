package com.ausn.user.dao;

import com.ausn.entity.PUser;
import com.ausn.entity.UserFollow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: Ausn
 * @DateTime: 2023/9/24 19:00
 * @Description:
 */

@Mapper
public interface UserFollowDao  extends BaseMapper<UserFollow>
{
    List<Long> getAuthorIds(Long followerId);
}
