package com.sky.chowder.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import com.sky.chowder.R;

import java.io.File;

/**
 * Created by 李彬 on 2016/8/28.
 */

public class VideoActivity extends AppCompatActivity {
    VideoView video;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        video = (VideoView) findViewById(R.id.video);
        MediaController mediaController = new MediaController(this);
        File file = new File("/storage/emulated/0/QQBrowser/视频/隐世小龙女，她用颜值与功夫惊艳了时光.mp4");
        if (file.exists()) {
            video.setVideoPath(file.getAbsolutePath());
            video.setMediaController(mediaController);
            mediaController.setMediaPlayer(video);
            video.requestFocus();
            video.start();
        }

    }
}
