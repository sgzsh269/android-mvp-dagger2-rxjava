package com.sagarnileshshah.carouselmvp;

import com.sagarnileshshah.carouselmvp.data.DataRepository;
import com.sagarnileshshah.carouselmvp.data.DataRepositoryModule;
import com.sagarnileshshah.carouselmvp.ui.MainActivity;
import com.sagarnileshshah.carouselmvp.util.BaseApplication;
import com.sagarnileshshah.carouselmvp.util.FoaBaseActivity;
import com.sagarnileshshah.carouselmvp.util.NetworkHelper;
import com.sagarnileshshah.carouselmvp.util.di.ApplicationScope;
import com.sagarnileshshah.carouselmvp.util.threading.MainUiThread;
import com.sagarnileshshah.carouselmvp.util.threading.ThreadExecutor;
import com.sagarnileshshah.carouselmvp.util.threading.ThreadingModule;

import dagger.Component;

@ApplicationScope
@Component(modules = {ApplicationModule.class, ThreadingModule.class, DataRepositoryModule.class})
public interface ApplicationComponent {

    BaseApplication getBaseApplication();
    ThreadExecutor getThreadExecutor();
    MainUiThread getMainUiThread();
    DataRepository getDataRepository();
    NetworkHelper getNetworkHelper();

    void inject(FoaBaseActivity foaBaseActivity);
}
