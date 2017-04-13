package com.sagarnileshshah.carouselmvp.ui.photodetail;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import com.sagarnileshshah.carouselmvp.R;
import com.sagarnileshshah.carouselmvp.data.DataRepository;
import com.sagarnileshshah.carouselmvp.data.DataSource;
import com.sagarnileshshah.carouselmvp.data.models.comment.Comment;
import com.sagarnileshshah.carouselmvp.data.models.photo.Photo;
import com.sagarnileshshah.carouselmvp.util.threading.MainUiThread;
import com.sagarnileshshah.carouselmvp.util.threading.ThreadExecutor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class PhotoDetailPresenterTest {

    @Mock
    private PhotoDetailContract.View mockView;

    @Mock
    private DataRepository mockDataRepository;

    @Mock
    private ThreadExecutor mockThreadExecutor;

    @Mock
    private MainUiThread mockMainUiThread;

    @Mock
    private Context mockContext;

    @Mock
    private Photo mockPhoto;

    @Mock
    private List<Comment> mockComments;

    @Mock
    Subscription mockSubscription;

    @Captor
    private ArgumentCaptor<DataSource.Callback<List<Comment>>> onSuccessCaptor;

    @Captor
    private ArgumentCaptor<DataSource.Callback<Throwable>> onErrorCaptor;

    private PhotoDetailContract.Presenter photoDetailPresenter;
    private CompositeSubscription mockCompositeSubscription = new CompositeSubscription();

    @Before
    public void setup() {
        photoDetailPresenter = new PhotoDetailPresenter(mockView, mockDataRepository,
                mockThreadExecutor, mockMainUiThread, mockCompositeSubscription);
    }

    @Test
    public void getComments_testCallback() {
        String errorMsg = "ERROR";
        when(mockDataRepository.getComments(any(Context.class), any(Photo.class),
                any(DataSource.Callback.class), any(DataSource.Callback.class)))
                .thenReturn(mockSubscription);

        when(mockContext.getString(R.string.error_msg)).thenReturn(errorMsg);

        photoDetailPresenter.getComments(mockContext, mockPhoto);

        verify(mockView).setProgressBar(true);
        verify(mockDataRepository).getComments(eq(mockContext), eq(mockPhoto),
                onSuccessCaptor.capture(),
                onErrorCaptor.capture());

        onSuccessCaptor.getValue().call(mockComments);
        onErrorCaptor.getValue().call(new Throwable());

        verify(mockView).showComments(mockComments);
        verify(mockView, times(2)).setProgressBar(false);
        verify(mockView, times(2)).shouldShowPlaceholderText();
        verify(mockContext).getString(R.string.error_msg);
        verify(mockView).showToastMessage(errorMsg);
    }

}
