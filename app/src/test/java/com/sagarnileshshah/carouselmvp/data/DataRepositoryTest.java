package com.sagarnileshshah.carouselmvp.data;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import com.sagarnileshshah.carouselmvp.testutil.RxJavaTestRunner;
import com.sagarnileshshah.carouselmvp.data.local.LocalDataSource;
import com.sagarnileshshah.carouselmvp.data.models.photo.Photo;
import com.sagarnileshshah.carouselmvp.data.remote.RemoteDataSource;
import com.sagarnileshshah.carouselmvp.util.NetworkHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.schedulers.Schedulers;

@RunWith(RxJavaTestRunner.class)
public class DataRepositoryTest {

    @Mock
    LocalDataSource mockLocalDataSource;

    @Mock
    RemoteDataSource mockRemoteDataSource;

    @Mock
    NetworkHelper mockNetworkHelper;

    @Mock
    Context mockContext;

    @Mock
    DataSource.Callback<List<Photo>> mockOnSuccess;

    @Mock
    DataSource.Callback<Throwable> mockOnError;

    @Mock
    List<Photo> mockPhotos;

    DataRepository dataRepository;

    @Before
    public void setup() {
        dataRepository = new DataRepository(mockRemoteDataSource, mockLocalDataSource,
                mockNetworkHelper);
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
    }

    @Test
    public void getPhotos_shouldGetFromRemoteDataSourceAndStoreLocally() {
        when(mockNetworkHelper.isNetworkAvailable(mockContext)).thenReturn(true);
        when(mockRemoteDataSource.getPhotos(anyInt())).thenReturn(Observable.just(mockPhotos));

        dataRepository.getPhotos(mockContext, anyInt(), mockOnSuccess, mockOnError);

        verify(mockRemoteDataSource).getPhotos(anyInt());
        verify(mockLocalDataSource).storePhotos(mockPhotos);
        verify(mockOnSuccess).call(mockPhotos);
        verify(mockOnError, never()).call(any(Throwable.class));
    }

    @Test
    public void getPhotos_shouldGetFromLocalDataSource() {
        when(mockNetworkHelper.isNetworkAvailable(mockContext)).thenReturn(false);
        when(mockLocalDataSource.getPhotos(anyInt())).thenReturn(Observable.just(mockPhotos));

        dataRepository.getPhotos(mockContext, anyInt(), mockOnSuccess, mockOnError);

        verify(mockRemoteDataSource, never()).getPhotos(anyInt());
        verify(mockLocalDataSource, never()).storePhotos(mockPhotos);
        verify(mockLocalDataSource).getPhotos(anyInt());
        verify(mockOnSuccess).call(mockPhotos);
        verify(mockOnError, never()).call(any(Throwable.class));
    }

}
