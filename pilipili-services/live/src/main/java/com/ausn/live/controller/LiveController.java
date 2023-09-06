package com.ausn.live.controller;


import org.kurento.client.IceCandidate;
import org.kurento.client.KurentoClient;
import org.kurento.client.MediaPipeline;
import org.kurento.client.WebRtcEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/live")
public class LiveController
{
    @Autowired
    private KurentoClient kurentoClient;


    @PostMapping("/startStreaming")
    public ResponseEntity<String> startStreaming()
    {
        MediaPipeline pipeline=kurentoClient.createMediaPipeline();
        WebRtcEndpoint webRtcEndpoint = new WebRtcEndpoint.Builder(pipeline).build();

        webRtcEndpoint.addIceCandidateFoundListener(event -> {
            IceCandidate candidate = event.getCandidate();
            // 处理ICE候选项
        });

        String sdpOffer = webRtcEndpoint.generateOffer();

        // 返回SDP offer给前端
        return ResponseEntity.ok(sdpOffer);
    }

}
