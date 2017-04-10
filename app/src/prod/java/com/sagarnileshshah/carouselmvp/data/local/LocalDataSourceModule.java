package com.sagarnileshshah.carouselmvp.data.local;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.sagarnileshshah.carouselmvp.util.di.ApplicationScope;
import com.sagarnileshshah.carouselmvp.util.threading.MainUiThread;
import com.sagarnileshshah.carouselmvp.util.threading.ThreadExecutor;

import dagger.Module;
import dagger.Provides;

@Module
public class LocalDataSourceModule {

    @Provides
    @ApplicationScope
    public LocalDataSource provideLocalDataSource(MainUiThread mainUiThread,
            ThreadExecutor threadExecutor, DatabaseDefinition databaseDefinition) {
        return new LocalDataSource(mainUiThread, threadExecutor, databaseDefinition);
    }

    @Provides
    @ApplicationScope
    public DatabaseDefinition provideDatabaseDefinition(){
        return FlowManager.getDatabase(LocalDatabase.class);
    }
}
