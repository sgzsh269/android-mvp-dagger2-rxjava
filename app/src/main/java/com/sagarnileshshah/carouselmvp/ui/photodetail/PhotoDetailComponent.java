package com.sagarnileshshah.carouselmvp.ui.photodetail;

import com.sagarnileshshah.carouselmvp.ApplicationComponent;
import com.sagarnileshshah.carouselmvp.util.di.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(
        dependencies = {ApplicationComponent.class},
        modules = {PhotoDetailModule.class}
)
public interface PhotoDetailComponent {

    PhotoDetailContract.Presenter getPhotoDetailPresenter();

    void inject(PhotoDetailFragment photoDetailFragment);
}
