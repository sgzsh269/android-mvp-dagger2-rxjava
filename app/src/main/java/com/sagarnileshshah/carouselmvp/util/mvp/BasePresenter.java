package com.sagarnileshshah.carouselmvp.util.mvp;


import rx.subscriptions.CompositeSubscription;

/**
 * Abstract base class to be extended by any MVP Presenter. It contains common attributes and
 * methods to be shared across Presenters.
 *
 * @param <ViewT> a generic type to indicate a type of MVP View
 */
public abstract class BasePresenter<ViewT> implements IBasePresenter<ViewT> {

    protected ViewT view;
    protected CompositeSubscription compositeSubscription;

    @Override
    public void onViewActive(ViewT view) {
        this.view = view;

    }

    @Override
    public void onViewInactive() {
        view = null;
        if (compositeSubscription != null) {
            compositeSubscription.clear();
        }
    }
}
