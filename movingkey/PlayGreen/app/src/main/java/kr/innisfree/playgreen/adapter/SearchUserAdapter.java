package kr.innisfree.playgreen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moyusoft.util.BitmapCircleResize;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.TextUtil;
import com.squareup.picasso.Picasso;
import com.volley.network.dto.NetworkData;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-02-19.
 */
public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> {

	private Context context;
	private ArrayList<NetworkData> itemArray;
	private AdapterItemClickListener adapterItemClickListener;
	private boolean isSearchHistory;
	private String searchKeyword;

	public SearchUserAdapter(Context context, AdapterItemClickListener listener) {
		this.context = context;
		this.adapterItemClickListener = listener;
	}

	public void setSearchKeyword(String keyword) {
		this.searchKeyword = keyword;
	}

	public void setItemArray(ArrayList<NetworkData> array, boolean isSearchHistory) {
		this.itemArray = array;
		this.isSearchHistory = isSearchHistory;
		notifyDataSetChanged();
	}

	public NetworkData getItem(int position) {
		if (itemArray == null || itemArray.size() <= position) return null;
		return itemArray.get(position);
	}

	@Override
	public int getItemCount() {
		if (itemArray == null) {
			return 0;
		}
		return itemArray.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_user, parent, false);
		ViewHolder vh = new ViewHolder(view);
		return vh;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		if (itemArray == null || itemArray.size() <= position) return;
		NetworkData item = itemArray.get(position);

		Picasso.with(context).load(R.drawable.img_user_null2).into(holder.imgUserProfile);

		/** 검색기록 */
		if (isSearchHistory) {
			holder.btnRemoveHistory.setVisibility(View.VISIBLE);
			if (!TextUtil.isNull(item.SEARCH_KEYWORD)) {
				holder.txtUserName.setText(item.SEARCH_KEYWORD);
			}
//			if(!TextUtil.isNull(item.MEMB_NAME)){
//				holder.txtUserName.setText(item.MEMB_NAME);
//			}
			if (!TextUtil.isNull(item.STATE_TEXT)) {
				holder.txtUserState.setVisibility(View.VISIBLE);
				holder.txtUserState.setText(item.STATE_TEXT);
			} else {
				holder.txtUserState.setVisibility(View.GONE);
			}
			if (!TextUtil.isNull(item.MEMB_IMG)) {
				Picasso.with(context).load(item.MEMB_IMG)
						.transform(new BitmapCircleResize(context, context.getResources().getDimensionPixelOffset(R.dimen.dp_30))).into(holder.imgUserProfile);
			}


			if (position == itemArray.size() - 1) {
				holder.layoutRemoveAllHistory.setVisibility(View.VISIBLE);
			} else {
				holder.layoutRemoveAllHistory.setVisibility(View.GONE);
			}
		}
		/** 검색 */
		else {
			holder.layoutRemoveAllHistory.setVisibility(View.GONE);
			holder.btnRemoveHistory.setVisibility(View.GONE);
			if (!TextUtil.isNull(item.MEMB_NAME)) {
				holder.txtUserName.setText(item.MEMB_NAME);
			}
			if (!TextUtil.isNull(item.STATE_TEXT)) {
				holder.txtUserState.setVisibility(View.VISIBLE);
				holder.txtUserState.setText(item.STATE_TEXT);
			} else {
				holder.txtUserState.setVisibility(View.GONE);
			}
			if (!TextUtil.isNull(item.MEMB_IMG)) {
				Picasso.with(context).load(item.MEMB_IMG)
						.transform(new BitmapCircleResize(context, context.getResources().getDimensionPixelOffset(R.dimen.dp_30))).into(holder.imgUserProfile);
			}
		}

		/** 프로필 뱃지 */
		boolean isSuperGreener = false, isBestGreener = false;
		if (!TextUtils.isEmpty(item.SUPERGREENER_YN) && item.SUPERGREENER_YN.equals(Definitions.YN.YES))
			isSuperGreener = true;
		if (!TextUtils.isEmpty(item.BESTGREENER_YN) && item.BESTGREENER_YN.equals(Definitions.YN.YES))
			isBestGreener = true;
		if (isSuperGreener || isBestGreener) {
			holder.imgBadge.setVisibility(View.VISIBLE);
			if (isSuperGreener && isBestGreener)
				holder.imgBadge.setBackgroundResource(R.drawable.icon_badge_both);
			else if (isSuperGreener)
				holder.imgBadge.setBackgroundResource(R.drawable.icon_badge_sg);
			else if (isBestGreener)
				holder.imgBadge.setBackgroundResource(R.drawable.icon_badge_bestpg);
		} else {
			holder.imgBadge.setVisibility(View.GONE);
		}

	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		public LinearLayout layoutItem;
		public RelativeLayout layoutRemoveAllHistory;
		public ImageView imgUserProfile, imgBadge;
		public TextView txtUserName, txtUserState;
		public ImageButton btnRemoveHistory;

		public ViewHolder(View v) {
			super(v);
			imgBadge = (ImageView) v.findViewById(R.id.img_icon_badge);
			layoutItem = (LinearLayout) v.findViewById(R.id.layout_item);
			layoutRemoveAllHistory = (RelativeLayout) v.findViewById(R.id.layout_item_remove_all_history);
			imgUserProfile = (ImageView) v.findViewById(R.id.img_item_profile);
			txtUserName = (TextView) v.findViewById(R.id.txt_item_name);
			txtUserState = (TextView) v.findViewById(R.id.txt_item_state);
			btnRemoveHistory = (ImageButton) v.findViewById(R.id.btn_item_remove_history);

			layoutItem.setOnClickListener(this);
			btnRemoveHistory.setOnClickListener(this);
			layoutRemoveAllHistory.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (adapterItemClickListener != null)
				adapterItemClickListener.onAdapterItemClick(v, getLayoutPosition());
		}
	}
}
