package com.sagarnileshshah.carouselmvp.util;


import android.app.Application;

import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.sagarnileshshah.carouselmvp.ApplicationComponent;
import com.sagarnileshshah.carouselmvp.BuildConfig;

public abstract class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
        initApplicationComponent();
    }

    public abstract ApplicationComponent getApplicationComponent();

    public abstract void initApplicationComponent();
}
