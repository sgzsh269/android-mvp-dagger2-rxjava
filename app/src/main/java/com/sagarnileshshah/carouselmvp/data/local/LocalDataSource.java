package com.sagarnileshshah.carouselmvp.data.local;


import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.sagarnileshshah.carouselmvp.data.DataSource;
import com.sagarnileshshah.carouselmvp.data.models.comment.Comment;
import com.sagarnileshshah.carouselmvp.data.models.comment.Comment_Table;
import com.sagarnileshshah.carouselmvp.data.models.photo.Photo;
import com.sagarnileshshah.carouselmvp.util.threading.MainUiThread;
import com.sagarnileshshah.carouselmvp.util.threading.ThreadExecutor;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * The class for fetching from and storing data into a local SQLite DB on a background thread and
 * returning the relevant {@link Observable} for the data.
 */
public class LocalDataSource extends DataSource {

    private DatabaseDefinition databaseDefinition;

    public LocalDataSource(MainUiThread mainUiThread, ThreadExecutor threadExecutor,
            DatabaseDefinition databaseDefinition) {
        super(mainUiThread, threadExecutor);
        this.databaseDefinition = databaseDefinition;
    }

    private List<Photo> getPhotosFromDb(int page) {
        List<Photo> photos = SQLite.select()
                .from(Photo.class)
                .limit(10)
                .offset((page - 1) * 10)
                .queryList();
        return photos;
    }

    @Override
    public Observable<List<Photo>> getPhotos(int page) {
        return Observable.fromCallable(() -> getPhotosFromDb(page));

    }

    private List<Comment> getCommentsFromDb(String photoId) {
        List<Comment> comments = SQLite.select()
                .from(Comment.class)
                .where(Comment_Table.photo_id.eq(photoId))
                .queryList();
        return comments;
    }

    @Override
    public Observable<List<Comment>> getComments(String photoId) {
        return Observable.fromCallable(() -> getCommentsFromDb(photoId));
    }

    public void storePhotos(final List<Photo> photos) {

        Observable.fromCallable(() -> savePhotos(photos))
                .subscribeOn(Schedulers.io())
                .publish()
                .connect();
    }

    private boolean savePhotos(List<Photo> photos) {
        for (Photo photo : photos) {
            photo.save();
        }
        return true;
    }

    public void storeComments(final Photo photo, final List<Comment> comments) {

        Observable.fromCallable(() -> saveComments(photo, comments))
                .subscribeOn(Schedulers.io())
                .publish()
                .connect();
    }

    private boolean saveComments(Photo photo, List<Comment> comments) {
        for (Comment comment : comments) {
            comment.setPhoto(photo);
            comment.save();
        }
        return true;
    }
}
