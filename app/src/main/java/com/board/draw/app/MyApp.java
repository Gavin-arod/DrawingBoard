package com.board.draw.app;

import android.app.Application;
import android.content.Context;

import com.board.draw.R;
import com.board.draw.crash.CrashHandler;
import com.board.draw.util.Constant;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化崩溃日志收集
        CrashHandler.getInstance().init(this);
        Constant.SP = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
    }
}
