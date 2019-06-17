package com.chan_wen.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

import com.chan_wen.app.player.VideoPlayerPageActivity;

/**
 * 版权:
 * 作者: 胡涛
 * 创建日期:   2019-6-17
 * 创建时间:   23:42
 * 描述: 主页
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button layVideo, layImage;
    private String voiceKey;
    public static final String MEDIA_TYPE = "mediaType";
    public static final String VIDEO = "video";
    public static final String  IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        Intent intent = getIntent();
        voiceKey = intent.getStringExtra("name");
        if (voiceKey != null) {
            //1、通过语音控制传参数启动app
            Intent i = new Intent(this, VideoPlayerPageActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra(VideoPlayerPageActivity.VOICE_KEY, voiceKey);
            startActivity(i);
            finish();
        }
    }

    private void initView() {
        layImage = findViewById(R.id.layImage);
        layVideo = findViewById(R.id.layVideo);
        layImage.setOnClickListener(this);
        layVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        switch (v.getId()) {
            case R.id.layImage:
                intent.putExtra(MEDIA_TYPE, IMAGE);
                break;
            case R.id.layVideo:
                intent.putExtra(MEDIA_TYPE, VIDEO);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}
