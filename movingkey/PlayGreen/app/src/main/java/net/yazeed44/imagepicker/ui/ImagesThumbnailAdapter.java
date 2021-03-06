package net.yazeed44.imagepicker.ui;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.yazeed44.imagepicker.model.AlbumEntry;
import net.yazeed44.imagepicker.model.ImageEntry;
import net.yazeed44.imagepicker.util.Events;
import net.yazeed44.imagepicker.util.Picker;
import net.yazeed44.imagepicker.util.Util;

import org.greenrobot.eventbus.EventBus;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.transformation.ScaledResizeBitmapTransformation;


/**
 * Created by yazeed44 on 11/23/14.
 */
public class ImagesThumbnailAdapter extends RecyclerView.Adapter<ImagesThumbnailAdapter.ImageViewHolder> implements Util.OnClickImage {


    protected final AlbumEntry mAlbum;
    protected final RecyclerView mRecyclerView;
    protected final Picker mPickOptions;

    protected final Fragment mFragment;
    private OnPickImageListener pickImageListener;


    public ImagesThumbnailAdapter(final Fragment fragment, final AlbumEntry album, final RecyclerView recyclerView, Picker pickOptions) {
        mFragment = fragment;
        this.mAlbum = album;
        this.mRecyclerView = recyclerView;
        mPickOptions = pickOptions;
    }

    public ImagesThumbnailAdapter(final Fragment fragment, final AlbumEntry album, final RecyclerView recyclerView, Picker pickOptions, OnPickImageListener pickImageListener) {
        mFragment = fragment;
        this.mAlbum = album;
        this.mRecyclerView = recyclerView;
        mPickOptions = pickOptions;
        this.pickImageListener = pickImageListener;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View imageLayout = LayoutInflater.from(mRecyclerView.getContext()).inflate(R.layout.element_image, viewGroup, false);
        return new ImageViewHolder(imageLayout, this);
    }

    @Override
    public int getItemCount() {
        return mAlbum.imageList.size();
    }

    @Override
    public void onBindViewHolder(ImageViewHolder imageViewHolder, int position) {
        final ImageEntry imageEntry = mAlbum.imageList.get(position);
        setHeight(imageViewHolder.itemView);
        displayThumbnail(imageViewHolder, imageEntry);
        drawGrid(imageViewHolder, imageEntry);
    }

    public void setHeight(final View convertView) {
        final int height = mRecyclerView.getMeasuredWidth() / mRecyclerView.getResources().getInteger(R.integer.num_columns_images);
        convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
    }

    public void displayThumbnail(final ImageViewHolder holder, final ImageEntry photo) {
        Glide.with(mFragment)
                .load(photo.path)
                .bitmapTransform(
                        new ScaledResizeBitmapTransformation(mFragment.getActivity())
                )
                .centerCrop()
                .into(holder.thumbnail);
    }

    public void drawGrid(final ImageViewHolder holder, final ImageEntry imageEntry) {
        holder.videoIcon.setVisibility(View.GONE);

        if (imageEntry.isPicked) {
            holder.check.setSelected(true);
            holder.imgPick.setVisibility(View.VISIBLE);
            //holder.thumbnail.setColorFilter(mPickOptions.checkedImageOverlayColor);
        } else {
            holder.imgPick.setVisibility(View.GONE);
            holder.check.setSelected(false);
            //holder.thumbnail.setColorFilter(Color.TRANSPARENT);
        }

        if (mPickOptions.pickMode == Picker.PickMode.SINGLE_IMAGE) {
            holder.check.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClickImage(View layout, ImageView thumbnail, ImageView check) {
        final int position = Util.getPositionOfChild(layout, R.id.image_layout, mRecyclerView);
        final ImageViewHolder holder = (ImageViewHolder) mRecyclerView.getChildViewHolder(layout);
        if (pickImageListener != null) {
            pickImageListener.onPickImage(mAlbum.imageList.get(position), position);
        }
        //pickImage(holder, mAlbum.imageList.get(position));
    }

    public void pickImage(final ImageViewHolder holder, final ImageEntry imageEntry) {
        if (imageEntry.isPicked) {
            //Unpick
            EventBus.getDefault().post(new Events.OnUnpickImageEvent(imageEntry));
        } else {
            //pick
            EventBus.getDefault().postSticky(new Events.OnPickImageEvent(imageEntry));
        }
        drawGrid(holder, imageEntry);
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView thumbnail;
        private final ImageView check;
        private final ImageView videoIcon;
        private final ImageView imgPick;

        public ImageViewHolder(final View itemView, final Util.OnClickImage listener) {
            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.image_thumbnail);
            check = (ImageView) itemView.findViewById(R.id.image_check);
            videoIcon = (ImageView) itemView.findViewById(R.id.image_video_icon);
            imgPick = (ImageView) itemView.findViewById(R.id.img_pick);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickImage(itemView, thumbnail, check);
                }
            });
        }
    }

    public interface OnPickImageListener {
        void onPickImage(ImageEntry entry, int posision);
    }

}
