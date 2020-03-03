package com.yezi.yezibbs;

import android.app.Application;

import com.yezi.baselibrary.exception.ExceptionCrashHandler;

/**
 * @author : yezi
 * @date : 2020/3/3 15:46
 * desc   :
 * version: 1.0
 */
public class YeziApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ExceptionCrashHandler.getInstance().init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
