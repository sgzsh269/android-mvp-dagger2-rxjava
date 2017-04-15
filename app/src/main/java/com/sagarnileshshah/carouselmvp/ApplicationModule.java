package com.sagarnileshshah.carouselmvp;


import com.sagarnileshshah.carouselmvp.util.BaseApplication;
import com.sagarnileshshah.carouselmvp.util.MiscHelper;
import com.sagarnileshshah.carouselmvp.util.di.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private BaseApplication baseApplication;

    public ApplicationModule(BaseApplication baseApplication) {
        this.baseApplication = baseApplication;
    }

    @Provides
    @ApplicationScope
    public BaseApplication provideApplication() {
        return baseApplication;
    }

    @Provides
    @ApplicationScope
    public MiscHelper provideMiscHelper() {
        return new MiscHelper();
    }
}
