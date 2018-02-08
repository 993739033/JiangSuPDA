package com.mingnong.scanappnew;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;

/**
 * Created by wyw on 2016/11/16.
 */

public class MyApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Stetho.initializeWithDefaults(mContext);
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder().setDebug(true);
        OkHttpFinal.getInstance().init(builder.build());
    }
    public static Context getContext() {
        return mContext;
    }
}
