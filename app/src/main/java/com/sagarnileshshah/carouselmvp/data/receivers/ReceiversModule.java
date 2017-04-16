package com.sagarnileshshah.carouselmvp.data.receivers;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

import android.content.Context;
import android.content.IntentFilter;

import com.sagarnileshshah.carouselmvp.ui.mainactivity.ActivityContext;
import com.sagarnileshshah.carouselmvp.util.NetworkHelper;
import com.sagarnileshshah.carouselmvp.util.di.ActivityScope;

import dagger.Module;
import dagger.Provides;
import rx.subjects.PublishSubject;

@Module
public class ReceiversModule {

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
