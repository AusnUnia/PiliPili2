package com.ausn.security.dao;

import com.ausn.entity.PUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PUserDao extends BaseMapper<PUser>
{
    public int save(PUser pUser);

    public int delete(PUser pUser);
    public PUser getByUid(@Param("uid") Long uid);
    public PUser getByPhoneNumber(@Param("phoneNumber") String phoneNumber);
    public Long getLastInsertedId();
}
