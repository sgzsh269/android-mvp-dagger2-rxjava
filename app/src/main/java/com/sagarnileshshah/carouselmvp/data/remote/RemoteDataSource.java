package com.sagarnileshshah.carouselmvp.data.remote;


import com.sagarnileshshah.carouselmvp.BuildConfig;
import com.sagarnileshshah.carouselmvp.data.DataSource;
import com.sagarnileshshah.carouselmvp.data.models.comment.Comment;
import com.sagarnileshshah.carouselmvp.data.models.photo.Photo;
import com.sagarnileshshah.carouselmvp.data.models.photo.Response;
import com.sagarnileshshah.carouselmvp.util.threading.MainUiThread;
import com.sagarnileshshah.carouselmvp.util.threading.ThreadExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * The class for fetching data from Flickr API on a background thread and returning data via
 * callbacks on the main UI thread
 */
public class RemoteDataSource extends DataSource {

    public static final String API_KEY = BuildConfig.FLICKR_API_KEY;
    public static final String BASE_API_URL = "https://api.flickr.com";
    public static final String PHOTOS_ENDPOINT = "flickr.interestingness.getList";
    public static final String COMMENTS_ENDPOINT = "flickr.photos.comments.getList";

    public static final String QUERY_PARAM_PHOTO_ID = "photo_id";
    public static final String QUERY_PARAM_API_KEY = "api_key";
    public static final String QUERY_PARAM_METHOD = "method";
    public static final String QUERY_PARAM_NO_JSON_CALLBACK = "nojsoncallback";
    public static final String QUERY_PARAM_FORMAT = "format";
    public static final String QUERY_PARAM_VALUE_JSON = "json";
    public static final String QUERY_PARAM_VALUE_NO_JSON_CALLBACK = "1";
    public static final String QUERY_PARAM_PER_PAGE = "per_page";
    public static final String QUERY_PARAM_VALUE_PER_PAGE = "10";
    public static final String QUERY_PARAM_PAGE = "page";

    private ApiService apiService;

    public RemoteDataSource(MainUiThread mainUiThread,
            ThreadExecutor threadExecutor, ApiService apiService) {
        super(mainUiThread, threadExecutor);
        this.apiService = apiService;

    }

    @Override
    public void getPhotos(int page, Callback<List<Photo>> onSuccess,
            Callback<Throwable> onError) {

        Map<String, String> queryMap = new HashMap<>();
        queryMap.put(QUERY_PARAM_METHOD, PHOTOS_ENDPOINT);
        queryMap.put(QUERY_PARAM_PER_PAGE, QUERY_PARAM_VALUE_PER_PAGE);
        queryMap.put(QUERY_PARAM_PAGE, String.valueOf(page));

        Observable<retrofit2.Response<Response>> call =
                apiService.getPhotos(queryMap);

        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.isSuccessful()) {
                                Response photoResponse = response.body();
                                onSuccess.call(photoResponse.getPhotos().getPhoto());
                            } else {
                                onError.call(new Throwable());
                            }
                        },
                        throwable -> onError.call(throwable));

    }

    @Override
    public void getComments(String photoId, Callback<List<Comment>> onSuccess,
            Callback<Throwable> onError) {

        Map<String, String> queryMap = new HashMap<>();
        queryMap.put(QUERY_PARAM_METHOD, COMMENTS_ENDPOINT);

        Observable<retrofit2.Response<com.sagarnileshshah.carouselmvp.data.models.
                comment.Response>> call = apiService.getComments(photoId, queryMap);

        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.isSuccessful()) {
                                com.sagarnileshshah.carouselmvp.data.models.comment.Response
                                        commentsResponse = response.body();
                                onSuccess.call(commentsResponse.getComments().getComment());
                            } else {
                                onError.call(new Throwable());
                            }
                        },
                        throwable -> onError.call(throwable));
    }
}
