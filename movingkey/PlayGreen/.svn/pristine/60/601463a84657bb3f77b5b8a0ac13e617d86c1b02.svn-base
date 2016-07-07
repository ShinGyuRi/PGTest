package kr.innisfree.playgreen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moyusoft.util.Definitions;
import com.moyusoft.util.DeviceUtil;
import com.moyusoft.util.TextUtil;
import com.volley.network.dto.NetworkData;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-02-19.
 */
public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

	public final String TYPE_UPDATE = "U";
	public final String TYPE_NOTICE = "N";

	public int expandPosition;

	private Context context;
	private ArrayList<NetworkData> itemArray;
	private AdapterItemClickListener adapterItemClickListener;

	public void setAdapterItemClickListener(AdapterItemClickListener listener) {
		this.adapterItemClickListener = listener;
	}

	public NoticeAdapter(Context context) {
		expandPosition = -1;
		this.context = context;
	}

	public void setItemArray(ArrayList<NetworkData> array) {
		if (itemArray == null || itemArray.size() == 0) {
			this.itemArray = array;
		} else {
			itemArray.addAll(array);
		}
		notifyDataSetChanged();
	}

	public NetworkData getItem(int position) {
		if (itemArray == null || itemArray.size() <= position) return null;
		return itemArray.get(position);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);
		ViewHolder vh = new ViewHolder(view);
		return vh;
	}

	public void setExpandPosition(int position){
		if(position == expandPosition){
			expandPosition = -1;
		}else{
			expandPosition = position;
		}
		notifyDataSetChanged();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		if (itemArray == null || itemArray.size() <= position) return;
		NetworkData item = itemArray.get(position);

		if(position == expandPosition){
			holder.layoutContent.setVisibility(View.VISIBLE);
			holder.imgArrow.setSelected(true);
		}else{
			holder.layoutContent.setVisibility(View.GONE);
			holder.imgArrow.setSelected(false);
		}

		if (!TextUtil.isNull(item.CATEGORY) && item.CATEGORY.equals(TYPE_UPDATE)){
			holder.txtType.setText(R.string.str_update);
			holder.txtUpdate.setVisibility(View.VISIBLE);
			if(DeviceUtil.getVersionCode(context) < Definitions.APP_RECENT_VERSION){
				holder.txtUpdate.setText(R.string.str_update);
				holder.txtUpdate.setEnabled(true);
			}else{
				holder.txtUpdate.setText(R.string.str_user_recent_version);
				holder.txtUpdate.setEnabled(false);
			}
		} else {
			holder.txtType.setText(R.string.str_notice);
			holder.txtUpdate.setVisibility(View.GONE);
		}

		if(item.REG_DT>0){
			holder.txtRegdate.setText(PlaygreenManager.getTimeStampToDate(item.REG_DT, false));
		}

		if(!TextUtil.isNull(item.NOTICE_TITLE))
			holder.txtTitle.setText(item.NOTICE_TITLE);

		if(!TextUtil.isNull(item.NOTICE_TEXT))
			holder.txtContent.setText(item.NOTICE_TEXT);

		View.OnClickListener clickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (adapterItemClickListener != null)
					adapterItemClickListener.onAdapterItemClick(v, position);
			}
		};
		holder.layoutTitle.setOnClickListener(clickListener);
		holder.txtUpdate.setOnClickListener(clickListener);
	}

	@Override
	public int getItemCount() {
		if (itemArray == null) {
			return 0;
		}
		return itemArray.size();
	}


	public class ViewHolder extends RecyclerView.ViewHolder {
		public LinearLayout layoutTitle, layoutContent;
		public TextView txtType, txtRegdate, txtTitle, txtContent, txtUpdate;
		public ImageView imgArrow;

		public ViewHolder(View v) {
			super(v);
			layoutTitle = (LinearLayout) v.findViewById(R.id.layout_title);
			layoutContent = (LinearLayout) v.findViewById(R.id.layout_content);
			txtType = (TextView) v.findViewById(R.id.txt_type);
			txtRegdate = (TextView) v.findViewById(R.id.txt_reg_date);
			txtTitle = (TextView) v.findViewById(R.id.txt_title);
			txtContent = (TextView) v.findViewById(R.id.txt_content);
			txtUpdate = (TextView) v.findViewById(R.id.txt_update);
			imgArrow = (ImageView) v.findViewById(R.id.img_arrow);
		}
	}
}
