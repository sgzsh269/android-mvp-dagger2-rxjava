package com.sagarnileshshah.carouselmvp.ui.mainactivity;


import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

import android.content.Context;
import android.content.IntentFilter;

import com.sagarnileshshah.carouselmvp.data.receivers.ConnectivityBroadcastReceiver;
import com.sagarnileshshah.carouselmvp.util.NetworkHelper;
import com.sagarnileshshah.carouselmvp.util.di.ActivityScope;

import dagger.Module;
import dagger.Provides;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

@Module
public class MainActivityModule {

    private MainActivity mainActivity;

    public MainActivityModule(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Provides
    @ActivityScope
    public MainActivityContract.Presenter provideMainActivityPresenter(
            MainActivityContract.View view,
            ConnectivityBroadcastReceiver connectivityBroadcastReceiver) {
        CompositeSubscription compositeSubscription = new CompositeSubscription();
        return new MainActivityPresenter(view, connectivityBroadcastReceiver,
                compositeSubscription);
    }

    @Provides
    @ActivityScope
    public MainActivityContract.View provideMainActivityView() {
        return mainActivity;
    }

    @Provides
    @ActivityScope
    @ActivityContext
    public Context provideContext() {
        return mainActivity;
    }

    @Provides
    @ActivityScope
    public ConnectivityBroadcastReceiver provideConnectivityBroadcastReceiver(
            @ActivityContext Context context,
            NetworkHelper networkHelper) {
        IntentFilter intentFilter = new IntentFilter(CONNECTIVITY_ACTION);
        PublishSubject<Boolean> publishSubject = PublishSubject.create();
        return new ConnectivityBroadcastReceiver(context, networkHelper, intentFilter,
                publishSubject);
    }
}
