package com.superchen.demo.application;

import android.annotation.SuppressLint;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.superchen.demo.util.CrashHandler;

/**
 * Created by sevnce on 2016/8/12.
 */
@SuppressLint("StaticFieldLeak")
public class Application extends android.app.Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        Fresco.initialize(this);
    }

}
