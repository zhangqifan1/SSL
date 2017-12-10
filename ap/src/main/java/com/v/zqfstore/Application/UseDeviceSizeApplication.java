package com.v.zqfstore.Application;

import android.app.Application;

import com.zhy.autolayout.config.AutoLayoutConifg;

public class UseDeviceSizeApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        AutoLayoutConifg.getInstance().useDeviceSize();
    }
}