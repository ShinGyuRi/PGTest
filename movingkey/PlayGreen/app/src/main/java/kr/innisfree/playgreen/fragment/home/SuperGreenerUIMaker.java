package kr.innisfree.playgreen.fragment.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moyusoft.util.BitmapCircleResize;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.TextUtil;
import com.squareup.picasso.Picasso;
import com.volley.network.dto.NetworkData;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;

/**
 * Created by jooyoung on 2016-03-22.
 */
public class SuperGreenerUIMaker {

	private Context context;
	private View view;
	private View.OnClickListener onClickListener;
	private ArrayList<NetworkData> dataArray;
	private ArrayList<View> itemViewArray;

	private LinearLayout layoutOpenMore, layoutTooltip, layoutRankContent;
	private ImageButton btnInfo;
	private TextView txtOpenMore, txtTooltipTitle, txtTooltipContent;

	private boolean isOpenMore;

	public SuperGreenerUIMaker(Context context,  View view, View.OnClickListener listener){
		this.view = view;
		this.context = context;
		this.onClickListener = listener;
		init();
	}

	public void init(){
		layoutOpenMore = (LinearLayout)view.findViewById(R.id.layout_supergreener_more);
		txtOpenMore = (TextView)view.findViewById(R.id.txt_supergreener_more);
		btnInfo = (ImageButton)view.findViewById(R.id.btn_supergreener_info);
		layoutTooltip = (LinearLayout)view.findViewById(R.id.layout_tooltip);
		txtTooltipTitle = (TextView)view.findViewById(R.id.txt_supergreener_tooltip_title);
		txtTooltipContent = (TextView)view.findViewById(R.id.txt_supergreener_tooltip_content);
		layoutRankContent = (LinearLayout)view.findViewById(R.id.layout_supergreener_content);

		btnInfo.setOnClickListener(onClickListener);
		layoutOpenMore.setOnClickListener(onClickListener);
	}

	public void setData(ArrayList<NetworkData> datas){
		this.dataArray = datas;
		initializeView();
	}

	public void initializeView(){
		isOpenMore = false;
		makeLankView(false);
	}


	public void makeLankView(boolean isToggle){
		if(isToggle)	isOpenMore = !isOpenMore;

		layoutRankContent.removeAllViews();

		for(int i=1; i<6; i++ ){
			ArrayList<NetworkData> rankArray = getRankGroup(i);
			if(rankArray == null) continue;

			View itemScrollView = LayoutInflater.from(context).inflate(R.layout.view_pick_supergreener_scroll, null);
			ScrollViewHolder scrollViewHolder = new ScrollViewHolder(itemScrollView);
			scrollViewHolder.layoutSuperGreenerItem.removeAllViews();
			scrollViewHolder.txtRank.setText(context.getString(R.string.str_rank, i));
			if(rankArray.size()>0)
				scrollViewHolder.txtCount.setText("#"+ rankArray.get(0).ENTER_COUNT);
			for(NetworkData data: rankArray){
				View itemView = LayoutInflater.from(context).inflate(R.layout.view_pick_supergreener_item, null);
				ItemViewHolder vh = new ItemViewHolder(itemView);
				if(!TextUtil.isNull(data.MEMB_NAME))
					vh.txtName.setText(data.MEMB_NAME);
				if(!TextUtil.isNull(data.MEMB_IMG))
					Picasso.with(context).load(data.MEMB_IMG).error(R.drawable.img_user_null2)
							.transform(new BitmapCircleResize(context, context.getResources().getDimensionPixelOffset(R.dimen.dp_66))).into(vh.imgProfile);

				/** 프로필 뱃지 */
				boolean isSuperGreener = false, isBestGreener = false;
				if(!TextUtils.isEmpty(data.SUPERGREENER_YN) && data.SUPERGREENER_YN.equals(Definitions.YN.YES))
					isSuperGreener = true;
				if(!TextUtils.isEmpty(data.BESTGREENER_YN) && data.BESTGREENER_YN.equals(Definitions.YN.YES))
					isBestGreener = true;
				if(isSuperGreener || isBestGreener){
					vh.imgBadge.setVisibility(View.VISIBLE);
					if(isSuperGreener && isBestGreener)
						vh.imgBadge.setBackgroundResource(R.drawable.icon_badge_both);
					else if(isSuperGreener)
						vh.imgBadge.setBackgroundResource(R.drawable.icon_badge_sg);
					else if(isBestGreener)
						vh.imgBadge.setBackgroundResource(R.drawable.icon_badge_bestpg);
				}else{
					vh.imgBadge.setVisibility(View.GONE);
				}

				itemView.setId(R.id.id_supergreener_item);
				itemView.setTag(data);
				itemView.setOnClickListener(onClickListener);
				scrollViewHolder.layoutSuperGreenerItem.addView(itemView);
			}

			layoutRankContent.addView(itemScrollView);

			if(isOpenMore==false)	 break;
		}

		if(isOpenMore){
			txtOpenMore.setText(R.string.str_close);
			txtOpenMore.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_fold1,0);
		}else{
			txtOpenMore.setText(R.string.str_open_all_ranker);
			txtOpenMore.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_unfold1,0);
		}
	}

	public ArrayList<NetworkData> getRankGroup(int rank){
		ArrayList<NetworkData> sameRankArray = new ArrayList<NetworkData>();
		for(NetworkData data:dataArray){
			if(data.SUPERGREENER_RANK == rank){
				sameRankArray.add(data);
			}
		}
		if(sameRankArray==null || sameRankArray.size()==0)
			return null;
		return sameRankArray;
	}

	public void toggleTooltip(){
		if(layoutTooltip.getVisibility() == View.VISIBLE){
			layoutTooltip.setVisibility(View.GONE);
		}else{
			layoutTooltip.setVisibility(View.VISIBLE);
		}
	}

	public class ScrollViewHolder extends RecyclerView.ViewHolder {
		public TextView txtRank, txtCount;
		public LinearLayout layoutSuperGreenerItem;

		public ScrollViewHolder(View v) {
			super(v);
			txtRank = (TextView)v.findViewById(R.id.txt_supergreener_rank);
			txtCount = (TextView)v.findViewById(R.id.txt_supergreener_count);
			layoutSuperGreenerItem = (LinearLayout)v.findViewById(R.id.layout_supergreener_item);
		}
	}

	public class ItemViewHolder extends RecyclerView.ViewHolder {
		public TextView txtName;
		public ImageView imgProfile;
		public ImageView imgBadge;

		public ItemViewHolder(View v) {
			super(v);
			txtName = (TextView)v.findViewById(R.id.txt_supergreener_name);
			imgBadge = (ImageView)v.findViewById(R.id.img_supergreener_badge);
			imgProfile = (ImageView)v.findViewById(R.id.img_supergreener_profile);
		}
	}

}
