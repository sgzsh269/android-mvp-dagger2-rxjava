package com.sagarnileshshah.carouselmvp.data.remote;


import com.sagarnileshshah.carouselmvp.BuildConfig;
import com.sagarnileshshah.carouselmvp.data.DataSource;
import com.sagarnileshshah.carouselmvp.data.models.comment.Comment;
import com.sagarnileshshah.carouselmvp.data.models.photo.Photo;
import com.sagarnileshshah.carouselmvp.util.threading.MainUiThread;
import com.sagarnileshshah.carouselmvp.util.threading.ThreadExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * The class for fetching data from Flickr API on a background thread and returning the relevant
 * {@link Observable} for the data.
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
    public Observable<List<Photo>> getPhotos(int page) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put(QUERY_PARAM_METHOD, PHOTOS_ENDPOINT);
        queryMap.put(QUERY_PARAM_PER_PAGE, QUERY_PARAM_VALUE_PER_PAGE);
        queryMap.put(QUERY_PARAM_PAGE, String.valueOf(page));

        return apiService.getPhotos(queryMap)
                .flatMap(response -> Observable.just(response.getPhotos().getPhoto()));
    }

    @Override
    public Observable<List<Comment>> getComments(String photoId) {

        Map<String, String> queryMap = new HashMap<>();
        queryMap.put(QUERY_PARAM_METHOD, COMMENTS_ENDPOINT);

        return apiService.getComments(photoId, queryMap)
                .flatMap(response -> Observable.just(response.body().getComments().getComment()));
    }
}
