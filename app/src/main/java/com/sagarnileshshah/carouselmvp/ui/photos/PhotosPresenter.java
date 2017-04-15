package com.sagarnileshshah.carouselmvp.ui.photos;

import android.content.Context;

import com.sagarnileshshah.carouselmvp.R;
import com.sagarnileshshah.carouselmvp.data.DataRepository;
import com.sagarnileshshah.carouselmvp.data.models.photo.Photo;
import com.sagarnileshshah.carouselmvp.util.MiscHelper;
import com.sagarnileshshah.carouselmvp.util.mvp.BasePresenter;
import com.sagarnileshshah.carouselmvp.util.threading.MainUiThread;
import com.sagarnileshshah.carouselmvp.util.threading.ThreadExecutor;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * The Presenter that fetches photo data by calling {@link DataRepository} at the request of
 * its View "{@link PhotosContract.View}", and then delivers the data back to
 * its View.
 * The presenter also calls other relevant methods of its View such as for
 * showing/hiding progress bar and for showing Toast messages.
 * The Presenter subscribes to its View lifecycle by allowing
 * the View to call the Presenter's {@link #onViewActive(Object)} and {@link #onViewInactive()}
 * to reference/unreference its View. This allows its View to be GCed and prevents memory leaks.
 * The Presenter uses
 */
public class PhotosPresenter extends BasePresenter<PhotosContract.View> implements
        PhotosContract.Presenter {

    private DataRepository dataRepository;
    private ThreadExecutor threadExecutor;
    private MainUiThread mainUiThread;
    private MiscHelper miscHelper;

    public PhotosPresenter(PhotosContract.View view, DataRepository dataRepository,
            ThreadExecutor threadExecutor, MainUiThread mainUiThread,
            CompositeSubscription compositeSubscription, MiscHelper miscHelper) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.threadExecutor = threadExecutor;
        this.mainUiThread = mainUiThread;
        this.compositeSubscription = compositeSubscription;
        this.miscHelper = miscHelper;
    }

    @Override
    public void getPhotos(final Context context, int page) {
        view.setProgressBar(true);
        Subscription subscription = dataRepository.getPhotos(
                context,
                page,
                photos -> {
                        view.showPhotos(photos);
                        view.setProgressBar(false);
                        view.shouldShowPlaceholderText();
                },
                throwable -> {
                        view.setProgressBar(false);
                        view.showToastMessage(context.getString(R.string.error_msg));
                        view.shouldShowPlaceholderText();
                }
        );
        compositeSubscription.add(subscription);
    }

    @Override
    public String getPhotoUrl(Photo photo) {
        return miscHelper.getPhotoUrl(photo);
    }
}
