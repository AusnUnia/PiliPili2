package com.ausn.entity.converter;

import com.ausn.entity.Video;
import com.ausn.entity.requestEntity.VideoUploadRequest;

/**
 * @Author: 付显贵
 * @DateTime: 2023/7/27 22:48
 * @Description:
 */
public class VideoConverter
{

    public static Video toVideo(VideoUploadRequest videoUploadRequest) {
        if (videoUploadRequest == null)
        {
            return null;
        }
        Video video = new Video();
        video.setTitle(videoUploadRequest.getTitle());
        video.setTags(videoUploadRequest.getTags());
        video.setDescription(videoUploadRequest.getDescription());

        // Not mapped Video fields:
        // bv
        // authorId
        // viewNum
        // uploadDate
        // bulletScreenNum
        // commentNum
        // upvoteNum
        // downvoteNum
        // coinNum
        // saveNum
        // shareNum
        // videoPath
        return video;
    }
}
