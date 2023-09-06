package com.ausn.live.service;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;
import org.kurento.client.IceCandidate;
import org.kurento.client.KurentoClient;
import org.kurento.client.MediaPipeline;
import org.kurento.client.WebRtcEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
public class StreamService
{
    private static final String INPUT_FILE = "/path/to/input.mp4";
    private static final String OUTPUT_URL = "rtmp://192.168.107.128/live/livestream";

    public void startStream()
    {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(INPUT_FILE);
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(OUTPUT_URL, grabber.getImageWidth(), grabber.getImageHeight());

        try
        {
            grabber.start();
            recorder.setFrameRate(grabber.getFrameRate());
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setFormat("flv");
            recorder.setVideoBitrate(2000000);
            recorder.setVideoOption("preset", "ultrafast");
            recorder.setVideoOption("tune", "zerolatency");
            recorder.setVideoOption("crf", "18");
            recorder.setVideoOption("pix_fmt", "yuv420p");
            recorder.start();

            Frame frame;
            while ((frame = grabber.grabFrame()) != null) {
                recorder.record(frame);
            }

            recorder.stop();
            grabber.stop();
        } catch (FrameGrabber.Exception | FrameRecorder.Exception e) {
            e.printStackTrace();
        }
    }


}