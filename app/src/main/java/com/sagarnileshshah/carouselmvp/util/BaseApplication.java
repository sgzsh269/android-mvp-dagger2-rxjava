package com.sagarnileshshah.carouselmvp.util;


import android.app.Application;

import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.sagarnileshshah.carouselmvp.ApplicationComponent;
import com.sagarnileshshah.carouselmvp.BuildConfig;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

public abstract class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        FlowManager.init(new FlowConfig.Builder(this).build());
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
        initApplicationComponent();
    }

    public abstract ApplicationComponent getApplicationComponent();

    public abstract void initApplicationComponent();
}
