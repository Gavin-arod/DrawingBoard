package com.board.draw.app;

import android.app.Application;
import android.content.Context;

import com.board.draw.R;
import com.board.draw.util.Constant;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Constant.SP = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
    }
}
