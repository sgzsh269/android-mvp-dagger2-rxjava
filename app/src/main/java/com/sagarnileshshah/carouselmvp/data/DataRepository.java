package com.sagarnileshshah.carouselmvp.data;

import android.content.Context;

import com.sagarnileshshah.carouselmvp.data.local.LocalDataSource;
import com.sagarnileshshah.carouselmvp.data.models.comment.Comment;
import com.sagarnileshshah.carouselmvp.data.models.photo.Photo;
import com.sagarnileshshah.carouselmvp.util.NetworkHelper;

import java.util.List;

/**
 * The primary class for the presenters that extend
 * {@link com.sagarnileshshah.carouselmvp.util.mvp.BasePresenter} to interact with
 * for fetching and storing data.
 * It is the middleman in front of all data sources such as
 * {@link com.sagarnileshshah.carouselmvp.data.remote.RemoteDataSource}
 * and {@link com.sagarnileshshah.carouselmvp.data.local.LocalDataSource} and delegates the work to
 * them depending on conditions such as network availability, etc.
 */
public class DataRepository {

    private DataSource remoteDataSource;
    private DataSource localDataSource;
    private NetworkHelper networkHelper;

    public DataRepository(DataSource remoteDataSource, DataSource localDataSource,
            NetworkHelper networkHelper) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
        this.networkHelper = networkHelper;
    }

    public void getPhotos(Context context, int page, DataSource.Callback<List<Photo>> onSuccess,
            DataSource.Callback<Throwable> onError) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.getPhotos(
                    page,
                    photos -> {
                        onSuccess.call(photos);
                        ((LocalDataSource) localDataSource).storePhotos(photos);

                    },
                    throwable -> onError.call(throwable)
            );
        } else {
            localDataSource.getPhotos(page, onSuccess, onError);
        }
    }

    public void getComments(Context context, final Photo photo,
            final DataSource.GetCommentsCallback callback) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.getComments(photo.getId(), new DataSource.GetCommentsCallback() {
                @Override
                public void onSuccess(List<Comment> comments) {
                    callback.onSuccess(comments);
                    ((LocalDataSource) localDataSource).storeComments(photo, comments);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void onNetworkFailure() {
                    callback.onNetworkFailure();
                }
            });
        } else {
            localDataSource.getComments(photo.getId(), callback);
        }
    }
}
