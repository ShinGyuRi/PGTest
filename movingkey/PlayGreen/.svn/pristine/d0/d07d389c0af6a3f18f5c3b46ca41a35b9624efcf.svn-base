package kr.innisfree.playgreen.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.TextUtil;

import java.io.File;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.data.PlayGreenImageFilter;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;


/**
 * Created by preparkha on 2015. 11. 12..
 */
public class VideoPreviewAdapter extends RecyclerView.Adapter {

    private Bitmap bitmap;
    private String absolutePath;
    private Bitmap item;
    private List<Bitmap> adapterArrayList;
    private static Context context;
    private AdapterItemClickListener adapterItemClickListener;

    public static class PreviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgPreview;

        private AdapterItemClickListener itemClickListener;

        public PreviewViewHolder(View v) {
            super(v);

            imgPreview = (ImageView) v.findViewById(R.id.camera_cg_tool_img_preview);

        }

        public void setClickListener(AdapterItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null)
                itemClickListener.onAdapterItemClick(v, getLayoutPosition());
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public VideoPreviewAdapter(Context context) {
        this.context = context;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public void setAdapterItemClickListener(AdapterItemClickListener adapterItemClickListener) {
        this.adapterItemClickListener = adapterItemClickListener;
    }

    public void setAdapterArrayList(List<Bitmap> adapterArrayList) {
        this.adapterArrayList = adapterArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_frag_camera_cg_tool_trim_preview, parent, false);
        vh = new PreviewViewHolder(v);
        ((PreviewViewHolder) vh).setClickListener(adapterItemClickListener);

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        ((PreviewViewHolder) holder).imgPreview.setImageBitmap(null);

        if (adapterArrayList == null || adapterArrayList.size() <= position)
            return;

        item = adapterArrayList.get(position);

        if (item != null) {
            ((PreviewViewHolder) holder).imgPreview.setImageBitmap(item);
        }

    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (adapterArrayList != null)
            itemCount = adapterArrayList.size();
        return itemCount;
    }
}
