package com.chan_wen.app.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
  * 版权: 公司所有
  * 作者: 胡涛
  * 创建日期:   2019-5-26
  * 创建时间:   10:54
  * 描述: 图片
  */
public class Image extends BmobObject {

    private String title;
    private BmobFile imgFile;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BmobFile getImgFile() {
        return imgFile;
    }

    public void setImgFile(BmobFile imgFile) {
        this.imgFile = imgFile;
    }
}
