package kr.innisfree.playgreen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moyusoft.util.BitmapCircleResize;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.YN;
import com.moyusoft.util.PrefUtil;
import com.moyusoft.util.TextUtil;
import com.squareup.picasso.Picasso;
import com.volley.network.dto.NetworkData;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-02-19.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

	public class ViewHolder extends RecyclerView.ViewHolder {
		public LinearLayout layoutItem;
		public ImageView imgProfile, imgBadge;
		public TextView txtNickname, txtState;
		public Button btnFollow;

		public ViewHolder(View v) {
			super(v);
			layoutItem = (LinearLayout) v.findViewById(R.id.layout_item);
			imgProfile = (ImageView) v.findViewById(R.id.img_item_profile);
			imgBadge = (ImageView)v.findViewById(R.id.img_icon_badge);
			txtNickname = (TextView) v.findViewById(R.id.txt_item_name);
			txtState = (TextView) v.findViewById(R.id.txt_item_state);
			btnFollow = (Button) v.findViewById(R.id.btn_item_follow);
		}
	}

	private Context context;
	private ArrayList<NetworkData> itemArray;
	private AdapterItemClickListener adapterItemClickListener;
	private String membID;

	public void setAdapterItemClickListener(AdapterItemClickListener listener) {
		this.adapterItemClickListener = listener;
	}

	public UserAdapter(Context context) {
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
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
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

		/** 프로필 뱃지 */
		boolean isSuperGreener = false, isBestGreener = false;
		if(!TextUtils.isEmpty(item.SUPERGREENER_YN) && item.SUPERGREENER_YN.equals(YN.YES))
			isSuperGreener = true;
		if(!TextUtils.isEmpty(item.BESTGREENER_YN) && item.BESTGREENER_YN.equals(YN.YES))
			isBestGreener = true;
		if(isSuperGreener || isBestGreener){
			holder.imgBadge.setVisibility(View.VISIBLE);
			if(isSuperGreener && isBestGreener)
				holder.imgBadge.setBackgroundResource(R.drawable.icon_badge_both);
			else if(isSuperGreener)
				holder.imgBadge.setBackgroundResource(R.drawable.icon_badge_sg);
			else if(isBestGreener)
				holder.imgBadge.setBackgroundResource(R.drawable.icon_badge_bestpg);
		}else{
			holder.imgBadge.setVisibility(View.GONE);
		}

		if(!TextUtil.isNull(item.OWN_YN) && item.OWN_YN.equals(YN.YES)){
			holder.btnFollow.setVisibility(View.GONE);
		}else{
			holder.btnFollow.setVisibility(View.VISIBLE);
			if(!TextUtil.isNull(item.FRIEND_YN) && item.FRIEND_YN.equals(YN.YES))
				holder.btnFollow.setSelected(true);
			else
				holder.btnFollow.setSelected(false);
		}

		View.OnClickListener clickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (adapterItemClickListener != null)
					adapterItemClickListener.onAdapterItemClick(v, position);
			}
		};
		holder.layoutItem.setOnClickListener(clickListener);
		holder.btnFollow.setOnClickListener(clickListener);
	}

	@Override
	public int getItemCount() {
		if (itemArray == null) {
			return 0;
		}
		return itemArray.size();
	}

	public boolean isMine(String id){
		if(TextUtil.isNull(membID)){
			membID = PrefUtil.getInstance().getStringPreference(Definitions.PREFKEY.MEMB_ID_STR);
		}
		if(!TextUtil.isNull(id) && !TextUtil.isNull(membID) && membID.equals(id)){
			return true;
		}
		return false;
	}
}
