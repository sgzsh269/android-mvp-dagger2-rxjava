package com.sagarnileshshah.carouselmvp.ui.mainactivity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.sagarnileshshah.carouselmvp.R;
import com.sagarnileshshah.carouselmvp.ui.photos.PhotosFragment;
import com.sagarnileshshah.carouselmvp.util.FoaBaseActivity;

import io.fabric.sdk.android.Fabric;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The container responsible for showing and destroying relevant {@link Fragment}, handling
 * back and up navigation using the Fragment back stack and maintaining global state
 * and event subscriptions. This is based on the Fragment Oriented Architecture explained here
 * http://vinsol.com/blog/2014/09/15/advocating-fragment-oriented-applications-in-android/
 */
public class MainActivity extends FoaBaseActivity implements MainActivityContract.View {

    @BindView(R.id.fragmentPlaceHolder)
    FrameLayout fragmentPlaceholder;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tvOfflineMode)
    TextView tvOfflineMode;

    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    @Inject
    MainActivityContract.Presenter mainActivityPresenter;

    private MainActivityComponent mainActivityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        initDependencyInjection();
        if (savedInstanceState == null) {
            initViews();
        } else {
            showRetainedFragment();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDependencyInjection();
        registerEventListeners();
    }

    @Override
    protected void onPause() {
        unregisterEventListeners();
        releaseDependencyInjection();
        super.onPause();
    }


    protected void initDependencyInjection() {
        super.initDependencyInjection();
        if (mainActivityComponent == null) {
            mainActivityComponent = DaggerMainActivityComponent.builder()
                    .applicationComponent(applicationComponent)
                    .mainActivityModule(new MainActivityModule(this))
                    .build();

            mainActivityComponent.inject(this);
        }
    }

    private void initViews() {
        showFragment(PhotosFragment.class);
    }

    private void releaseDependencyInjection() {
        mainActivityComponent = null;
    }


    private void registerEventListeners() {
        mainActivityPresenter.subscribeEventStream();
    }

    private void unregisterEventListeners() {
        mainActivityPresenter.unsubscribeEventStream();
    }


    @Override
    public void resetToolBarScroll() {
        appBarLayout.setExpanded(true, true);
    }

    @Override
    public void showOfflineMsg(boolean networkAvailable) {
        if (networkAvailable) {
            tvOfflineMode.setVisibility(View.GONE);
        } else {
            tvOfflineMode.setVisibility(View.VISIBLE);
        }
    }
}
