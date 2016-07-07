package kr.innisfree.playgreen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.volley.network.dto.NetworkData;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;

/**
 * Created by jooyoung on 2016-02-19.
 */
public class StickyAdapter extends RecyclerView.Adapter<StickyAdapter.ViewHolder> implements StickyRecyclerHeadersAdapter<StickyAdapter.HeaderViewHolder> {
	private Context context;
	private ArrayList<NetworkData> itemArray;
	public StickyAdapter(Context context){
		this.context =context;
	}

	public void setItemArray(ArrayList<NetworkData> array){
		this.itemArray = array;
		notifyDataSetChanged();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline, parent, false);
		ViewHolder vh = new ViewHolder(view);
		return vh;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.txtContent.setText("POSITION #"+position);

	}

	@Override
	public int getItemCount() {
		if(itemArray ==null){
			return 0;
		}
		return itemArray.size();
	}

	@Override
	public long getItemId(int position) {
		//return super.getItemId(position);
		return position;
	}

	/**
	 * About Header
	 */
	@Override
	public long getHeaderId(int position) {
//		if (position == 0) {
//			return -1;
//		} else {
//			return getItem(position).charAt(0);
//		}
		return position;
	}

	@Override
	public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_header, parent, false);
		HeaderViewHolder vh = new HeaderViewHolder(view);
		return vh;
	}

	@Override
	public void onBindHeaderViewHolder(HeaderViewHolder holder, int position) {

	}


	/**
	 *  View holder
	 */
	public class ViewHolder extends RecyclerView.ViewHolder {
		public ImageView imgProfile;
		public TextView txtContent;
		public ViewHolder(View v) {
			super(v);
			imgProfile = (ImageView)v.findViewById(R.id.img_profile_item);
			txtContent = (TextView)v.findViewById(R.id.txt_item_content);
		}
	}

	public class HeaderViewHolder extends RecyclerView.ViewHolder {
		public ImageView imgProfile;
		public TextView txtTest;
		public HeaderViewHolder(View v) {
			super(v);
//			imgProfile = (ImageView)v.findViewById(R.id.img_profile_item);
//			txtTest = (TextView)v.findViewById(R.id.txt_item_content);
		}
	}
}