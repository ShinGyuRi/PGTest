package kr.innisfree.playgreen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.moyusoft.util.BitmapWidthResize;
import com.moyusoft.util.TextUtil;
import com.moyusoft.util.ToforUtil;
import com.squareup.picasso.Picasso;
import com.volley.network.dto.NetworkData;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-02-19.
 */
public class TimelineGridAdapter extends RecyclerView.Adapter<TimelineGridAdapter.ViewHolder> {
	private Context context;
	private ArrayList<NetworkData> itemArray;
	private AdapterItemClickListener adapterItemClickListener;
	private RecyclerView recyclerView;

	public TimelineGridAdapter(Context context) {
		this.context = context;
	}

	public TimelineGridAdapter(Context context, AdapterItemClickListener listener, RecyclerView recyclerView) {
		this.context = context;
		this.adapterItemClickListener = listener;
		this.recyclerView = recyclerView;
	}

	public void setAdapterItemClickListener(AdapterItemClickListener listener) {
		this.adapterItemClickListener = listener;
	}

	public void setItemArray(ArrayList<NetworkData> array) {
		if (itemArray == null || itemArray.size() == 0) {
			this.itemArray = array;
		} else {
			itemArray.addAll(array);
		}
		notifyDataSetChanged();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_grid, parent, false);
		ViewHolder vh = new ViewHolder(view);
		return vh;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		if (itemArray == null || itemArray.size() <= position) return;
		NetworkData item = itemArray.get(position);
		setHeight(holder.itemView);
		/**
		 * grid view 에서는 비디오는 재생하지 않는건가?
		 * 그렇다면, api에서 video url 은 절대 안날라오는건가?
		 * 우선은 지금처럼 TIMELINE_MP4_SCENE으로 분기처리하는데, TIMELINE_MP4로 분기처리했으면 한다. (내 생각)
		 */
//		if (!TextUtil.isNull(item.TIMELINE_MP4_SCENE)) {
//			holder.imgContent.setVisibility(View.VISIBLE);
//			holder.imgPlay.setVisibility(View.VISIBLE);
//			Picasso.with(context).load(item.TIMELINE_MP4_SCENE).transform(new BitmapWidthResize(ToforUtil.PHONE_W / 3)).into(holder.imgContent);
//		}else if (!TextUtil.isNull(item.TIMELINE_IMG)) {
//			holder.imgContent.setVisibility(View.VISIBLE);
//			Picasso.with(context).load(item.TIMELINE_IMG).transform(new BitmapWidthResize(ToforUtil.PHONE_W / 3)).into(holder.imgContent);		//transform(new BitmapWidthResize(ToforUtil.PHONE_W / 3)).
//		}
		if (!TextUtil.isNull(item.TIMELINE_MP4_SCENE)) {
			holder.imgContent.setVisibility(View.VISIBLE);
			holder.imgPlay.setVisibility(View.VISIBLE);
//			Picasso.with(context).load(item.TIMELINE_MP4_SCENE).transform(new BitmapWidthResize(ToforUtil.PHONE_W / 3)).into(holder.imgContent);
		}
		if (!TextUtil.isNull(item.TIMELINE_IMG)) {
			holder.imgContent.setVisibility(View.VISIBLE);
			Picasso.with(context).load(item.TIMELINE_IMG).transform(new BitmapWidthResize(ToforUtil.PHONE_W / 3)).into(holder.imgContent);		//transform(new BitmapWidthResize(ToforUtil.PHONE_W / 3)).
		}
//		if (!TextUtil.isNull(item.TIMELINE_IMG)) {
//			Picasso.with(context).load(item.TIMELINE_IMG).transform(new BitmapWidthResize(ToforUtil.PHONE_W/3)).into(holder.imgContent);
//		}
		if(!TextUtil.isNull(item.HASHTAG_IMG)){
			Picasso.with(context).load(item.HASHTAG_IMG).transform(new BitmapWidthResize(ToforUtil.PHONE_W/3)).into(holder.imgContent);
		}
	}

	public void setHeight(final View convertView) {
		final int height = recyclerView.getMeasuredWidth() / 3;
		convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
	}

	@Override
	public int getItemCount() {
		if (itemArray == null) {
			return 0;
		}
		return itemArray.size();
	}

	public NetworkData getItem(int position) {
		if (itemArray == null || itemArray.size() <= position) return null;
		else return itemArray.get(position);
	}

	/**
	 * View holder
	 */
	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		public ImageView imgContent, imgPlay;

		public ViewHolder(View v) {
			super(v);
			imgContent = (ImageView) v.findViewById(R.id.img_item_content);
			imgPlay = (ImageView) v.findViewById(R.id.img_item_play);

			imgContent.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (adapterItemClickListener != null) {
				adapterItemClickListener.onAdapterItemClick(v, getLayoutPosition());
			}
		}
	}

}