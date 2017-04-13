package com.sagarnileshshah.carouselmvp.ui.photodetail;

import android.content.Context;

import com.sagarnileshshah.carouselmvp.R;
import com.sagarnileshshah.carouselmvp.data.DataRepository;
import com.sagarnileshshah.carouselmvp.data.models.photo.Photo;
import com.sagarnileshshah.carouselmvp.util.mvp.BasePresenter;
import com.sagarnileshshah.carouselmvp.util.threading.MainUiThread;
import com.sagarnileshshah.carouselmvp.util.threading.ThreadExecutor;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * The Presenter that fetches comment data by calling {@link DataRepository} at the request of
 * its View "{@link PhotoDetailContract.View}", and then delivers the data back to
 * its View.
 * The presenter also calls other relevant methods of its View such as for
 * showing/hiding progress bar and for showing Toast messages.
 * The Presenter subscribes to its View lifecycle by allowing
 * the View to call the Presenter's {@link #onViewActive(Object)} and {@link #onViewInactive()}
 * to reference/unreference its View. This allows its View to be GCed and prevents memory leaks.
 */
public class PhotoDetailPresenter extends BasePresenter<PhotoDetailContract.View> implements
        PhotoDetailContract.Presenter {

    private DataRepository dataRepository;
    private ThreadExecutor threadExecutor;
    private MainUiThread mainUiThread;

    public PhotoDetailPresenter(PhotoDetailContract.View view, DataRepository dataRepository,
            ThreadExecutor threadExecutor, MainUiThread mainUiThread,
            CompositeSubscription compositeSubscription) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.threadExecutor = threadExecutor;
        this.mainUiThread = mainUiThread;
        this.compositeSubscription = compositeSubscription;
    }

    @Override
    public void getComments(final Context context, Photo photo) {
        view.setProgressBar(true);

        Subscription subscription = dataRepository.getComments(
                context,
                photo,
                comments -> {
                        view.showComments(comments);
                        view.setProgressBar(false);
                        view.shouldShowPlaceholderText();
                },
                throwable -> {
                        view.setProgressBar(false);
                        view.showToastMessage(context.getString(R.string.error_msg));
                        view.shouldShowPlaceholderText();
                });
        compositeSubscription.add(subscription);
    }
}
