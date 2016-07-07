package kr.innisfree.playgreen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moyusoft.util.Definitions.YN;
import com.moyusoft.util.TextUtil;
import com.squareup.picasso.Picasso;
import com.volley.network.dto.NetworkData;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-03-11.
 */
public class PG21Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private static final int TYPE_HEADER = 0;
	private static final int TYPE_ITEM = 1;

	private Context context;
	private ArrayList<NetworkData> itemArray;
	private NetworkData headerData;
	private AdapterItemClickListener adapterItemClickListener;

	public PG21Adapter(Context context) {
		this.context = context;
	}

	public void setAdapterItemClickListener(AdapterItemClickListener listener) {
		this.adapterItemClickListener = listener;
	}

	public void setHeaderData(NetworkData data) {
		this.headerData = data;
	}

	public void setItemArray(ArrayList<NetworkData> array) {
		this.itemArray = array;
		notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0)
			return TYPE_HEADER;
		else return TYPE_ITEM;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view;
		RecyclerView.ViewHolder vh;

		if (viewType == TYPE_HEADER) {
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pg21_header, parent, false);
			vh = new HeaderViewHolder(view);
		} else {
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pg21, parent, false);
			vh = new PG21ViewHolder(view);
		}
		return vh;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
		View.OnClickListener clickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (adapterItemClickListener != null)
					adapterItemClickListener.onAdapterItemClick(v, position - 1);
			}
		};
		/**헤더 뷰 */
		if (holder instanceof HeaderViewHolder) {
			if (headerData == null) return;
			makeHeaderView(((HeaderViewHolder) holder));
		}
		/**아이템 뷰 */
		else {
			int realPos = position - 1;
			if (realPos < 0 || itemArray == null || itemArray.size() <= realPos) return;
			NetworkData item = itemArray.get(realPos);
			//Picasso.with(context).load(item.TIMELINE_IMG).into(holder.imgContent);

			((PG21ViewHolder) holder).txtAddMission.setVisibility(View.GONE);
			((PG21ViewHolder) holder).marginView.setVisibility(View.GONE);

			if (item.isTodayMission) {
				((PG21ViewHolder) holder).txtAddMission.setVisibility(View.VISIBLE);
				((PG21ViewHolder) holder).txtMissionName.setText(R.string.str_pg21_today_mission);
				Picasso.with(context).load(R.drawable.img_todaymisson).into(((PG21ViewHolder) holder).imgContent);
				((PG21ViewHolder) holder).txtMissionInfo.setText(PlaygreenManager.getTimeStampToDate(System.currentTimeMillis() / 1000, false));
			} else {
				if (!TextUtil.isNull(item.HIGHLIGHT_IMG)) {
					Picasso.with(context).load(item.HIGHLIGHT_IMG).into(((PG21ViewHolder) holder).imgContent);
				} else if (!TextUtil.isNull(item.PG21_MS_ENTER_IMG)) {
					Picasso.with(context).load(item.PG21_MS_ENTER_IMG).into(((PG21ViewHolder) holder).imgContent);
				}
				if (!TextUtil.isNull(item.PG21_MS_TITLE)) {
					((PG21ViewHolder) holder).txtMissionName.setText(item.PG21_MS_TITLE);
				}
				if (item.REG_DT > 0)
					((PG21ViewHolder) holder).txtMissionInfo.setText(PlaygreenManager.getTimeStampToDate(item.REG_DT, false));
			}

			if(TextUtil.isNull(item.PG21_MS_ENTER_NO)){
				((PG21ViewHolder) holder).txtDay.setText(context.getString(R.string.str_pg21_day, getRealItemCount() - realPos) + " ");
			}else{
				((PG21ViewHolder) holder).txtDay.setText(context.getString(R.string.str_pg21_day, item.PG21_MS_ENTER_NO)+" ");
			}
			((PG21ViewHolder) holder).imgContent.setOnClickListener(clickListener);
			if ((getRealItemCount() - 1) == realPos)
				((PG21ViewHolder) holder).marginView.setVisibility(View.VISIBLE);
		}
	}

	public void makeHeaderView(HeaderViewHolder vh) {
		vh.layoutMissionEnd.setVisibility(View.GONE);
		vh.layoutMissionIng.setVisibility(View.GONE);

		/** pg21미션  */
//		if (headerData.ENTER_COUNT <= 21) {
//			vh.layoutMissionIng.setVisibility(View.VISIBLE);
//			if (headerData.ENTER_COUNT == 21)
//				vh.layoutMissionComplete.setVisibility(View.VISIBLE);
//			else
//				vh.layoutMissionComplete.setVisibility(View.GONE);
//
//			if (!TextUtil.isNull(headerData.EARTHBOX_IMG)) {
//				Picasso.with(context).load(headerData.EARTHBOX_IMG).into(vh.imgReward);
//			}
//			vh.txtPG21Status.setText(Html.fromHtml(context.getString(R.string.str_pg21_progress,
//					PlaygreenManager.getUserInfo().MEMB_NAME, "<br/><font color='#4b9b50'>" + headerData.ENTER_COUNT + "일 째</font>")));
//		}
		vh.layoutMissionIng.setVisibility(View.VISIBLE);

		/** EARTH BOX 응모하기 노출 */
		if (headerData.isMine == false
				|| (!TextUtil.isNull(headerData.ENTER_YN) && headerData.ENTER_YN.equals(YN.YES)))
			vh.layoutMissionComplete.setVisibility(View.GONE);
		else
			vh.layoutMissionComplete.setVisibility(View.VISIBLE);

		if (!TextUtil.isNull(headerData.EARTHBOX_IMG)) {
			Picasso.with(context).load(headerData.EARTHBOX_IMG).into(vh.imgReward);
		}
		vh.txtPG21Status.setText(Html.fromHtml(context.getString(R.string.str_pg21_progress,
				headerData.MEMB_NAME, "<br/><font color='#4b9b50'>" + headerData.ENTER_COUNT + "일 째</font>")));

		/** 도전!슈퍼그리너 */
		if (headerData.ENTER_COUNT > 21) {
			vh.layoutMissionEnd.setVisibility(View.VISIBLE);
			vh.txtChallengePeriod.setText(" | " + context.getString(R.string.str_challenge_period,
					PlaygreenManager.getTimeStampToDate(headerData.START_DT, false),
					PlaygreenManager.getTimeStampToDate(headerData.END_DT, false)));
		}
	}


	/**
	 * 헤더 카운트 포함
	 */
	@Override
	public int getItemCount() {
		if (itemArray == null) {
			return 0 + 1;
		}
		return itemArray.size() + 1;
	}

	public int getRealItemCount() {
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

	public class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		public LinearLayout layoutMissionIng, layoutMissionEnd;

		public LinearLayout layoutPG21Header, layoutMissionComplete;
		public ImageView imgReward;
		public TextView txtPG21Status, txtSubscription;

		public TextView txtChallengePeriod;

		public HeaderViewHolder(View v) {
			super(v);
			layoutMissionIng = (LinearLayout) v.findViewById(R.id.layout_mission_ing);
			layoutMissionEnd = (LinearLayout) v.findViewById(R.id.layout_mission_end);
			txtChallengePeriod = (TextView) v.findViewById(R.id.txt_super_greener_period);
			layoutPG21Header = (LinearLayout) v.findViewById(R.id.layout_pg21_header);
			layoutMissionComplete = (LinearLayout) v.findViewById(R.id.layout_pg21_mission_complete);
			imgReward = (ImageView) v.findViewById(R.id.img_pg21_reward);
			txtSubscription = (TextView) v.findViewById(R.id.txt_earthbox_request);
			txtPG21Status = (TextView) v.findViewById(R.id.txt_pg21_progress);

			txtSubscription.setOnClickListener(this);
			v.findViewById(R.id.btn_test).setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (adapterItemClickListener != null)
				adapterItemClickListener.onAdapterItemClick(v, getLayoutPosition());
		}
	}

}