package com.sagarnileshshah.carouselmvp.ui.mainactivity;

import android.content.BroadcastReceiver;

import com.sagarnileshshah.carouselmvp.ApplicationComponent;
import com.sagarnileshshah.carouselmvp.data.receivers.ReceiversModule;
import com.sagarnileshshah.carouselmvp.util.di.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = {ApplicationComponent.class},
        modules = {MainActivityModule.class, ReceiversModule.class}
        )
public interface MainActivityComponent {

    MainActivityContract.Presenter getMainActivityPresenter();

    void inject(MainActivity mainActivity);
}
