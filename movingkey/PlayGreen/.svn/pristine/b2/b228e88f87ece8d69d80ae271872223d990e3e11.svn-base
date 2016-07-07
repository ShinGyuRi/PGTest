package kr.innisfree.playgreen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moyusoft.util.TextUtil;
import com.volley.network.dto.NetworkData;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-02-19.
 */
public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

	public int selectCountryPosition;

	public class ViewHolder extends RecyclerView.ViewHolder {
		public LinearLayout layoutItem;
		public TextView txtCountry;
		public ImageView imgRadio;

		public ViewHolder(View v) {
			super(v);
			layoutItem = (LinearLayout) v.findViewById(R.id.layout_item_country);
			imgRadio = (ImageView) v.findViewById(R.id.img_radio);
			txtCountry = (TextView) v.findViewById(R.id.txt_item_country);
		}
	}

	private ArrayList<NetworkData> itemArray;
	private AdapterItemClickListener adapterItemClickListener;

	public void setAdapterItemClickListener(AdapterItemClickListener listener) {
		this.adapterItemClickListener = listener;
	}

	public CountryAdapter() {
		selectCountryPosition = -1;
	}

	public void setItemArray(ArrayList<NetworkData> array) {
		this.itemArray = array;
		notifyDataSetChanged();
	}

	public NetworkData getItem(int position){
		if(itemArray==null || itemArray.size()<=position)	  return null;
		return itemArray.get(position);
	}

	public void setSelectCountryPosition(int position){
		this.selectCountryPosition = position;
		notifyDataSetChanged();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
		ViewHolder vh = new ViewHolder(view);
		return vh;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		if (itemArray == null || itemArray.size() <= position) return;
		NetworkData item = itemArray.get(position);

		if(!TextUtil.isNull(item.country))
			holder.txtCountry.setText(item.country);
		if(position == selectCountryPosition){
			holder.imgRadio.setSelected(true);
		}else{
			holder.imgRadio.setSelected(false);
		}

		View.OnClickListener clickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (adapterItemClickListener != null)
					adapterItemClickListener.onAdapterItemClick(v, position);
			}
		};
		holder.layoutItem.setOnClickListener(clickListener);
	}

	@Override
	public int getItemCount() {
		if (itemArray == null) {
			return 0;
		}
		return itemArray.size();
	}
}
