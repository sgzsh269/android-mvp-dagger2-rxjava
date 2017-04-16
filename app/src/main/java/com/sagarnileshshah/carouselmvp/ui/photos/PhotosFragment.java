package com.sagarnileshshah.carouselmvp.ui.photos;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sagarnileshshah.carouselmvp.R;
import com.sagarnileshshah.carouselmvp.data.models.photo.Photo;
import com.sagarnileshshah.carouselmvp.ui.photodetail.PhotoDetailFragment;
import com.sagarnileshshah.carouselmvp.util.BaseFragmentInteractionListener;
import com.sagarnileshshah.carouselmvp.util.EndlessRecyclerViewScrollListener;
import com.sagarnileshshah.carouselmvp.util.ItemClickSupport;
import com.sagarnileshshah.carouselmvp.util.Properties;
import com.sagarnileshshah.carouselmvp.util.mvp.BaseView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The {@link Fragment} that receives photo data from its {@link PhotosContract.Presenter} and
 * renders a list of photos and also handles user actions, such as clicks on photos,
 * and passes it to its {@link PhotosContract.Presenter}.
 */
public class PhotosFragment extends BaseView implements PhotosContract.View {

    @BindView(R.id.rvPhotos)
    RecyclerView rvPhotos;

    @BindView(R.id.tvPlaceholder)
    TextView tvPlaceholder;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;


    public static final int STARTING_PAGE_INDEX = 1;

    private PhotosRecyclerAdapter recyclerAdapter;
    private List<Photo> photos;
    private EndlessRecyclerViewScrollListener endlessScrollListener;
    private BaseFragmentInteractionListener fragmentInteractionListener;
    private boolean shouldRefreshPhotos;

    @Inject
    PhotosContract.Presenter presenter;

    private PhotosComponent photosComponent;

    /**
     * To track if the fragment has been newly created. This can be used to manage operations that
     * should only be run once when fragment is created.
     * This check originated because of the situation that when a Fragment is displayed through
     * {@link android.support.v4.app.FragmentTransaction#replace(int, Fragment)} and popped from the
     * backstack, the {@link Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)} method and
     * subsequent lifecycle methods are called again
     */
    private boolean isCreatedOnce;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentInteractionListener = (BaseFragmentInteractionListener) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMemberVariables();
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        initDepedencyInjection();
        View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Only get data once and not on fragment restore such as on config change
        if (savedInstanceState == null) {
            initData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initDepedencyInjection();
        presenter.onViewActive(this);
        fragmentInteractionListener.resetToolBarScroll();
    }

    @Override
    public void onPause() {
        releaseDependecyInjection();
        presenter.onViewInactive();
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void showPhotos(List<Photo> photos) {
        if (shouldRefreshPhotos) {
            recyclerAdapter.clear();
            endlessScrollListener.resetState();
            shouldRefreshPhotos = false;
        }
        recyclerAdapter.addAll(photos);
    }

    @Override
    public void shouldShowPlaceholderText() {
        if (photos.isEmpty()) {
            tvPlaceholder.setVisibility(View.VISIBLE);
        } else {
            tvPlaceholder.setVisibility(View.GONE);
        }
    }


    private void getPhotos(int page) {
        presenter.getPhotos(getContext().getApplicationContext(), page);
    }

    private void refreshPhotos() {
        shouldRefreshPhotos = true;
        getPhotos(STARTING_PAGE_INDEX);
    }

    @Override
    public void setProgressBar(boolean show) {
        swipeRefreshLayout.setRefreshing(show);
    }

    private void showDetailFragment(int photoPosition) {
        Photo photo = photos.get(photoPosition);
        Parcelable parcelable = Parcels.wrap(photo);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Properties.BUNDLE_KEY_PHOTO, parcelable);
        fragmentInteractionListener.showFragment(PhotoDetailFragment.class, bundle,
                true);
    }


    private void initMemberVariables() {
        photos = new ArrayList<>();
    }

    private void initDepedencyInjection() {
        photosComponent = DaggerPhotosComponent.builder()
                .applicationComponent(fragmentInteractionListener.getApplicationComponent())
                .photosModule(new PhotosModule(this))
                .build();

        photosComponent.inject(this);
    }

    private void initViews(View rootView) {
        ButterKnife.bind(this, rootView);
        recyclerAdapter = new PhotosRecyclerAdapter(this, presenter, photos);
        rvPhotos.setAdapter(recyclerAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPhotos.setLayoutManager(linearLayoutManager);

        endlessScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager,
                STARTING_PAGE_INDEX) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getPhotos(page);
            }
        };

        rvPhotos.addOnScrollListener(endlessScrollListener);

        ItemClickSupport.addTo(rvPhotos).setOnItemClickListener(
                (recyclerView, position, v) -> showDetailFragment(position));

        swipeRefreshLayout.setOnRefreshListener(() -> refreshPhotos());

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary);
    }

    private void initData() {
        if (!isCreatedOnce) {
            isCreatedOnce = true;
            refreshPhotos();
        }
    }


    private void releaseDependecyInjection() {
        photosComponent = null;
    }

}