package com.sagarnileshshah.carouselmvp.ui.mainactivity;


import android.content.Context;

import com.sagarnileshshah.carouselmvp.data.receivers.ConnectivityBroadcastReceiver;
import com.sagarnileshshah.carouselmvp.util.mvp.BasePresenter;

import rx.Observable;
import rx.subscriptions.CompositeSubscription;

public class MainActivityPresenter implements MainActivityContract.Presenter {

    private ConnectivityBroadcastReceiver connectivityBroadcastReceiver;
    private CompositeSubscription compositeSubscription;
    private MainActivityContract.View view;

    public MainActivityPresenter(MainActivityContract.View view,
            ConnectivityBroadcastReceiver connectivityBroadcastReceiver,
            CompositeSubscription compositeSubscription) {
        this.view = view;
        this.connectivityBroadcastReceiver = connectivityBroadcastReceiver;
        this.compositeSubscription = compositeSubscription;
    }

    @Override
    public void subscribeEventStream() {
        Observable<Boolean> observable = connectivityBroadcastReceiver.registerReceiver();
        observable.subscribe(view::showOfflineMsg);
    }

    @Override
    public void unsubscribeEventStream() {
        compositeSubscription.clear();
        connectivityBroadcastReceiver.unregisterReceiver();
    }

    @Override
    public void onViewActive(MainActivityContract.View view) {
        this.view = view;
        subscribeEventStream();
    }

    @Override
    public void onViewInactive() {
        view = null;
        compositeSubscription.clear();
        unsubscribeEventStream();
    }
}
