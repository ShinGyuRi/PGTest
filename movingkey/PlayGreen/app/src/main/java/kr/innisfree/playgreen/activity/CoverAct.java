package kr.innisfree.playgreen.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ParentAct;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.DeviceUtil;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.PrefUtil;
import com.moyusoft.util.TextUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.NetworkConstantUtil.URLS;
import com.volley.network.NetworkController;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.common.PlaygreenManager;

/**
 * Created by jooyoung on 2016-02-16.
 */
public class CoverAct extends ParentAct implements View.OnClickListener{

	private NetworkData netResultData;
	private LinearLayout layoutBg;
	private ImageView imgBg;
	private Button btnStart;
	private TextView txtCoverTitle, txtCoverHashtag, txtBottom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLoading(this);
		setContentView(R.layout.act_cover);

		layoutBg = (LinearLayout)findViewById(R.id.layout_bg);
		if (Build.VERSION.SDK_INT >= 21) {
			layoutBg.setPadding(0, DeviceUtil.getStatusBarHeight(this), 0, 0);
		}
		imgBg = (ImageView)findViewById(R.id.img_bg);

		txtCoverTitle = (TextView)findViewById(R.id.txt_cover_title);
		txtCoverHashtag = (TextView)findViewById(R.id.txt_cover_hashtag);
		txtBottom = (TextView)findViewById(R.id.txt_cover_bottom);
		btnStart= (Button)findViewById(R.id.btn_start);
		findViewById(R.id.btn_start).setOnClickListener(this);

		requestCover();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		String today = simpleDateFormat.format(new Date());
		JYLog.D(today,new Throwable());
		PrefUtil.getInstance().putPreference(Definitions.PREFKEY.COVER_PAGE_VIEW_DATE_STR, today);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.btn_start:
				finish();
				break;
		};
	}

	@Override
	public void onUIRefresh() {
		super.onUIRefresh();
		if(netResultData==null)	return;
		if(!TextUtil.isNull(netResultData.COVER_IMG)){
			Picasso.with(this).load(netResultData.COVER_IMG).into(target);
		}
		if(!TextUtil.isNull(netResultData.COVER_TITLE))
			txtCoverTitle.setText(netResultData.COVER_TITLE);
		if(!TextUtil.isNull(netResultData.COVER_HASHTAG))
			txtCoverHashtag.setText(netResultData.COVER_HASHTAG);
//		if(!TextUtil.isNull(netResultData.COVER_MEMB_NAME))
//			txtBottom.append(netResultData.COVER_MEMB_NAME + " | ");
//		if(!TextUtil.isNull(netResultData.COVER_DT))
//			txtBottom.append(PlaygreenManager.getTimeStampToDate(Long.parseLong(netResultData.COVER_DT), false));
		if(!TextUtil.isNull(netResultData.COVER_TEXT))
			txtBottom.setText(netResultData.COVER_TEXT);
	}

	public Target target = new Target() {
		@Override
		public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
			imgBg.setImageBitmap(bitmap);
			btnStart.setVisibility(View.VISIBLE);
		}
		@Override
		public void onBitmapFailed(Drawable errorDrawable) {
			btnStart.setVisibility(View.VISIBLE);
		}
		@Override
		public void onPrepareLoad(Drawable placeHolderDrawable) {
			//btnStart.setVisibility(View.VISIBLE);
		}
	};

	public void requestCover(){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
		params.put("LANG", PlaygreenManager.getLanguagePG());

		StringRequest myReq = netUtil.requestPost(APIKEY.COVER_PAGE, URLS.COVER_PAGE, params);
		NetworkController.addToRequestQueue(myReq);
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx){
			case APIKEY.COVER_PAGE:
				netResultData = json.DATA;
				onUIRefresh();
				break;
		}
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
		btnStart.setVisibility(View.VISIBLE);
	}
}
