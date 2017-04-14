package com.sagarnileshshah.carouselmvp.ui.photodetail;

import static com.sagarnileshshah.carouselmvp.util.Properties.PHOTO_URL;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sagarnileshshah.carouselmvp.R;
import com.sagarnileshshah.carouselmvp.data.models.comment.Comment;
import com.sagarnileshshah.carouselmvp.data.models.photo.Photo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The {@link android.support.v7.widget.RecyclerView.Adapter} that renders and populates each
 * comment in the comments list.
 */
public class PhotoDetailRecyclerAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int PHOTO_DETAIL_TYPE = 0;
    private static final int COMMENT_TYPE = 1;
    private static final int PROGRESSBAR_TYPE = 2;
    private static final int PLACEHOLDER_TEXT_TYPE = 3;

    private List<Comment> comments;
    private Photo photo;
    private Fragment fragment;
    private boolean showProgressBar;

    public PhotoDetailRecyclerAdapter(Fragment fragment, Photo photo, List<Comment> comments) {
        this.fragment = fragment;
        this.photo = photo;
        this.comments = comments;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvContent)
        TextView tvContent;

        @BindView(R.id.tvAuthor)
        TextView tvAuthor;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class PhotoDetailViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivPhoto)
        ImageView ivPhoto;

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        public PhotoDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ProgressBarViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        public ProgressBarViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class PlaceholderTextViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvPlaceholder)
        TextView tvPlaceholder;

        public PlaceholderTextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setProgressBar(boolean show) {
        showProgressBar = show;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return PHOTO_DETAIL_TYPE;
        } else if (position >= comments.size() && showProgressBar) {
            return PROGRESSBAR_TYPE;
        } else if (comments.isEmpty()) {
            return PLACEHOLDER_TEXT_TYPE;
        } else {
            return COMMENT_TYPE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case PHOTO_DETAIL_TYPE:
                viewHolder = inflatePhotoDetailViewHolder(inflater, parent);
                break;
            case COMMENT_TYPE:
                viewHolder = inflateCommentViewHolder(inflater, parent);
                break;
            case PROGRESSBAR_TYPE:
                viewHolder = inflateProgressbarViewHolder(inflater, parent);
                break;
            default:
                viewHolder = inflatePlaceholderTextViewHolder(inflater, parent);
        }

        return viewHolder;
    }

    private RecyclerView.ViewHolder inflatePhotoDetailViewHolder(LayoutInflater layoutInflater,
            ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.item_photo_detail, parent, false);
        return new PhotoDetailViewHolder(view);
    }

    private RecyclerView.ViewHolder inflateCommentViewHolder(LayoutInflater layoutInflater,
            ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    private RecyclerView.ViewHolder inflateProgressbarViewHolder(LayoutInflater layoutInflater,
            ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.item_progressbar, parent, false);
        return new ProgressBarViewHolder(view);
    }

    private RecyclerView.ViewHolder inflatePlaceholderTextViewHolder(LayoutInflater layoutInflater,
            ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.item_placeholder_text, parent, false);
        return new PlaceholderTextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = viewHolder.getItemViewType();

        // ProgressBar and Placeholder text viewholders do not need any data binding
        switch (viewType) {
            case PHOTO_DETAIL_TYPE:
                renderPhotoDetail(viewHolder);
                break;
            case COMMENT_TYPE: COMMENT_TYPE:
                renderComment(viewHolder, position);
            default: return;
        }
    }

    private void renderPhotoDetail(RecyclerView.ViewHolder viewHolder) {
        PhotoDetailViewHolder photoDetailViewHolder = (PhotoDetailViewHolder) viewHolder;

        photoDetailViewHolder.tvTitle.setText(photo.getTitle());

        String photoUrl = String.format(PHOTO_URL, photo.getFarm(), photo.getServer(),
                photo.getId(), photo.getSecret());

        Glide.with(fragment).load(photoUrl).placeholder(R.drawable.drawable_placeholder).error(
                R.drawable.drawable_placeholder).into(photoDetailViewHolder.ivPhoto);
    }

    private void renderComment(RecyclerView.ViewHolder viewHolder, int position) {
        CommentViewHolder commentViewHolder = (CommentViewHolder) viewHolder;

        /**
         * Subtracting 1 is to offset the position taken by the Photo which is always at the top
         * of the list
         */
        Comment comment = comments.get(position - 1);

        commentViewHolder.tvContent.setText(comment.getContent());
        commentViewHolder.tvAuthor.setText(comment.getRealname());
    }

    /**
     * Adding 2 to accommodate the Photo item and progressbar/placeholder text
     */
    @Override
    public int getItemCount() {
        return comments.size() + 2;
    }

    public void clear() {
        int size = getItemCount();
        comments.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void addAll(List<Comment> comments) {
        int prevSize = getItemCount();
        this.comments.addAll(comments);
        notifyItemRangeInserted(prevSize, comments.size());
    }

}
