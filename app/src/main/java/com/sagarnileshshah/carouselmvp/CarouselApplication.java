package com.sagarnileshshah.carouselmvp;

import com.sagarnileshshah.carouselmvp.util.BaseApplication;

public class CarouselApplication extends BaseApplication {

    private ApplicationComponent applicationComponent;

    @Override
    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    @Override
    public void initApplicationComponent() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }
}
