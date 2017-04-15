package com.sagarnileshshah.carouselmvp.ui.photos;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sagarnileshshah.carouselmvp.R;
import com.sagarnileshshah.carouselmvp.data.models.photo.Photo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The {@link android.support.v7.widget.RecyclerView.Adapter} that renders and populates each photo
 * in the photos list.
 */
public class PhotosRecyclerAdapter extends RecyclerView.Adapter<PhotosRecyclerAdapter.ViewHolder> {

    private List<Photo> photos;
    private Fragment fragment;
    private PhotosContract.Presenter presenter;

    public PhotosRecyclerAdapter(Fragment fragment, PhotosContract.Presenter presenter,
            List<Photo> photos) {
        this.fragment = fragment;
        this.presenter = presenter;
        this.photos = photos;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.ivPhoto)
        ImageView ivPhoto;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public PhotosRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_photo, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PhotosRecyclerAdapter.ViewHolder viewHolder, int position) {

        Photo photo = photos.get(position);

        viewHolder.tvTitle.setText(photo.getTitle());

        Glide.with(fragment).load(presenter.getPhotoUrl(photo)).placeholder(
                R.drawable.drawable_placeholder).error(
                R.drawable.drawable_placeholder).into(viewHolder.ivPhoto);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void clear() {
        int size = getItemCount();
        photos.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void addAll(List<Photo> photos) {
        int prevSize = getItemCount();
        this.photos.addAll(photos);
        notifyItemRangeInserted(prevSize, photos.size());
    }
}
