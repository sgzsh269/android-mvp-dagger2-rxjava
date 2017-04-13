package com.sagarnileshshah.carouselmvp.data;

import android.content.Context;

import com.sagarnileshshah.carouselmvp.data.local.LocalDataSource;
import com.sagarnileshshah.carouselmvp.data.models.comment.Comment;
import com.sagarnileshshah.carouselmvp.data.models.photo.Photo;
import com.sagarnileshshah.carouselmvp.util.NetworkHelper;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * The primary class for the presenters that extend
 * {@link com.sagarnileshshah.carouselmvp.util.mvp.BasePresenter} to interact with
 * for fetching and storing data.
 * It is the middleman in front of all data sources such as
 * {@link com.sagarnileshshah.carouselmvp.data.remote.RemoteDataSource}
 * and {@link com.sagarnileshshah.carouselmvp.data.local.LocalDataSource} and delegates the work to
 * them.
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

    public Subscription getPhotos(Context context, int page,
            DataSource.Callback<List<Photo>> onSuccess,
            DataSource.Callback<Throwable> onError) {

        Observable<List<Photo>> observable;

        if (networkHelper.isNetworkAvailable(context)) {
            observable = remoteDataSource.getPhotos(page)
                    .doOnNext(photos -> ((LocalDataSource) localDataSource).storePhotos(photos));
        } else {
            observable = localDataSource.getPhotos(page);
        }

        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        photos -> onSuccess.call(photos),
                        throwable -> onError.call(throwable));
    }

    public Subscription getComments(Context context, final Photo photo,
            DataSource.Callback<List<Comment>> onSuccess,
            DataSource.Callback<Throwable> onError) {

        Observable<List<Comment>> observable;
        if (networkHelper.isNetworkAvailable(context)) {
            observable = remoteDataSource.getComments(photo.getId())
                    .doOnNext(comments ->
                            ((LocalDataSource) localDataSource).storeComments(photo, comments));

        } else {
            observable = localDataSource.getComments(photo.getId());
        }
        return observable.
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        comments -> onSuccess.call(comments),
                        throwable -> onError.call(throwable));
    }
}
