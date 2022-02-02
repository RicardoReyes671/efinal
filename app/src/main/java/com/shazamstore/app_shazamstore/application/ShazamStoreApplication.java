package com.shazamstore.app_shazamstore.application;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

public class ShazamStoreApplication extends Application {
    private static ShazamStoreApplication instance;
    private static Context appContext;

    public static ShazamStoreApplication getInstance() { return instance; }

    public static Context getAppContext() { return appContext; }

    public void setAppContext(Context mAppContext){ this.appContext = mAppContext; }

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;

        this.setAppContext(getApplicationContext());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
