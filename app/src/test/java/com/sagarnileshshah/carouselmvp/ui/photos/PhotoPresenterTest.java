package com.sagarnileshshah.carouselmvp.ui.photos;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import com.sagarnileshshah.carouselmvp.R;
import com.sagarnileshshah.carouselmvp.data.DataRepository;
import com.sagarnileshshah.carouselmvp.data.DataSource;
import com.sagarnileshshah.carouselmvp.data.models.comment.Comment;
import com.sagarnileshshah.carouselmvp.data.models.photo.Photo;
import com.sagarnileshshah.carouselmvp.util.MiscHelper;
import com.sagarnileshshah.carouselmvp.util.threading.MainUiThread;
import com.sagarnileshshah.carouselmvp.util.threading.ThreadExecutor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

@RunWith(MockitoJUnitRunner.class)
public class PhotoPresenterTest {

    @Mock
    private PhotosContract.View mockView;

    @Mock
    private DataRepository mockDataRepository;

    @Mock
    private ThreadExecutor mockThreadExecutor;

    @Mock
    private MainUiThread mockMainUiThread;

    @Mock
    Context mockContext;

    @Mock
    Subscription mockSubscription;

    @Mock
    List<Photo> mockPhotos;

    @Mock
    MiscHelper miscHelper;

    @Captor
    private ArgumentCaptor<DataSource.Callback<List<Photo>>> onSuccessCaptor;

    @Captor
    private ArgumentCaptor<DataSource.Callback<Throwable>> onErrorCaptor;

    private PhotosContract.Presenter photosPresenter;
    private CompositeSubscription mockCompositeSubscription = new CompositeSubscription();

    @Before
    public void setup() {
        photosPresenter = new PhotosPresenter(mockView, mockDataRepository, mockThreadExecutor,
                mockMainUiThread, mockCompositeSubscription, miscHelper);
    }

    @Test
    public void getPhotos_testCallback() {
        String errorMsg = "ERROR";
        int page = 0;
        when(mockDataRepository.getPhotos(any(Context.class), eq(page),
                any(DataSource.Callback.class), any(DataSource.Callback.class)))
                .thenReturn(mockSubscription);
        when(mockContext.getString(R.string.error_msg)).thenReturn(errorMsg);

        photosPresenter.getPhotos(mockContext, page);

        verify(mockView).setProgressBar(true);
        verify(mockDataRepository).getPhotos(eq(mockContext), eq(page),
                onSuccessCaptor.capture(), onErrorCaptor.capture());

        onSuccessCaptor.getValue().call(mockPhotos);
        onErrorCaptor.getValue().call(any(Throwable.class));

        verify(mockView).showPhotos(mockPhotos);
        verify(mockView, times(2)).setProgressBar(false);
        verify(mockView, times(2)).shouldShowPlaceholderText();
        verify(mockContext).getString(R.string.error_msg);
        verify(mockView).showToastMessage(errorMsg);
    }


}
