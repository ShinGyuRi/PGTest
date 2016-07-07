package kr.innisfree.playgreen.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moyusoft.util.BitmapCircleResize;
import com.moyusoft.util.BitmapWidthResize;
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
public class AlarmFollowingAdapter extends RecyclerView.Adapter<AlarmFollowingAdapter.ViewHolder> {

	private Context context;
	private ArrayList<NetworkData> itemArray;
	private AdapterItemClickListener adapterItemClickListener;

	public AlarmFollowingAdapter() {
	}

	public AlarmFollowingAdapter(Context context, AdapterItemClickListener listener) {
		this.context = context;
		this.adapterItemClickListener = listener;
	}

	@Override
	public int getItemCount() {
		if (itemArray == null) return 0;
		return itemArray.size();
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
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm_following, parent, false);
		ViewHolder vh = new ViewHolder(view);
		return vh;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		if (itemArray == null || itemArray.size() <= position) return;
		NetworkData item = itemArray.get(position);

		if (!TextUtil.isNull(item.MEMB_IMG)) {
			Picasso.with(context).load(item.MEMB_IMG).error(R.drawable.img_user_null2)
					.transform(new BitmapCircleResize(context, context.getResources().getDimensionPixelOffset(R.dimen.dp_30))).into(holder.imgProfile);
		} else {
			holder.imgProfile.setImageResource(R.drawable.img_user_null2);
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

		/** 팔로잉 소식인경우 사진등록, 댓글등록, 좋아요, 베스트픽선정 알림 수신 */
		if (!TextUtil.isNull(item.INFO_CATEGORY)) {
			makeMessage(holder, item);
		}

		if (item.LIST_LIKE_TIMELINE.size() > 0) {
			holder.txtMessage.setPadding(0, context.getResources().getDimensionPixelOffset(R.dimen.dp_12), 0, 0);
			holder.layoutPictureContent.setVisibility(View.VISIBLE);
			makeFollowingsLikeView(holder, item.LIST_LIKE_TIMELINE, position);
		} else {
			holder.txtMessage.setPadding(0, 0, 0, 0);
			holder.layoutPictureContent.setVisibility(View.GONE);
		}
	}

	public void makeFollowingsLikeView(ViewHolder holder, ArrayList<NetworkData> dataArray, int position) {
		holder.layoutPictureContent.removeAllViews();

		ArrayList<ImageViewHolder> itemViewArray = new ArrayList<ImageViewHolder>();
		for (NetworkData data : dataArray) {
			View itemView = LayoutInflater.from(context).inflate(R.layout.view_simple_image, null);
			ImageViewHolder imageViewHolder = new ImageViewHolder(itemView);
			if (!TextUtil.isNull(data.LIST_LIKE_TIMELINE_MP4_SCENE)){
				imageViewHolder.imagePlayIcon.setVisibility(View.VISIBLE);
				Picasso.with(context).load(data.LIST_LIKE_TIMELINE_MP4_SCENE)
						.transform(new BitmapWidthResize(context.getResources().getDimensionPixelOffset(R.dimen.dp_42)))
						.into(imageViewHolder.imageView);
			}else if (!TextUtil.isNull(data.LIST_LIKE_TIMELINE_IMG)){
				imageViewHolder.imagePlayIcon.setVisibility(View.GONE);
				Picasso.with(context).load(data.LIST_LIKE_TIMELINE_IMG)
						.transform(new BitmapWidthResize(context.getResources().getDimensionPixelOffset(R.dimen.dp_42)))
						.into(imageViewHolder.imageView);
			}
			itemView.setId(R.id.id_item_picture);
			itemView.setTag(data);
			itemView.setOnClickListener(holder);
			itemViewArray.add(imageViewHolder);
		}

		for (ImageViewHolder vh : itemViewArray) {
			holder.layoutPictureContent.addView(vh.itemView);
		}
	}

//	private ArrayList<ArrayList<ImageViewHolder>> itemViewHolderArray;
//	public void makeFollowingsLikeView(ViewHolder holder, ArrayList<NetworkData> dataArray, int position) {
//		holder.layoutPictureContent.removeAllViews();
//
//		if(itemViewHolderArray ==null)
//			itemViewHolderArray = new ArrayList<ArrayList<ImageViewHolder>>();
//
//		if(itemViewHolderArray.size() <= position){
//			ArrayList<ImageViewHolder> itemViewArray = new ArrayList<ImageViewHolder>();
//			for (NetworkData data : dataArray) {
//				View itemView = LayoutInflater.from(context).inflate(R.layout.view_simple_image, null);
//				ImageViewHolder imageViewHolder = new ImageViewHolder(itemView);
//				if (!TextUtil.isNull(data.LIST_LIKE_TIMELINE_IMG))
//					Picasso.with(context).load(data.LIST_LIKE_TIMELINE_IMG)
//							//.transform(new BitmapWidthResize(context.getResources().getDimensionPixelOffset(R.dimen.dp_42)))
//							.into(imageViewHolder.imageView);
//
//				itemView.setId(R.id.id_item_picture);
//				itemView.setTag(data);
//				itemView.setOnClickListener(holder);
//				itemViewArray.add(imageViewHolder);
//			}
//			itemViewHolderArray.add(itemViewArray);
//		}
//
//		if(itemViewHolderArray.size() > position){
//			for (ImageViewHolder vh : itemViewHolderArray.get(position)) {
//				holder.layoutPictureContent.addView(vh.itemView);
//			}
//		}
//	}

	public void makeMessage(final ViewHolder holder, NetworkData item) {
		String ago = "";
		int agoResId;
		switch (item.TERM_UNIT) {
			case "D":
				agoResId = R.string.str_unit_day;
				break;
			case "W":
				agoResId = R.string.str_unit_week;
				break;
			case "H":
				agoResId = R.string.str_unit_hour;
				break;
			case "M":
				agoResId = R.string.str_unit_minute;
				break;
			case "S":
				agoResId = R.string.str_unit_second;
				break;
			default:
				agoResId = -1;
				break;
		}

		if (agoResId > 0)
			ago = context.getString(agoResId, item.TERM);

		StringBuffer message = new StringBuffer();
		message.append(item.INFO_TEXT);
		message.append(" "+ago);

		if (!TextUtil.isNull(item.INFO_TEXT)) {
			SpannableString span = new SpannableString(message);
			if (!TextUtil.isNull(item.MEMB_NAME)) {
				span.setSpan(new StyleSpan(Typeface.BOLD), 0, item.MEMB_NAME.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				span.setSpan(new ClickableSpan() {
					@Override
					public void onClick(View v) {
						holder.imgProfile.performClick();
					}

					@Override
					public void updateDrawState(TextPaint ds) {
					}
				}, 0, item.MEMB_NAME.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
			}
			if (!TextUtil.isNull(ago)) {
				int i = message.indexOf(ago);
				span.setSpan(new ForegroundColorSpan(Color.parseColor("#999999")), i, i + ago.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			holder.txtMessage.setText(span);
			holder.txtMessage.setMovementMethod(LinkMovementMethod.getInstance());
		}

		//		switch (item.INFO_CATEGORY) {
//			case ALARM_CATEGORY.FOLLOW:
//				messageResId = R.string.str_alarm_following_follow_message;
//				break;
//			case ALARM_CATEGORY.REGIST_PICTURE:
//				messageResId = R.string.str_alarm_following_regist_picture_message;
//				break;
//			case ALARM_CATEGORY.LIKE:
//				messageResId = R.string.str_alarm_following_like_message;
//				break;
//			case ALARM_CATEGORY.REGIST_COMMENT:
//				messageResId = R.string.str_alarm_following_regist_comment_message;
//				break;
//			case ALARM_CATEGORY.BESTPIC0K_SELECTION:
//				messageResId = R.string.str_alarm_following_bestpick_message;
//				break;
//			default:
//				messageResId = -1;
//				break;
//		}
	}

	public class ImageViewHolder extends RecyclerView.ViewHolder {
		public ImageView imageView, imagePlayIcon;

		public ImageViewHolder(View v) {
			super(v);
			imageView = (ImageView) v.findViewById(R.id.img_item);
			imagePlayIcon = (ImageView)v.findViewById(R.id.img_item_play);
		}

	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		public LinearLayout layoutItem;
		public GridLayout layoutPictureContent;
		public TextView txtMessage;
		public ImageView imgThumbnail, imgProfile, imgBadge;

		public ViewHolder(View v) {
			super(v);
			imgBadge = (ImageView) v.findViewById(R.id.img_icon_badge);
			layoutItem = (LinearLayout) v.findViewById(R.id.layout_item_country);
			layoutPictureContent = (GridLayout) v.findViewById(R.id.layout_pick_content);
			imgProfile = (ImageView) v.findViewById(R.id.img_item_profile);
			txtMessage = (TextView) v.findViewById(R.id.txt_item_message);
			//imgThumbnail = (ImageView) v.findViewById(R.id.img_item_thumb);
			//imgThumbnail.setOnClickListener(this);
			imgProfile.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (adapterItemClickListener != null)
				adapterItemClickListener.onAdapterItemClick(v, getLayoutPosition());
		}
	}
}
