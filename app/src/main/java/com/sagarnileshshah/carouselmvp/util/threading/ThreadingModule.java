package com.sagarnileshshah.carouselmvp.util.threading;

import com.sagarnileshshah.carouselmvp.util.di.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ThreadingModule {

    @Provides
    @ApplicationScope
    public ThreadExecutor provideThreadExecutor() {
        return new ThreadExecutor();
    }

    @Provides
    @ApplicationScope
    public MainUiThread provideMainUiThread() {
        return new MainUiThread();
    }
}
