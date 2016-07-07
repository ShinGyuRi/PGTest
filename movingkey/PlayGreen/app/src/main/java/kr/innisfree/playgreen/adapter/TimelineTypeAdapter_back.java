package kr.innisfree.playgreen.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moyusoft.util.BitmapCircleResize;
import com.moyusoft.util.BitmapHeightResizeForGlide;
import com.moyusoft.util.BitmapWidthResize;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.TextUtil;
import com.moyusoft.util.ToforUtil;
import com.moyusoft.util.UIUtils;
import com.sprylab.android.widget.TextureVideoView;
import com.squareup.picasso.Picasso;
import com.volley.network.dto.NetworkData;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-02-19.
 */
public class TimelineTypeAdapter_back extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	public final int VIEW_TYPE_GRID = 100;
	public final int VIEW_TYPE_LIST = 200;


	private Context context;
	private RecyclerView recyclerView;
	private ArrayList<NetworkData> itemArray;
	private AdapterItemClickListener adapterItemClickListener;

	public TimelineTypeAdapter_back(Context context, AdapterItemClickListener listener, RecyclerView recyclerView) {
		this.context = context;
		this.adapterItemClickListener = listener;
		this.recyclerView = recyclerView;
	}

	public void setItemArray(ArrayList<NetworkData> array) {
		this.itemArray = array;
		notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position) {
		if(recyclerView !=null){
			if(recyclerView.getLayoutManager() instanceof GridLayoutManager){
				return VIEW_TYPE_GRID;
			}else{
				return VIEW_TYPE_LIST;
			}
		}
		return VIEW_TYPE_GRID;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		RecyclerView.ViewHolder vh;
		if(viewType == VIEW_TYPE_LIST){
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline, parent, false);
			vh = new ViewHolder(view);
		}else{
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_grid, parent, false);
			vh = new GridViewHolder(view);
		}
		return vh;
	}

	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
		if (itemArray == null || itemArray.size() <= position) return;
		NetworkData item = itemArray.get(position);
		/**그리드 뷰 */
		if (holder instanceof GridViewHolder) {
			final int height = recyclerView.getMeasuredWidth() / 3;
			holder.itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
			if (!TextUtil.isNull(item.TIMELINE_IMG)) {
				Picasso.with(context).load(item.TIMELINE_IMG).transform(new BitmapWidthResize(ToforUtil.PHONE_W / 3)).into(((GridViewHolder) holder).imgContent);
			}
		}
		/**리스트 뷰 */
		else{
			if (!TextUtil.isNull(item.TIMELINE_MP4_SCENE)) {
				int newWidth, newHeight;
				newWidth = ToforUtil.PHONE_W;
				newHeight = Math.round(((float) newWidth / item.TIMELINE_IMG_WIDTH * item.TIMELINE_IMG_HEIGHT));
				JYLog.D(position + ".TIMELINE_MP4_SCENE::h:" + item.TIMELINE_IMG_HEIGHT + "//newheight:" + newHeight, new Throwable());
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(newWidth, newHeight);
				((ViewHolder) holder).imgMp4Scene.setLayoutParams(layoutParams);
				Glide.with(context).load(item.TIMELINE_MP4_SCENE).bitmapTransform(new BitmapHeightResizeForGlide(context, newHeight)).into(((ViewHolder) holder).imgMp4Scene);
			}

			/***/
			if (!TextUtil.isNull(item.TIMELINE_IMG)) {
				int newWidth, newHeight;
				newWidth = ToforUtil.PHONE_W;
				newHeight = Math.round(((float) newWidth / item.TIMELINE_IMG_WIDTH * item.TIMELINE_IMG_HEIGHT));
				JYLog.D(position + ".TIMELINE_IMG::h:" + item.TIMELINE_IMG_HEIGHT + "//newheight:" + newHeight, new Throwable());
				//Picasso.with(context).load(item.TIMELINE_IMG).transform(new BitmapHeightResize(item.TIMELINE_IMG_HEIGHT)).into(((ViewHolder) holder).imgContent);
				//Picasso.with(context).load(item.TIMELINE_IMG).resize(ToforUtil.PHONE_W, 0).into(((ViewHolder) holder).imgContent);
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(newWidth, newHeight);
				((ViewHolder) holder).imgContent.setLayoutParams(layoutParams);
				Glide.with(context).load(item.TIMELINE_IMG).bitmapTransform(new BitmapHeightResizeForGlide(context, newHeight)).into(((ViewHolder) holder).imgContent);
			}

			if (!TextUtil.isNull(item.TIMELINE_MP4)) {
				((ViewHolder) holder).imgBtnPlay.setVisibility(View.VISIBLE);
				((ViewHolder) holder).imgMp4Scene.setVisibility(View.VISIBLE);
				((ViewHolder) holder).videoView.setVideoPath(item.TIMELINE_MP4);
				handleVideo((ViewHolder) holder);
			} else {
				((ViewHolder) holder).imgBtnPlay.setVisibility(View.GONE);
				((ViewHolder) holder).imgMp4Scene.setVisibility(View.GONE);
			}

			if (!TextUtil.isNull(item.MEMB_IMG)) {
				Picasso.with(context).load(item.MEMB_IMG)
						.error(R.drawable.img_user_null2)
						.transform(new BitmapCircleResize(context, context.getResources().getDimensionPixelOffset(R.dimen.dp_54)))
						.into(((ViewHolder) holder).imgProfile);
			} else {
				((ViewHolder) holder).imgProfile.setImageResource(R.drawable.img_user_null2);
			}

			((ViewHolder) holder).txtName.setText("");
			((ViewHolder) holder).txtRegistInfo.setText("");
			((ViewHolder) holder).txtLikeCount.setText("");
			((ViewHolder) holder).txtContent.setText("");
			((ViewHolder) holder).txtHashtag.setText("");
			((ViewHolder) holder).txtHashtag.setVisibility(View.GONE);
			((ViewHolder) holder).txtLikeCount.setVisibility(View.GONE);

			if (!TextUtil.isNull(item.MEMB_NAME))
				((ViewHolder) holder).txtName.setText(item.MEMB_NAME);

			if (item.REG_DT > 0) {
				((ViewHolder) holder).txtRegistInfo.setText(PlaygreenManager.getTimeStampToDate(item.REG_DT, true));
			}

			if (!TextUtil.isNull(item.LOCATION))
				((ViewHolder) holder).txtRegistInfo.append(" " + item.LOCATION);

			if (item.LIKE_COUNT > 0) {
				((ViewHolder) holder).txtLikeCount.setVisibility(View.VISIBLE);
				((ViewHolder) holder).txtLikeCount.setText(context.getString(R.string.str_like_count, item.LIKE_COUNT));
			}
			if (!TextUtil.isNull(item.FRIEND_YN) && item.FRIEND_YN.equals(Definitions.YN.YES))
				((ViewHolder) holder).btnFollow.setSelected(true);
			else
				((ViewHolder) holder).btnFollow.setSelected(false);
			if (!TextUtil.isNull(item.LIKE_YN) && item.LIKE_YN.equals(Definitions.YN.YES))
				((ViewHolder) holder).imgBtnLike.setImageResource(R.drawable.btn_like1_selected);
			else
				((ViewHolder) holder).imgBtnLike.setImageResource(R.drawable.btn_like1);
			if (!TextUtil.isNull(item.TIMELINE_TEXT))
				((ViewHolder) holder).txtContent.setText(item.TIMELINE_TEXT);

			//TODO 개별 단어 선택 가능하도록 바꿔야함
			if (item.HASHTAG_LIST != null && item.HASHTAG_LIST.size() > 0) {
				((ViewHolder) holder).txtHashtag.setVisibility(View.VISIBLE);
				for (NetworkData hashtagItem : item.HASHTAG_LIST) {
					((ViewHolder) holder).txtHashtag.append("#" + hashtagItem.HASHTAG + " ");
				}
			}
//			if (!TextUtil.isNull(item.TIMELINE_MP4_SCENE)) {
//				//TODO 일단 최대 height를 1000으로 맞춰놈
//				if (item.TIMELINE_IMG_HEIGHT > 1000) {
//					item.TIMELINE_IMG_HEIGHT = 1000;
//				}
//				Picasso.with(context).load(item.TIMELINE_MP4_SCENE).transform(new BitmapHeightResize(item.TIMELINE_IMG_HEIGHT)).into(((ViewHolder) holder).imgMp4Scene);
//			}
//
//			if (!TextUtil.isNull(item.TIMELINE_IMG)) {
//				int newWidth, newHeight;
//				newWidth = ToforUtil.PHONE_W;
//				newHeight = Math.round(((float) newWidth / item.TIMELINE_IMG_WIDTH * item.TIMELINE_IMG_HEIGHT));
//				JYLog.D("h:" + item.TIMELINE_IMG_HEIGHT+ "newheight:" + newHeight, new Throwable());
//				item.TIMELINE_IMG_HEIGHT = newHeight;
//
//				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(newWidth, newHeight);
//				((ViewHolder) holder).imgContent.setLayoutParams(layoutParams);
//
//				Picasso.with(context).load(item.TIMELINE_IMG).transform(new BitmapHeightResize(item.TIMELINE_IMG_HEIGHT)).into(((ViewHolder) holder).imgContent);
//			}
//
//			if (!TextUtil.isNull(item.TIMELINE_MP4)) {
//				((ViewHolder) holder).imgBtnPlay.setVisibility(View.VISIBLE);
//				((ViewHolder) holder).imgMp4Scene.setVisibility(View.VISIBLE);
//				((ViewHolder) holder).videoView.setVideoPath(item.TIMELINE_MP4);
//				handleVideo(((ViewHolder) holder));
//			} else {
//				((ViewHolder) holder).imgBtnPlay.setVisibility(View.GONE);
//				((ViewHolder) holder).imgMp4Scene.setVisibility(View.GONE);
//			}
//
//			if (!TextUtil.isNull(item.MEMB_IMG)) {
//				Picasso.with(context).load(item.MEMB_IMG)
//						.error(R.drawable.img_user_null2)
//						.transform(new BitmapCircleResize(context, context.getResources().getDimensionPixelOffset(R.dimen.dp_54)))
//						.into(((ViewHolder) holder).imgProfile);
//			} else {
//				((ViewHolder) holder).imgProfile.setImageResource(R.drawable.img_user_null2);
//			}
//
//			((ViewHolder) holder).txtName.setText("");
//			((ViewHolder) holder).txtRegistInfo.setText("");
//			((ViewHolder) holder).txtLikeCount.setText("");
//			((ViewHolder) holder).txtContent.setText("");
//			((ViewHolder) holder).txtHashtag.setText("");
//			((ViewHolder) holder).txtHashtag.setVisibility(View.GONE);
//			((ViewHolder) holder).txtLikeCount.setVisibility(View.GONE);
//
//			if (!TextUtil.isNull(item.MEMB_NAME))
//				((ViewHolder) holder).txtName.setText(item.MEMB_NAME);
//
//			if (item.REG_DT > 0) {
//				((ViewHolder) holder).txtRegistInfo.setText(PlaygreenManager.getTimeStampToDate(item.REG_DT, true));
//			}
//
//			if (!TextUtil.isNull(item.LOCATION))
//				((ViewHolder) holder).txtRegistInfo.append(" " + item.LOCATION);
//
//			if (item.LIKE_COUNT > 0) {
//				((ViewHolder) holder).txtLikeCount.setVisibility(View.VISIBLE);
//				((ViewHolder) holder).txtLikeCount.setText(context.getString(R.string.str_like_count, item.LIKE_COUNT));
//			}
//			if (!TextUtil.isNull(item.FRIEND_YN) && item.FRIEND_YN.equals(YN.YES))
//				((ViewHolder) holder).btnFollow.setSelected(true);
//			else
//				((ViewHolder) holder).btnFollow.setSelected(false);
//			if (!TextUtil.isNull(item.LIKE_YN) && item.LIKE_YN.equals(YN.YES))
//				((ViewHolder) holder).imgBtnLike.setImageResource(R.drawable.btn_like1_selected);
//			else
//				((ViewHolder) holder).imgBtnLike.setImageResource(R.drawable.btn_like1);
//			if (!TextUtil.isNull(item.TIMELINE_TEXT))
//				((ViewHolder) holder).txtContent.setText(item.TIMELINE_TEXT);
//
//			//TODO 개별 단어 선택 가능하도록 바꿔야함
//			if (item.HASHTAG_LIST != null && item.HASHTAG_LIST.size() > 0) {
//				((ViewHolder) holder).txtHashtag.setVisibility(View.VISIBLE);
//				for (NetworkData hashtagItem : item.HASHTAG_LIST) {
//					((ViewHolder) holder).txtHashtag.append("#" + hashtagItem.HASHTAG + " ");
//				}
//			}
		}
	}

	public void handleVideo(final ViewHolder vh) {
		vh.videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				//TODO 에러처리
				return true;
			}
		});
		vh.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(final MediaPlayer mp) {
				mp.setVolume(0f, 0f);
				UIUtils.resizeView(vh.videoView, ToforUtil.PHONE_W, vh.imgContent.getHeight());
			}
		});
		vh.videoView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				JYLog.D("onFocusChange : " + hasFocus, new Throwable());
				if (!hasFocus) {
					vh.videoView.pause();
				}
				vh.imgBtnPlay.setVisibility(View.VISIBLE);
			}
		});
		vh.videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
			@Override
			public boolean onInfo(MediaPlayer mp, int what, int extra) {
				JYLog.D("onInfo" + what + " " + extra, new Throwable());
				switch (what) {
					case MediaPlayer.MEDIA_INFO_BUFFERING_END:
						vh.pbVideo.setVisibility(View.INVISIBLE);
						break;
					case MediaPlayer.MEDIA_INFO_BUFFERING_START:
						vh.pbVideo.setVisibility(View.VISIBLE);
						vh.imgBtnPlay.setVisibility(View.GONE);
						break;
				}
				return false;
			}
		});
		vh.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				JYLog.D("complete", new Throwable());
				//vh.imgBtnPlay.setVisibility(View.VISIBLE);
				vh.videoView.requestFocus();
				vh.videoView.seekTo(0);
				vh.videoView.start();
			}
		});
		vh.imgBtnPlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				vh.imgMp4Scene.setVisibility(View.GONE);
				vh.imgBtnPlay.setVisibility(View.GONE);
				vh.videoView.requestFocus();
				vh.videoView.seekTo(0);
				vh.videoView.start();
			}
		});
	}



	@Override
	public void onViewRecycled(RecyclerView.ViewHolder holder) {
		super.onViewRecycled(holder);
		JYLog.D(new Throwable());
		if(holder instanceof  ViewHolder)
			((ViewHolder)holder).pbVideo.setVisibility(View.INVISIBLE);
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
	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
		public ImageView imgProfile, imgContent, imgMp4Scene;
		public TextView txtName, txtRegistInfo, txtLikeCount, txtContent, txtHashtag;
		public Button btnFollow;
		public ImageButton imgBtnLike, imgBtnShare, imgBtnComment, imgBtnEtc, imgBtnPlay;
		public TextureVideoView videoView;
		public ProgressBar pbVideo;

		public ViewHolder(View v) {
			super(v);
			imgProfile = (ImageView) v.findViewById(R.id.img_item_profile);
			imgContent = (ImageView) v.findViewById(R.id.img_item_content);
			imgMp4Scene = (ImageView) v.findViewById(R.id.img_item_mp4_scene);
			txtName = (TextView) v.findViewById(R.id.txt_item_name);
			txtRegistInfo = (TextView) v.findViewById(R.id.txt_item_regist_info);
			txtLikeCount = (TextView) v.findViewById(R.id.txt_item_like_count);
			txtHashtag = (TextView) v.findViewById(R.id.txt_item_hashtag);
			txtContent = (TextView) v.findViewById(R.id.txt_item_content);
			btnFollow = (Button) v.findViewById(R.id.btn_item_follow);
			imgBtnLike = (ImageButton) v.findViewById(R.id.btn_item_like);
			imgBtnShare = (ImageButton) v.findViewById(R.id.btn_item_share);
			imgBtnComment = (ImageButton) v.findViewById(R.id.btn_item_reply);
			imgBtnEtc = (ImageButton) v.findViewById(R.id.btn_item_etc);

			pbVideo = (ProgressBar) v.findViewById(R.id.pb_video_load);
			videoView = (TextureVideoView) v.findViewById(R.id.video_item_content);
			imgBtnPlay = (ImageButton) v.findViewById(R.id.btn_item_play);

//			txtLikeCount.setOnClickListener(this);
//			imgProfile.setOnClickListener(this);
//			btnFollow.setOnClickListener(this);
//			imgBtnLike.setOnClickListener(this);
//			imgBtnComment.setOnClickListener(this);
//			imgBtnShare.setOnClickListener(this);
//			imgBtnEtc.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (adapterItemClickListener != null)
				adapterItemClickListener.onAdapterItemClick(v, getLayoutPosition());
		}
	}


	public class GridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		public ImageView imgContent;

		public GridViewHolder(View v) {
			super(v);
			imgContent = (ImageView) v.findViewById(R.id.img_item_content);
		}

		@Override
		public void onClick(View v) {
			if (adapterItemClickListener != null) {
				adapterItemClickListener.onAdapterItemClick(v, getLayoutPosition());
			}
		}
	}
}