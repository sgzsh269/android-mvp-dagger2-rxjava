package com.sagarnileshshah.carouselmvp.data;

import com.sagarnileshshah.carouselmvp.data.models.comment.Comment;
import com.sagarnileshshah.carouselmvp.data.models.photo.Photo;
import com.sagarnileshshah.carouselmvp.util.threading.MainUiThread;
import com.sagarnileshshah.carouselmvp.util.threading.ThreadExecutor;

import java.util.List;

import rx.Observable;

/**
 * The interface that exposes fetching and storing data through helper methods. This is to be
 * implemented by all data sources such as
 * {@link com.sagarnileshshah.carouselmvp.data.remote.RemoteDataSource} and
 * {@link com.sagarnileshshah.carouselmvp.data.local.LocalDataSource}
 */
public abstract class DataSource {

    protected MainUiThread mainUiThread;
    protected ThreadExecutor threadExecutor;

    public DataSource(MainUiThread mainUiThread, ThreadExecutor threadExecutor) {
        this.mainUiThread = mainUiThread;
        this.threadExecutor = threadExecutor;
    }

    public interface Callback<T> {
        void call(T t);
    }

    public abstract Observable<List<Photo>> getPhotos(int page);

    public abstract Observable<List<Comment>> getComments(String photoId);

}
