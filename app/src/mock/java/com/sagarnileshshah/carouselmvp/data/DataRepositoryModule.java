package com.sagarnileshshah.carouselmvp.data;

import com.sagarnileshshah.carouselmvp.data.local.Local;
import com.sagarnileshshah.carouselmvp.data.local.LocalDataSource;
import com.sagarnileshshah.carouselmvp.data.local.LocalDataSourceModule;
import com.sagarnileshshah.carouselmvp.data.remote.Remote;
import com.sagarnileshshah.carouselmvp.data.remote.RemoteDataSource;
import com.sagarnileshshah.carouselmvp.data.remote.RemoteDataSourceModule;
import com.sagarnileshshah.carouselmvp.util.NetworkHelper;
import com.sagarnileshshah.carouselmvp.util.di.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module(includes = {LocalDataSourceModule.class, RemoteDataSourceModule.class})
public class DataRepositoryModule {

    @Provides
    @ApplicationScope
    public DataRepository provideDataRepository(@Remote DataSource remoteDataSource,
            @Local DataSource localDataSource, NetworkHelper networkHelper) {
        return new DataRepository(remoteDataSource, localDataSource, networkHelper);
    }

    @Provides
    @ApplicationScope
    public NetworkHelper provideNetworkHelper() {
        return new NetworkHelper();
    }

}
