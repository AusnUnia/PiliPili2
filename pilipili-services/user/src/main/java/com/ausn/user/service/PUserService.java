package com.ausn.user.service;

import com.ausn.common.Result;
import com.ausn.entity.PUser;
import com.ausn.entity.VideoFeedStream;
import com.ausn.entity.dto.LoginFormDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

public interface PUserService extends IService<PUser>
{
    boolean refreshVideoList(Long userId, String lastBv, LocalDateTime startTime);

    List<VideoFeedStream> getNewVideoList(Long userId, String lastBv, LocalDateTime time);
}
