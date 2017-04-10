package com.sagarnileshshah.carouselmvp.data.remote;


import static com.sagarnileshshah.carouselmvp.data.remote.RemoteDataSource.API_KEY;
import static com.sagarnileshshah.carouselmvp.data.remote.RemoteDataSource.BASE_API_URL;
import static com.sagarnileshshah.carouselmvp.data.remote.RemoteDataSource.QUERY_PARAM_API_KEY;
import static com.sagarnileshshah.carouselmvp.data.remote.RemoteDataSource.QUERY_PARAM_FORMAT;
import static com.sagarnileshshah.carouselmvp.data.remote.RemoteDataSource
        .QUERY_PARAM_NO_JSON_CALLBACK;
import static com.sagarnileshshah.carouselmvp.data.remote.RemoteDataSource.QUERY_PARAM_VALUE_JSON;
import static com.sagarnileshshah.carouselmvp.data.remote.RemoteDataSource
        .QUERY_PARAM_VALUE_NO_JSON_CALLBACK;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.sagarnileshshah.carouselmvp.data.DataSource;
import com.sagarnileshshah.carouselmvp.util.di.ApplicationScope;
import com.sagarnileshshah.carouselmvp.util.threading.MainUiThread;
import com.sagarnileshshah.carouselmvp.util.threading.ThreadExecutor;

import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RemoteDataSourceModule {

    @Provides
    @ApplicationScope
    @Remote
    public DataSource provideRemoteDataSource(MainUiThread mainUiThread,
            ThreadExecutor threadExecutor, ApiService apiService) {
        return new RemoteDataSource(mainUiThread, threadExecutor, apiService);
    }

    @Provides
    @ApplicationScope
    public ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    @Provides
    @ApplicationScope
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    @Provides
    @ApplicationScope
    public OkHttpClient provideOkHttpClient(Interceptor interceptor,
            StethoInterceptor stethoInterceptor) {
        OkHttpClient okHttpClient =
                new OkHttpClient.Builder()
                        .addNetworkInterceptor(stethoInterceptor)
                        .addInterceptor(interceptor)
                        .build();
        return okHttpClient;
    }

    @Provides
    @ApplicationScope
    public StethoInterceptor provideStethoInterceptor() {
        return new StethoInterceptor();
    }

    @Provides
    @ApplicationScope
    public Interceptor provideOkHttpInterceptor() {
        return interceptor;
    }

    private Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            HttpUrl httpUrl = chain.request().url().newBuilder()
                    .addQueryParameter(QUERY_PARAM_API_KEY, API_KEY)
                    .addQueryParameter(QUERY_PARAM_NO_JSON_CALLBACK,
                            QUERY_PARAM_VALUE_NO_JSON_CALLBACK)
                    .addQueryParameter(QUERY_PARAM_FORMAT, QUERY_PARAM_VALUE_JSON)
                    .build();
            Request newRequest = chain.request().newBuilder().url(httpUrl).build();
            return chain.proceed(newRequest);
        }
    };

}
