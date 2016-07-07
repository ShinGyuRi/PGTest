package kr.innisfree.playgreen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class PG21MissionAdapter extends RecyclerView.Adapter<PG21MissionAdapter.ViewHolder> {
	private Context context;
	private ArrayList<NetworkData> itemArray;
	private AdapterItemClickListener adapterItemClickListener;

	public PG21MissionAdapter(Context context) {
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
	public PG21MissionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view;
		ViewHolder vh;
		view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mission, parent, false);
		vh = new ViewHolder(view);
		return vh;
	}

	@Override
	public void onBindViewHolder(PG21MissionAdapter.ViewHolder holder, final int position) {
		if (itemArray == null || itemArray.size() <= position) return;
		NetworkData item = itemArray.get(position);
		//Picasso.with(context).load(item.TIMELINE_IMG).into(holder.imgContent);

		if(position == 0){
			Picasso.with(context).load(R.drawable.btn_add_misson).into(holder.imgMission);
			holder.txtMission.setText(R.string.str_mission_array_00);
		}else{
			if(!TextUtil.isNull(item.PG21_MS_IMG)){
				Picasso.with(context).load(item.PG21_MS_IMG).into(holder.imgMission);
			}
			holder.txtMission.setText(item.PG21_MS_TITLE);
		}

		View.OnClickListener clickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(adapterItemClickListener!=null) adapterItemClickListener.onAdapterItemClick(v,position);
			}
		};
		holder.layoutMission.setOnClickListener(clickListener);

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
	public class ViewHolder extends RecyclerView.ViewHolder {
		public LinearLayout layoutMission;
		public ImageView imgMission;
		public TextView txtMission;

		public ViewHolder(View v) {
			super(v);
			layoutMission = (LinearLayout)v.findViewById(R.id.layout_mission);
			imgMission = (ImageView)v.findViewById(R.id.img_mission);
			txtMission = (TextView)v.findViewById(R.id.txt_mission);
		}
	}

}