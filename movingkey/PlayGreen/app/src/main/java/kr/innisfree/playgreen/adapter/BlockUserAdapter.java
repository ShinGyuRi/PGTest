package kr.innisfree.playgreen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moyusoft.util.BitmapCircleResize;
import com.moyusoft.util.TextUtil;
import com.squareup.picasso.Picasso;
import com.volley.network.dto.NetworkData;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-02-19.
 */
public class BlockUserAdapter extends RecyclerView.Adapter<BlockUserAdapter.ViewHolder> {

	public class ViewHolder extends RecyclerView.ViewHolder {
		public LinearLayout layoutItem, layoutBlockToggle;
		public ImageView imgProfile;
		public TextView txtNickname, txtState, txtBlockToggle;

		public ViewHolder(View v) {
			super(v);
			layoutItem = (LinearLayout) v.findViewById(R.id.layout_item);
			imgProfile = (ImageView) v.findViewById(R.id.img_item_profile);
			txtNickname = (TextView) v.findViewById(R.id.txt_item_name);
			txtState = (TextView) v.findViewById(R.id.txt_item_state);
			layoutBlockToggle = (LinearLayout)v.findViewById(R.id.layout_block_toggle);
			txtBlockToggle = (TextView)v.findViewById(R.id.txt_block_toggle);
		}
	}

	private Context context;
	private ArrayList<NetworkData> itemArray;
	private AdapterItemClickListener adapterItemClickListener;

	public void setAdapterItemClickListener(AdapterItemClickListener listener) {
		this.adapterItemClickListener = listener;
	}

	public BlockUserAdapter(Context context) {
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
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_block_user, parent, false);
		ViewHolder vh = new ViewHolder(view);
		return vh;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		if (itemArray == null || itemArray.size() <= position) return;
		NetworkData item = itemArray.get(position);

		if(!TextUtil.isNull(item.MEMB_NAME))
			holder.txtNickname.setText(item.MEMB_NAME);
		if (!TextUtil.isNull(item.STATE_TEXT)){
			holder.txtState.setVisibility(View.VISIBLE);
			holder.txtState.setText(item.STATE_TEXT);
		}else{
			holder.txtState.setVisibility(View.GONE);
		}

		if(!TextUtil.isNull(item.MEMB_IMG))
			Picasso.with(context).load(item.MEMB_IMG).transform(new BitmapCircleResize(context,context.getResources().getDimensionPixelOffset(R.dimen.dp_30))).into(holder.imgProfile);

		if(item.isBlockCancel){
			holder.layoutBlockToggle.setSelected(true);
			holder.txtBlockToggle.setText(R.string.str_user_block);
		}else{
			holder.layoutBlockToggle.setSelected(false);
			holder.txtBlockToggle.setText(R.string.str_user_block_cancel);
		}


		View.OnClickListener clickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (adapterItemClickListener != null)
					adapterItemClickListener.onAdapterItemClick(v, position);
			}
		};
		holder.layoutItem.setOnClickListener(clickListener);
		holder.layoutBlockToggle.setOnClickListener(clickListener);
	}

	@Override
	public int getItemCount() {
		if (itemArray == null) {
			return 0;
		}
		return itemArray.size();
	}
}
