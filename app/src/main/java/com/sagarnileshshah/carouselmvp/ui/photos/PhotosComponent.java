package com.sagarnileshshah.carouselmvp.ui.photos;

import com.sagarnileshshah.carouselmvp.ApplicationComponent;
import com.sagarnileshshah.carouselmvp.util.di.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(
        dependencies = {ApplicationComponent.class},
        modules = {PhotosModule.class}
        )
public interface PhotosComponent {

    PhotosContract.Presenter getPhotosPresenter();

    void inject(PhotosFragment photosFragment);
}
