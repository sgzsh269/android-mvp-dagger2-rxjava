package com.sagarnileshshah.carouselmvp.ui.photos;


import com.sagarnileshshah.carouselmvp.data.DataRepository;
import com.sagarnileshshah.carouselmvp.util.di.FragmentScope;
import com.sagarnileshshah.carouselmvp.util.threading.MainUiThread;
import com.sagarnileshshah.carouselmvp.util.threading.ThreadExecutor;

import dagger.Module;
import dagger.Provides;

@Module
public class PhotosModule {

    PhotosContract.View view;

    public PhotosModule(PhotosContract.View view) {
        this.view = view;
    }

    @Provides
    @FragmentScope
    public PhotosContract.Presenter providePhotosPresenter(PhotosContract.View view,
            DataRepository dataRepository,
            ThreadExecutor threadExecutor, MainUiThread mainUiThread) {
        return new PhotosPresenter(view, dataRepository, threadExecutor, mainUiThread);
    }

    @Provides
    @FragmentScope
    public PhotosContract.View providePhotosView() {
        return this.view;
    }
}
