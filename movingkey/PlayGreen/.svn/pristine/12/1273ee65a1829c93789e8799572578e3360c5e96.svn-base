package kr.innisfree.playgreen.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.net.URLDecoder;
import java.util.ArrayList;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.common.view.ExpandableTextView;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-02-19.
 */
public class Timeline2TypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	public final int VIEW_TYPE_HEADER = 90;
	public final int VIEW_TYPE_GRID = 100;
	public final int VIEW_TYPE_LIST = 200;

	private Context context;
	private RecyclerView recyclerView;
	private ArrayList<NetworkData> itemArray;
	private NetworkData headerData;
	private AdapterItemClickListener adapterItemClickListener;
	private boolean isHeaderExist;
	private SparseBooleanArray mCollapsedStatus;
	private int totalListCount;
	private boolean isClickImgContent = false;


	public Timeline2TypeAdapter(Context context, AdapterItemClickListener listener, RecyclerView recyclerView) {
		this.context = context;
		this.adapterItemClickListener = listener;
		this.recyclerView = recyclerView;
		mCollapsedStatus = new SparseBooleanArray();
	}

	public void setHeaderExist(boolean isHeaderExist) {
		this.isHeaderExist = isHeaderExist;
		//헤더뷰가 있는경우 0번 인덱스(헤더뷰인덱스)에 빈데이터 추가하여 itemArray size 맞춘다
		if (itemArray == null) {
			itemArray = new ArrayList<NetworkData>();
		}
		NetworkData emptyItem = new NetworkData();
		emptyItem.isHeaderView = true;
		itemArray.add(0, emptyItem);
	}

	public void setHeaderData(NetworkData data) {
		this.headerData = data;
		if (totalListCount > 0)
			headerData.TOTAL_COUNT = totalListCount;
		notifyItemChanged(0);
	}

	public void setTotalCount(int totalCount) {
		if (isHeaderExist == false) return;
		this.totalListCount = totalCount;
		if (headerData != null) {
			headerData.TOTAL_COUNT = totalListCount;
			notifyItemChanged(0);
		}
	}

	public void setItemArray(ArrayList<NetworkData> array) {
		if (itemArray == null || itemArray.size() == 0) {
			this.itemArray = array;
		} else {
			itemArray.addAll(array);
		}
		notifyDataSetChanged();
	}

	public void clearItemArray() {
		if (itemArray != null && itemArray.size() > 0) {
			itemArray.clear();
		}
		setHeaderExist(isHeaderExist);
	}

	public void removeItem(NetworkData data) {
		if (itemArray != null) {
			if (itemArray.contains(data)) {
				int index = itemArray.indexOf(data);
				itemArray.remove(index);
				notifyItemRemoved(index);
			}
		}
	}

	public NetworkData getItem(int position) {
		if (itemArray == null || itemArray.size() <= position) return null;
		else return itemArray.get(position);
	}

	@Override
	public int getItemCount() {
		if (itemArray == null) {
			return 0;
		}
		return itemArray.size();
	}

	@Override
	public int getItemViewType(int position) {
		if (isHeaderExist == true && position == 0) {
			return VIEW_TYPE_HEADER;
		} else {
			if (recyclerView != null) {
				if (recyclerView.getLayoutManager() instanceof GridLayoutManager)
					return VIEW_TYPE_GRID;
				else return VIEW_TYPE_LIST;
			}
			return VIEW_TYPE_GRID;
		}
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		RecyclerView.ViewHolder vh;
		View view;
		if (viewType == VIEW_TYPE_HEADER) {
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_detail_header, parent, false);
			vh = new HeaderViewHolder(view);
		} else if (viewType == VIEW_TYPE_LIST) {
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline, parent, false);
			vh = new ViewHolder(view);
		} else {
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_grid, parent, false);
			vh = new GridViewHolder(view);
		}
		return vh;
	}

	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
		/**헤더 뷰 */
		if (position == 0 && isHeaderExist == true && holder instanceof HeaderViewHolder) {
			if (headerData == null) return;
			makeHeaderView(((HeaderViewHolder) holder));
			return;
		}

		if (position < 0 || itemArray == null || itemArray.size() <= position) return;
		final NetworkData item = itemArray.get(position);

		/**그리드 뷰 */
		if (holder instanceof GridViewHolder) {
			((GridViewHolder) holder).imgContent.setVisibility(View.GONE);
			((GridViewHolder) holder).imgPlay.setVisibility(View.GONE);
			final int height = recyclerView.getMeasuredWidth() / 3;
			//holder.itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
//			if (!TextUtil.isNull(item.TIMELINE_MP4_SCENE)) {
//				/** 시네마 그래프일 때, **/
//				((GridViewHolder) holder).imgContent.setVisibility(View.VISIBLE);
//				((GridViewHolder) holder).imgPlay.setVisibility(View.VISIBLE);
////				Picasso.with(context).load(item.TIMELINE_MP4_SCENE).transform(new BitmapWidthResize(ToforUtil.PHONE_W / 3)).fit().centerCrop().into(((GridViewHolder) holder).imgContent);
//			} else if (!TextUtil.isNull(item.TIMELINE_IMG)) {
//				/** 이미지일 때, **/
//				//JYLog.D("pos:" + realPos + "," + item.TIMELINE_IMG , new Throwable());
//				((GridViewHolder) holder).imgContent.setVisibility(View.VISIBLE);
////				Picasso.with(context).load(item.TIMELINE_IMG).transform(new BitmapWidthResize(ToforUtil.PHONE_W / 3)).into(((GridViewHolder) holder).imgContent);
//			}
			if (!TextUtil.isNull(item.TIMELINE_MP4)) {
				/** 시네마 그래프일 때, **/
				((GridViewHolder) holder).imgContent.setVisibility(View.VISIBLE);
				((GridViewHolder) holder).imgPlay.setVisibility(View.VISIBLE);
//				Picasso.with(context).load(item.TIMELINE_MP4_SCENE).transform(new BitmapWidthResize(ToforUtil.PHONE_W / 3)).fit().centerCrop().into(((GridViewHolder) holder).imgContent);
			} else if (!TextUtil.isNull(item.TIMELINE_IMG)) {
				/** 이미지일 때, **/
				//JYLog.D("pos:" + realPos + "," + item.TIMELINE_IMG , new Throwable());
				((GridViewHolder) holder).imgContent.setVisibility(View.VISIBLE);
//				Picasso.with(context).load(item.TIMELINE_IMG).transform(new BitmapWidthResize(ToforUtil.PHONE_W / 3)).into(((GridViewHolder) holder).imgContent);
			}
			Picasso.with(context).load(item.TIMELINE_IMG).transform(new BitmapWidthResize(ToforUtil.PHONE_W / 3)).into(((GridViewHolder) holder).imgContent);
		}
		/**리스트 뷰 */
		else {
//			if (!TextUtil.isNull(item.TIMELINE_MP4_SCENE)) {
//				int newWidth, newHeight;
//				newWidth = ToforUtil.PHONE_W;
//				newHeight = Math.round(((float) newWidth / item.TIMELINE_IMG_WIDTH * item.TIMELINE_IMG_HEIGHT));
//				JYLog.D(position + ".TIMELINE_MP4_SCENE::h:" + item.TIMELINE_IMG_HEIGHT + "//newheight:" + newHeight, new Throwable());
//				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(newWidth, newHeight);
//				((ViewHolder) holder).imgMp4Scene.setLayoutParams(layoutParams);
//				Glide.with(context).load(item.TIMELINE_MP4_SCENE).bitmapTransform(new BitmapHeightResizeForGlide(context, newHeight)).into(((ViewHolder) holder).imgMp4Scene);
//			}
//
//			if (!TextUtil.isNull(item.TIMELINE_IMG)) {
//				int newWidth, newHeight;
//				newWidth = ToforUtil.PHONE_W;
//				newHeight = Math.round(((float) newWidth / item.TIMELINE_IMG_WIDTH * item.TIMELINE_IMG_HEIGHT));
//				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(newWidth, newHeight);
//				((ViewHolder) holder).imgContent.setLayoutParams(layoutParams);
//				Glide.with(context).load(item.TIMELINE_IMG).bitmapTransform(new BitmapHeightResizeForGlide(context, newHeight)).into(((ViewHolder) holder).imgContent);
//			}

//			if (!TextUtil.isNull(item.TIMELINE_MP4)) {
//				((ViewHolder) holder).imgBtnPlay.setVisibility(View.VISIBLE);
//				((ViewHolder) holder).imgMp4Scene.setVisibility(View.VISIBLE);
//				((ViewHolder) holder).videoView.setVisibility(View.VISIBLE);
//				((ViewHolder) holder).videoView.setVideoPath(item.TIMELINE_MP4);
//				if (Build.VERSION.SDK_INT <= 17) {
//					((ViewHolder) holder).videoView.setRotation(90);
//				}
//				handleVideo((ViewHolder) holder);
//			} else {
//				((ViewHolder) holder).imgBtnPlay.setVisibility(View.GONE);
//				((ViewHolder) holder).imgMp4Scene.setVisibility(View.GONE);
//				((ViewHolder) holder).videoView.setVisibility(View.GONE);
//			}

			if (!TextUtil.isNull(item.TIMELINE_MP4)) {
				/** 시네마그래프일 때, **/
				if (!TextUtil.isNull(item.TIMELINE_IMG)) {
					int newWidth, newHeight;
					newWidth = ToforUtil.PHONE_W;
					newHeight = Math.round(((float) newWidth / item.TIMELINE_IMG_WIDTH * item.TIMELINE_IMG_HEIGHT));
					RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(newWidth, newHeight);
					((ViewHolder) holder).imgMp4Scene.setLayoutParams(layoutParams);
					Glide.with(context).load(item.TIMELINE_IMG).bitmapTransform(new BitmapHeightResizeForGlide(context, newHeight)).into(((ViewHolder) holder).imgMp4Scene);
				}
				if (!TextUtil.isNull(item.TIMELINE_MP4_SCENE)) {
					int newWidth, newHeight;
					newWidth = ToforUtil.PHONE_W;
					newHeight = Math.round(((float) newWidth / item.TIMELINE_IMG_WIDTH * item.TIMELINE_IMG_HEIGHT));
					JYLog.D(position + ".TIMELINE_MP4_SCENE::h:" + item.TIMELINE_IMG_HEIGHT + "//newheight:" + newHeight, new Throwable());
					RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(newWidth, newHeight);
					((ViewHolder) holder).imgContent.setLayoutParams(layoutParams);
					Glide.with(context).load(item.TIMELINE_MP4_SCENE).bitmapTransform(new BitmapHeightResizeForGlide(context, newHeight)).into(((ViewHolder) holder).imgContent);
//                Glide.with(context).load(item.TIMELINE_MP4_SCENE).bitmapTransform(new BitmapHeightResizeForGlide(context, newHeight)).into(holder.imgMp4Scene);
				}
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						((ViewHolder) holder).imgBtnPlay.setVisibility(View.VISIBLE);
						((ViewHolder) holder).imgMp4Scene.setVisibility(View.VISIBLE);
						((ViewHolder) holder).videoView.setVisibility(View.VISIBLE);
						((ViewHolder) holder).videoView.setVideoPath(item.TIMELINE_MP4);
					}
				}, 200);
				if (Build.VERSION.SDK_INT <= 17) {
					((ViewHolder) holder).videoView.setRotation(90);
				}
				handleVideo(((ViewHolder) holder));
			} else {
				/** 이미지일 때, **/
				((ViewHolder) holder).imgBtnPlay.setVisibility(View.GONE);
				((ViewHolder) holder).imgMp4Scene.setVisibility(View.GONE);
				((ViewHolder) holder).videoView.setVisibility(View.GONE);
				if (!TextUtil.isNull(item.TIMELINE_IMG)) {
					int newWidth, newHeight;
					newWidth = ToforUtil.PHONE_W;
					newHeight = Math.round(((float) newWidth / item.TIMELINE_IMG_WIDTH * item.TIMELINE_IMG_HEIGHT));
					RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(newWidth, newHeight);
					((ViewHolder) holder).imgContent.setLayoutParams(layoutParams);
					Glide.with(context).load(item.TIMELINE_IMG).bitmapTransform(new BitmapHeightResizeForGlide(context, newHeight)).into(((ViewHolder) holder).imgContent);
				}
			}

			if (!TextUtil.isNull(item.MEMB_IMG)) {
				Picasso.with(context).load(item.MEMB_IMG)
						.error(R.drawable.img_user_null2)
						.transform(new BitmapCircleResize(context, context.getResources().getDimensionPixelOffset(R.dimen.dp_54)))
						.into(((ViewHolder) holder).imgProfile);
			} else {
				((ViewHolder) holder).imgProfile.setImageResource(R.drawable.img_user_null2);
			}

			/** 프로필 뱃지 */
			boolean isSuperGreener = false, isBestGreener = false;
			if (!TextUtils.isEmpty(item.SUPERGREENER_YN) && item.SUPERGREENER_YN.equals(Definitions.YN.YES))
				isSuperGreener = true;
			if (!TextUtils.isEmpty(item.BESTGREENER_YN) && item.BESTGREENER_YN.equals(Definitions.YN.YES))
				isBestGreener = true;
			if (isSuperGreener || isBestGreener) {
				((ViewHolder) holder).imgBadge.setVisibility(View.VISIBLE);
				if (isSuperGreener && isBestGreener)
					((ViewHolder) holder).imgBadge.setBackgroundResource(R.drawable.icon_badge_both);
				else if (isSuperGreener)
					((ViewHolder) holder).imgBadge.setBackgroundResource(R.drawable.icon_badge_sg);
				else if (isBestGreener)
					((ViewHolder) holder).imgBadge.setBackgroundResource(R.drawable.icon_badge_bestpg);
			} else {
				((ViewHolder) holder).imgBadge.setVisibility(View.GONE);
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

			if (!TextUtil.isNull(item.OWN_YN) && item.OWN_YN.equals(Definitions.YN.YES)) {
				((ViewHolder) holder).btnFollow.setVisibility(View.GONE);
			} else {
				((ViewHolder) holder).btnFollow.setVisibility(View.VISIBLE);
				if (!TextUtil.isNull(item.FRIEND_YN) && item.FRIEND_YN.equals(Definitions.YN.YES))
					((ViewHolder) holder).btnFollow.setSelected(true);
				else
					((ViewHolder) holder).btnFollow.setSelected(false);
			}

			if (!TextUtil.isNull(item.LIKE_YN) && item.LIKE_YN.equals(Definitions.YN.YES))
				((ViewHolder) holder).imgBtnLike.setImageResource(R.drawable.btn_like1_selected);
			else
				((ViewHolder) holder).imgBtnLike.setImageResource(R.drawable.btn_like1);

			/** TIMELINIE  CONTENT */
			if (!TextUtil.isNull(item.TIMELINE_TEXT)) {
				String timelineContent = "";
				try {
					timelineContent = URLDecoder.decode(item.TIMELINE_TEXT, "UTF-8");
				} catch (Exception e) {
					e.printStackTrace();
				}
				((ViewHolder) holder).txtContent.setText(timelineContent, mCollapsedStatus, position);
			}

			/** HASH TAG */
			if (item.HASHTAG_LIST != null && item.HASHTAG_LIST.size() > 0) {
				StringBuffer hashtag = new StringBuffer();
				((ViewHolder) holder).txtHashtag.setVisibility(View.VISIBLE);
				for (NetworkData hashtagItem : item.HASHTAG_LIST) {
					hashtag.append("#" + hashtagItem.HASHTAG + " ");
				}
				/**해시태그 각각 선택할수있게 Spannable Click */
				SpannableString span = new SpannableString(hashtag.toString());
				for (final NetworkData hashtagItem : item.HASHTAG_LIST) {
					int startIndex = hashtag.toString().indexOf(hashtagItem.HASHTAG);
					span.setSpan(new ClickableSpan() {
						@Override
						public void onClick(View v) {
							v.setTag(hashtagItem);
							if (adapterItemClickListener != null)
								adapterItemClickListener.onAdapterItemClick(v, position);
						}

						@Override
						public void updateDrawState(TextPaint ds) {
						}
					}, startIndex, startIndex + hashtagItem.HASHTAG.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
				}
				((ViewHolder) holder).txtHashtag.setText(span);
				((ViewHolder) holder).txtHashtag.setMovementMethod(LinkMovementMethod.getInstance());
			}
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
				JYLog.D("TimeLineAdapter::setOnPreparedListener::", new Throwable());
				JYLog.D("TimeLineAdapter::setOnPreparedListener::getRotation::" + vh.videoView.getRotation(), new Throwable());
				JYLog.D("TimeLineAdapter::setOnPreparedListener::getRotation::" + Build.VERSION.SDK_INT, new Throwable());
				mp.setVolume(0f, 0f);
				if (Build.VERSION.SDK_INT > 17) {
					UIUtils.resizeView(vh.videoView, ToforUtil.PHONE_W, vh.imgContent.getHeight());
				}
			}
		});
		vh.videoView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					vh.videoView.pause();
					vh.imgBtnPlay.setVisibility(View.VISIBLE);
					vh.imgMp4Scene.setVisibility(View.VISIBLE);
					isClickImgContent = false;
				}
			}
		});
		vh.videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
			@Override
			public boolean onInfo(MediaPlayer mp, int what, int extra) {
				JYLog.D("onInfo::what::" + what + "::extra::" + extra, new Throwable());
				switch (what) {
					case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								if (vh.videoView.isPlaying()) {
									vh.pbVideo.setVisibility(View.INVISIBLE);
									vh.imgMp4Scene.setVisibility(View.GONE);
								}
							}
						}, 30);
						break;
					case MediaPlayer.MEDIA_INFO_BUFFERING_START:
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								if (!vh.videoView.isPlaying()) {
//									vh.imgBtnPlay.setVisibility(View.GONE);
									vh.imgMp4Scene.setVisibility(View.VISIBLE);
									vh.pbVideo.setVisibility(View.VISIBLE);
								}
							}
						}, 10);
						break;
					case MediaPlayer.MEDIA_INFO_BUFFERING_END:
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								if (!vh.videoView.isPlaying()) {
									vh.pbVideo.setVisibility(View.GONE);
								}
							}
						}, 10);
						break;
				}
				return true;
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
		vh.imgContent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (vh.videoView.isPlaying()) {
					vh.videoView.pause();
					vh.imgBtnPlay.setVisibility(View.VISIBLE);
					vh.imgMp4Scene.setVisibility(View.VISIBLE);
					isClickImgContent = true;
				}
			}
		});
		vh.imgBtnPlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				vh.imgBtnPlay.setVisibility(View.GONE);
				vh.videoView.requestFocus();
				vh.videoView.start();

				vh.imgBtnPlay.setVisibility(View.GONE);

				if (isClickImgContent) {
					vh.imgMp4Scene.setVisibility(View.GONE);
					isClickImgContent = false;
				} else {
					new Handler().post(new Runnable() {
						@Override
						public void run() {
							vh.imgMp4Scene.setVisibility(View.VISIBLE);
							vh.pbVideo.setVisibility(View.VISIBLE);
						}
					});
				}

			}
		});
	}


	@Override
	public void onViewRecycled(RecyclerView.ViewHolder holder) {
		super.onViewRecycled(holder);
		if (holder instanceof GridViewHolder) {
			((GridViewHolder) holder).imgContent.setVisibility(View.GONE);
		}
		if (holder instanceof ViewHolder)
			((ViewHolder) holder).pbVideo.setVisibility(View.INVISIBLE);
	}

	public void makeHeaderView(HeaderViewHolder vh) {
		if (headerData.isMyProfile) {
			vh.txtPlaygreen.setText(R.string.str_my_playgreen);
			vh.btnProfileEdit.setVisibility(View.VISIBLE);
		} else {
			vh.txtPlaygreen.setText(R.string.str_playgreen_ko);
			vh.btnProfileEdit.setVisibility(View.GONE);
		}
		if (!TextUtil.isNull(headerData.MEMB_NAME)) {
			vh.txtName.setText(headerData.MEMB_NAME);
		}
		if (!TextUtil.isNull(headerData.STATE_TEXT)) {
			vh.txtIntroduce.setText(headerData.STATE_TEXT);
		}
		if (!TextUtil.isNull(headerData.MEMB_IMG)) {
			JYLog.D(headerData.MEMB_IMG, new Throwable());
			Picasso.with(context).load(headerData.MEMB_IMG)
					.transform(new BitmapCircleResize(context, context.getResources().getDimensionPixelOffset(R.dimen.dp_80))).into(vh.imgProfile);
		}

		/** 프로필 뱃지 */
		boolean isSuperGreener = false, isBestGreener = false;
		if(!TextUtils.isEmpty(headerData.SUPERGREENER_YN) && headerData.SUPERGREENER_YN.equals(Definitions.YN.YES))
			isSuperGreener = true;
		if(!TextUtils.isEmpty(headerData.BESTGREENER_YN) && headerData.BESTGREENER_YN.equals(Definitions.YN.YES))
			isBestGreener = true;
		if(isSuperGreener || isBestGreener){
			vh.imgProfileBadge.setVisibility(View.VISIBLE);
			if(isSuperGreener && isBestGreener)
				vh.imgProfileBadge.setBackgroundResource(R.drawable.icon_badge_both);
			else if(isSuperGreener)
				vh.imgProfileBadge.setBackgroundResource(R.drawable.icon_badge_sg);
			else if(isBestGreener)
				vh.imgProfileBadge.setBackgroundResource(R.drawable.icon_badge_bestpg);
		}else{
			vh.imgProfileBadge.setVisibility(View.GONE);
		}


		vh.txtFollowerCount.setText(headerData.FOLLOWER_COUNT + "");
		vh.txtFollowingCount.setText(headerData.FOLLOWING_COUNT + "");
		vh.txtLikeCount.setText(headerData.LIKE_COUNT + "");
		vh.txtPG21Day.setText(headerData.PG21_DAY + "day");
		vh.txtPGPoint.setText(TextUtil.makeStringWithComma(headerData.POINT + "", false) + "p");

		vh.txtListCount.setText(headerData.TOTAL_COUNT + "");

		if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
			vh.btnShowGrid.setSelected(true);
			vh.btnShowList.setSelected(false);
		} else {
			vh.btnShowGrid.setSelected(false);
			vh.btnShowList.setSelected(true);
		}
	}

	/***********************************************************************
	 * View holder
	 * **********************************************************************
	 */
	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		public ImageView imgProfile, imgContent, imgMp4Scene, imgBadge;
		public TextView txtName, txtRegistInfo, txtLikeCount, txtHashtag;
		public Button btnFollow;
		public ImageButton imgBtnLike, imgBtnShare, imgBtnComment, imgBtnEtc, imgBtnPlay;
		public TextureVideoView videoView;
		public ProgressBar pbVideo;

		public ExpandableTextView txtContent;

		public ViewHolder(View v) {
			super(v);
			imgBadge = (ImageView) v.findViewById(R.id.img_icon_badge);
			imgProfile = (ImageView) v.findViewById(R.id.img_item_profile);
			imgContent = (ImageView) v.findViewById(R.id.img_item_content);
			imgMp4Scene = (ImageView) v.findViewById(R.id.img_item_mp4_scene);
			txtName = (TextView) v.findViewById(R.id.txt_item_name);
			txtRegistInfo = (TextView) v.findViewById(R.id.txt_item_regist_info);
			txtLikeCount = (TextView) v.findViewById(R.id.txt_item_like_count);
			txtHashtag = (TextView) v.findViewById(R.id.txt_item_hashtag);
			//txtContent = (TextView) v.findViewById(R.id.txt_item_content);
			btnFollow = (Button) v.findViewById(R.id.btn_item_follow);
			imgBtnLike = (ImageButton) v.findViewById(R.id.btn_item_like);
			imgBtnShare = (ImageButton) v.findViewById(R.id.btn_item_share);
			imgBtnComment = (ImageButton) v.findViewById(R.id.btn_item_reply);
			imgBtnEtc = (ImageButton) v.findViewById(R.id.btn_item_etc);

			pbVideo = (ProgressBar) v.findViewById(R.id.pb_video_load);
			videoView = (TextureVideoView) v.findViewById(R.id.video_item_content);
			imgBtnPlay = (ImageButton) v.findViewById(R.id.btn_item_play);

			txtContent = (ExpandableTextView) v.findViewById(R.id.txt_item_content);

			txtLikeCount.setOnClickListener(this);
			imgProfile.setOnClickListener(this);
			btnFollow.setOnClickListener(this);
			imgBtnLike.setOnClickListener(this);
			imgBtnComment.setOnClickListener(this);
			imgBtnShare.setOnClickListener(this);
			imgBtnEtc.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (adapterItemClickListener != null)
				adapterItemClickListener.onAdapterItemClick(v, getLayoutPosition());
		}
	}


	public class GridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		public ImageView imgContent, imgPlay;

		public GridViewHolder(View v) {
			super(v);
			imgContent = (ImageView) v.findViewById(R.id.img_item_content);
			imgPlay = (ImageView) v.findViewById(R.id.img_item_play);
			imgContent.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (adapterItemClickListener != null) {
				JYLog.D("position : " + getLayoutPosition() + " " + isHeaderExist, new Throwable());
				adapterItemClickListener.onAdapterItemClick(v, getLayoutPosition());
			}
		}
	}

	public class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private TextView txtName, txtIntroduce, txtFollowerCount, txtFollowingCount, txtLikeCount, txtPG21Day, txtPGPoint, txtListCount, txtPlaygreen;
		private ImageView imgProfile, imgProfileBadge;
		private Button btnShowGrid, btnShowList, btnProfileEdit;

		public HeaderViewHolder(View v) {
			super(v);
			imgProfileBadge = (ImageView) v.findViewById(R.id.img_profile_badge);
			txtName = (TextView) v.findViewById(R.id.txt_name);
			txtIntroduce = (TextView) v.findViewById(R.id.txt_introduce);
			txtFollowerCount = (TextView) v.findViewById(R.id.txt_follower_count);
			txtFollowingCount = (TextView) v.findViewById(R.id.txt_following_count);
			txtLikeCount = (TextView) v.findViewById(R.id.txt_like_count);
			txtPG21Day = (TextView) v.findViewById(R.id.txt_pg21_day);
			txtPGPoint = (TextView) v.findViewById(R.id.txt_pg_point);
			txtListCount = (TextView) v.findViewById(R.id.txt_list_count);
			imgProfile = (ImageView) v.findViewById(R.id.img_profile);
			btnShowGrid = (Button) v.findViewById(R.id.btn_show_grid);
			btnShowList = (Button) v.findViewById(R.id.btn_show_list);
			btnProfileEdit = (Button) v.findViewById(R.id.btn_profile_img_edit);
			txtPlaygreen = (TextView) v.findViewById(R.id.txt_playgreen);

			btnShowGrid.setOnClickListener(this);
			btnShowList.setOnClickListener(this);
			//btnShowGrid.setSelected(true);

			v.findViewById(R.id.btn_profile_img_edit).setOnClickListener(this);
			v.findViewById(R.id.layout_follower).setOnClickListener(this);
			v.findViewById(R.id.layout_following).setOnClickListener(this);
			v.findViewById(R.id.layout_like).setOnClickListener(this);
			v.findViewById(R.id.layout_pg21).setOnClickListener(this);
			v.findViewById(R.id.layout_pg_point).setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btn_show_list || v.getId() == R.id.btn_show_grid) {
				JYLog.D(v.isSelected() + " ???? ", new Throwable());
				if (v.isSelected()) return;
				else {
					btnShowGrid.setSelected(false);
					btnShowList.setSelected(false);
					v.setSelected(true);
				}
			}
			if (adapterItemClickListener != null) {
				adapterItemClickListener.onAdapterItemClick(v, getLayoutPosition());
			}
		}
	}
}