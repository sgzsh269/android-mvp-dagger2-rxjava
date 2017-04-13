package com.sagarnileshshah.carouselmvp.ui.photodetail;

import com.sagarnileshshah.carouselmvp.data.DataRepository;
import com.sagarnileshshah.carouselmvp.util.di.FragmentScope;
import com.sagarnileshshah.carouselmvp.util.threading.MainUiThread;
import com.sagarnileshshah.carouselmvp.util.threading.ThreadExecutor;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

@Module
public class PhotoDetailModule {

    PhotoDetailContract.View view;

    public PhotoDetailModule(PhotoDetailContract.View view) {
        this.view = view;
    }

    @Provides
    @FragmentScope
    public PhotoDetailContract.Presenter providePhotoDetailPresenter(PhotoDetailContract.View view,
            DataRepository dataRepository,
            ThreadExecutor threadExecutor, MainUiThread mainUiThread,
            CompositeSubscription compositeSubscription) {
        return new PhotoDetailPresenter(view, dataRepository, threadExecutor, mainUiThread,
                compositeSubscription);
    }

    @Provides
    @FragmentScope
    public PhotoDetailContract.View providePhotoDetailView() {
        return view;
    }

    @Provides
    @FragmentScope
    public CompositeSubscription provideCompositeSubcription() {
        return new CompositeSubscription();
    }
}
