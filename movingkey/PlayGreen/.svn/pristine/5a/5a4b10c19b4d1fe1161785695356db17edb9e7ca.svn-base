package kr.innisfree.playgreen.adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moyusoft.util.TextUtil;
import com.volley.network.dto.NetworkData;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.common.PlaygreenManager;

/**
 * Created by jooyoung on 2016-02-19.
 */
public class PointHistoryAdapter extends RecyclerView.Adapter<PointHistoryAdapter.ViewHolder> {

	public class ViewHolder extends RecyclerView.ViewHolder {
		public TextView txtSubject, txtDate, txtPoint, txtPointState;

		public ViewHolder(View v) {
			super(v);
			txtSubject = (TextView) v.findViewById(R.id.txt_subject);
			txtDate = (TextView) v.findViewById(R.id.txt_date);
			txtPoint = (TextView)v.findViewById(R.id.txt_point);
			txtPointState = (TextView)v.findViewById(R.id.txt_point_state);
		}
	}

	private Context context;
	private ArrayList<NetworkData> itemArray;

	public PointHistoryAdapter(Context context) {
		this.context = context;
	}

	public void setItemArray(ArrayList<NetworkData> array) {
		this.itemArray = array;
		notifyDataSetChanged();
	}

	public NetworkData getItem(int position){
		if(itemArray==null || itemArray.size()<=position)	  return null;
		return itemArray.get(position);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_point_history, parent, false);
		ViewHolder vh = new ViewHolder(view);
		return vh;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		if (itemArray == null || itemArray.size() <= position) return;
		NetworkData item = itemArray.get(position);

		if(!TextUtil.isNull(item.POINT_TEXT))
			holder.txtSubject.setText(item.POINT_TEXT);
		if(item.REG_DT>0)
			holder.txtDate.setText(PlaygreenManager.getTimeStampToDate(item.REG_DT,false));


		if(!TextUtil.isNull(item.POINT_DEPT)){
			int textColor;
			if(item.POINT_DEPT.equals("P")){	//적립
				textColor = ResourcesCompat.getColor(context.getResources(), R.color.c_4b9b50, context.getTheme());
				holder.txtPointState.setText(R.string.str_point_save);
			}else{
				textColor = ResourcesCompat.getColor(context.getResources(), R.color.c_e9552c, context.getTheme());
				holder.txtPointState.setText(R.string.str_point_use);
			}
			holder.txtPoint.setTextColor(textColor);
			holder.txtPointState.setTextColor(textColor);
		}
		holder.txtPoint.setText(TextUtil.makeStringWithComma(item.POINT+"",false)+"p");


	}

	@Override
	public int getItemCount() {
		if (itemArray == null) {
			return 0;
		}
		return itemArray.size();
	}
}
