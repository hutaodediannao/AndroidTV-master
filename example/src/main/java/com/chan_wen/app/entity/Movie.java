package com.chan_wen.app.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
  * 版权: 公司所有
  * 作者: 胡涛
  * 创建日期:   2019-5-26
  * 创建时间:   10:52
  * 描述: 视频
  */
public class Movie extends BmobObject {

    private String title;
    private BmobFile videoFile;
    private BmobFile imageFile;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BmobFile getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(BmobFile videoFile) {
        this.videoFile = videoFile;
    }

    public BmobFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(BmobFile imageFile) {
        this.imageFile = imageFile;
    }
}
