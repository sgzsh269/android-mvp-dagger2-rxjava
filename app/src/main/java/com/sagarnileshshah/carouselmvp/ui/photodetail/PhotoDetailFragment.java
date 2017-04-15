package com.sagarnileshshah.carouselmvp.ui.photodetail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sagarnileshshah.carouselmvp.R;
import com.sagarnileshshah.carouselmvp.data.models.comment.Comment;
import com.sagarnileshshah.carouselmvp.data.models.photo.Photo;
import com.sagarnileshshah.carouselmvp.util.BaseFragmentInteractionListener;
import com.sagarnileshshah.carouselmvp.util.EndlessRecyclerViewScrollListener;
import com.sagarnileshshah.carouselmvp.util.Properties;
import com.sagarnileshshah.carouselmvp.util.mvp.BaseView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The {@link Fragment} that receives photo data from
 * {@link com.sagarnileshshah.carouselmvp.ui.photos.PhotosFragment}
 * via a {@link Bundle} and comment data from its
 * {@link PhotoDetailContract.Presenter}. It then renders the photo and its list of comments.
 */
public class PhotoDetailFragment extends BaseView implements PhotoDetailContract.View {

    @BindView(R.id.rvComments)
    RecyclerView rvComments;

    @Inject
    PhotoDetailContract.Presenter presenter;

    private PhotoDetailRecyclerAdapter recyclerAdapter;
    private EndlessRecyclerViewScrollListener endlessScrollListener;
    private Photo photo;
    private List<Comment> comments;
    private BaseFragmentInteractionListener fragmentInteractionListener;
    private PhotoDetailComponent photoDetailComponent;


    public PhotoDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            photo = Parcels.unwrap(getArguments().getParcelable(Properties.BUNDLE_KEY_PHOTO));
        }
        comments = new ArrayList<>();
        setRetainInstance(true);
        initPhotoDetailComponent();
    }

    public void initPhotoDetailComponent() {
        photoDetailComponent = DaggerPhotoDetailComponent.builder()
                .applicationComponent(fragmentInteractionListener.getApplicationComponent())
                .photoDetailModule(new PhotoDetailModule(this))
                .build();

        photoDetailComponent.inject(this);
    }

    private void releasePhotoDetailComponent() {
        photoDetailComponent = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerAdapter = new PhotoDetailRecyclerAdapter(this, presenter, photo, comments);
        rvComments.setAdapter(recyclerAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvComments.setLayoutManager(linearLayoutManager);
        rvComments.setNestedScrollingEnabled(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentInteractionListener = (BaseFragmentInteractionListener) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        releasePhotoDetailComponent();
        presenter.onViewInactive();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        initPhotoDetailComponent();
        presenter.onViewActive(this);
        fragmentInteractionListener.resetToolBarScroll();
        presenter.getComments(getContext().getApplicationContext(), photo);
    }

    @Override
    public void showComments(List<Comment> comments) {
        if (comments != null) {
            recyclerAdapter.addAll(comments);
        }
    }

    @Override
    public void shouldShowPlaceholderText() {
        if (comments.isEmpty()) {

            /**
             * Placeholder text will always be 2nd (index: 1) in the recyclerview list, below
             * photo which is always at the top
             */
            recyclerAdapter.notifyItemChanged(1);
        }
    }

    @Override
    public void setProgressBar(boolean show) {
        recyclerAdapter.setProgressBar(show);

        /**
         * Progress bar will be either at the end of the comments list (index: comments.size()),
         * this it to support endless scrolling or if the comments list is empty then it will
         * be 2nd (index: 1), below photo which is always at the top
         */
        int position = comments.isEmpty() ? 1 : comments.size();
        recyclerAdapter.notifyItemChanged(position);
    }
}
