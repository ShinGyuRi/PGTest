package kr.innisfree.playgreen.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.moyusoft.util.BitmapCircleResize;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.TextUtil;
import com.squareup.picasso.Picasso;
import com.volley.network.dto.NetworkData;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.fragment.camera.CameraUploadFrag;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-02-19.
 */
public class HashtagAdapter extends RecyclerView.Adapter<HashtagAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtHashtag;
        public ImageButton imgBtnDeleteHashtag;

        private AdapterItemClickListener adapterItemClickListener;

        public ViewHolder(View v) {
            super(v);
            txtHashtag = (TextView) v.findViewById(R.id.camera_upload_tv_hash_tag);
            imgBtnDeleteHashtag = (ImageButton) v.findViewById(R.id.camera_upload_imgbtn_delete_current_hash_tag);
            imgBtnDeleteHashtag.setVisibility(View.GONE);

            txtHashtag.setOnClickListener(this);
            imgBtnDeleteHashtag.setOnClickListener(this);
        }

        public void setAdapterItemClickListener(AdapterItemClickListener adapterItemClickListener) {
            this.adapterItemClickListener = adapterItemClickListener;
        }

        @Override
        public void onClick(View v) {
            if (adapterItemClickListener != null)
                adapterItemClickListener.onAdapterItemClick(v, getLayoutPosition());
        }
    }

    private Context context;
    private int typeList;
    private String keyword;
    private ArrayList<NetworkData> itemArray;
    private ArrayList<String> itemStrArray;
    private AdapterItemClickListener adapterItemClickListener;

    public HashtagAdapter(Context context) {
        this.context = context;
    }

    public void setTypeList(int typeList) {
        this.typeList = typeList;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setItemArray(ArrayList<NetworkData> array) {
        this.itemArray = array;
        notifyDataSetChanged();
    }

    public void setItemStrArray(ArrayList<String> itemStrArray) {
        this.itemStrArray = itemStrArray;
        notifyDataSetChanged();
    }

    public void removeItemStr(int position) {
        if (this.itemStrArray == null && this.itemStrArray.size() < position)
            return;
        this.itemStrArray.remove(position);
        notifyItemRemoved(position);
    }

    public NetworkData getItem(int position) {
        if (itemArray != null && itemArray.size() > position) {
            return itemArray.get(position);
        } else
            return null;
    }


    public void setAdapterItemClickListener(AdapterItemClickListener adapterItemClickListener) {
        this.adapterItemClickListener = adapterItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hash_tag_list, parent, false);
        ViewHolder vh = new ViewHolder(view);
        vh.setAdapterItemClickListener(this.adapterItemClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (typeList == CameraUploadFrag.TYPE_RECENT_HASHTAG_LIST) {
            if (itemStrArray == null || itemStrArray.size() < position) return;
            String item = itemStrArray.get(position);

            holder.txtHashtag.setText("");
            holder.imgBtnDeleteHashtag.setVisibility(View.GONE);

            if (!TextUtil.isNull(item))
                holder.txtHashtag.setText("#" + item);

            if (typeList == CameraUploadFrag.TYPE_RECENT_HASHTAG_LIST) {
                holder.imgBtnDeleteHashtag.setVisibility(View.VISIBLE);
            }
        } else {
            if (itemArray == null || itemArray.size() < position) return;
            NetworkData item = itemArray.get(position);

            holder.txtHashtag.setText("");
            holder.imgBtnDeleteHashtag.setVisibility(View.GONE);

            if (!TextUtil.isNull(item.HASHTAG)) {
                holder.txtHashtag.setText("#" + item.HASHTAG);
                if (!TextUtil.isNull(keyword) && item.HASHTAG.contains(keyword)) {
                    int start = holder.txtHashtag.getText().toString().indexOf(keyword);
                    int end = start + keyword.length();
//                if(holder.txtHashtag.getText().toString().length() > keyword.length()){
//                    end = start + keyword.length();
//                } else {
//                    end = start + keyword.length();
//                }
                    JYLog.D("index::" + start, new Throwable());
                    SpannableStringBuilder sps = new SpannableStringBuilder(holder.txtHashtag.getText().toString());
                    sps.setSpan(new ForegroundColorSpan(Color.parseColor("#4b9b50")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.txtHashtag.setText(sps);
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        if (typeList == CameraUploadFrag.TYPE_RECENT_HASHTAG_LIST) {
            if (itemStrArray == null) {
                return 0;
            }
            return itemStrArray.size();
        } else {
            if (itemArray == null) {
                return 0;
            }
            return itemArray.size();
        }
    }
}
