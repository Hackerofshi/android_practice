package com.shixin.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import org.xutils.x;
//import android.support.multidex.MultiDex;

/**
 * APPLICATION
 */
public class BaseApplication extends Application {

    private static BaseApplication baseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        x.Ext.init(this);
    }

    public static Context getAppContext() {
        return baseApplication;
    }
    public static Resources getAppResources() {
        return baseApplication.getResources();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 分包
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
      //  MultiDex.install(this);
    }

}