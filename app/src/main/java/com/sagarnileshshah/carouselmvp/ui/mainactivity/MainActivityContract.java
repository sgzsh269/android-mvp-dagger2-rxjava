package com.sagarnileshshah.carouselmvp.ui.mainactivity;


import com.sagarnileshshah.carouselmvp.util.mvp.IBasePresenter;
import com.sagarnileshshah.carouselmvp.util.mvp.IBaseView;

public interface MainActivityContract {

    interface View {
        void showOfflineMsg(boolean networkAvailable);
    }

    interface Presenter extends IBasePresenter<View> {
        void subscribeEventStream();

        void unsubscribeEventStream();
    }
}
