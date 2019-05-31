package com.chan_wen.app;

import android.app.Application;

import com.chan_wen.app.config.Constant;

import cn.bmob.v3.Bmob;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;

/**
 * Created by owen on 2017/8/8.
 * 
 */

public class App extends Application {
    private static App instance;
    final static float DESIGN_WIDTH = 1280; //绘制页面时参照的设计图宽度
    
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AutoSizeConfig.getInstance().getUnitsManager().setSupportSubunits(Subunits.PT);
        //第一：默认初始化
        Bmob.initialize(this, Constant.APP_KEY);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
    
    public static App get() {
        return instance;
    }
}
