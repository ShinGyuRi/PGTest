package kr.innisfree.playgreen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moyusoft.util.TextUtil;
import com.squareup.picasso.Picasso;
import com.volley.network.dto.NetworkData;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-03-11.
 */
public class PG21Adapter_back extends RecyclerView.Adapter<PG21Adapter_back.PG21ViewHolder> {
	private Context context;
	private ArrayList<NetworkData> itemArray;
	private AdapterItemClickListener adapterItemClickListener;

	public PG21Adapter_back(Context context) {
		this.context = context;
	}

	public void setAdapterItemClickListener(AdapterItemClickListener listener) {
		this.adapterItemClickListener = listener;
	}

	public void setItemArray(ArrayList<NetworkData> array) {
		this.itemArray = array;
		notifyDataSetChanged();
	}

	@Override
	public PG21Adapter_back.PG21ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view;
		PG21ViewHolder vh;
		view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pg21, parent, false);
		vh = new PG21ViewHolder(view);
		return vh;
	}

	@Override
	public void onBindViewHolder(PG21Adapter_back.PG21ViewHolder holder, final int position) {
		if (itemArray == null || itemArray.size() <= position) return;
		NetworkData item = itemArray.get(position);
		//Picasso.with(context).load(item.TIMELINE_IMG).into(holder.imgContent);

		holder.txtAddMission.setVisibility(View.GONE);
		holder.marginView.setVisibility(View.GONE);

		if(position == 0 )
			holder.txtAddMission.setVisibility(View.VISIBLE);
		if((getItemCount() -1) == position)
			holder.marginView.setVisibility(View.VISIBLE);

		if(!TextUtil.isNull(item.MAIN_IMG)){
			Picasso.with(context).load(item.MAIN_IMG).into(holder.imgContent);
		}
		if(position == 0){
			holder.imgContent.setBackgroundResource(R.drawable.img_todaymisson);
//			File croppedFile = new File(context.getCacheDir(), "cropped.png");
//			Picasso.with(context).load(croppedFile).skipMemoryCache().into(holder.imgContent);
		}

		holder.txtDay.setText(context.getString(R.string.str_pg21_day, getItemCount()-position)+" ");

		View.OnClickListener clickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (adapterItemClickListener != null)
					adapterItemClickListener.onAdapterItemClick(v, position);
			}
		};
		holder.imgContent.setOnClickListener(clickListener);
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
	public class PG21ViewHolder extends RecyclerView.ViewHolder {
		public ImageView imgContent;
		public TextView txtDay, txtMissionName, txtMissionInfo;
		public TextView txtAddMission;
		public View marginView;

		public PG21ViewHolder(View v) {
			super(v);
			imgContent = (ImageView) v.findViewById(R.id.img_pg21_item_bg);
			txtDay = (TextView) v.findViewById(R.id.txt_pg21_day);
			txtMissionName = (TextView) v.findViewById(R.id.txt_pg21_mission_name);
			txtMissionInfo = (TextView) v.findViewById(R.id.txt_pg21_mission_info);
			txtAddMission = (TextView) v.findViewById(R.id.txt_add_mission);
			marginView = (View) v.findViewById(R.id.view_margin);
		}
	}

}