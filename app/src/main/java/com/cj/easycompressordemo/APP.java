package com.cj.easycompressordemo;

import android.app.Application;

import com.cj.easycompressor.config.CompressOptions;
import com.cj.easycompressor.core.EasyCompressor;

/**
 * Created by mayikang on 2018/9/20.
 */

public class APP extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        EasyCompressor.init(this);
        Thread.setDefaultUncaughtExceptionHandler(CrashHandler.getInstance(this));
    }




}
