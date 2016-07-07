package kr.innisfree.playgreen.activity.setting;

import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.ParentAct;
import com.android.volley.VolleyError;
import com.moyusoft.util.JYLog;
import com.volley.network.dto.NetworkJson;

import kr.innisfree.playgreen.R;

/**
 * Created by jooyoung on 2016-04-14.
 */
public class SampleAct extends ParentAct implements View.OnClickListener {

	VideoView videoView;
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		setLoading(this);
		//initToolbar();

		videoView = (VideoView)findViewById(R.id.videoview);
		videoView.setVideoURI(Uri.parse("http://124.111.244.46:8080/upload/timeline/201604/20160414133802190132.mp4"));
		MediaController controller = new MediaController(this);
		//videoView.setMediaController(controller);
		videoView.requestFocus();

		videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
			@Override
			public boolean onInfo(MediaPlayer mp, int what, int extra) {
				return false;
			}
		});
		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				JYLog.D(new Throwable());
				videoView.start();
			}
		});
	}

	private void initToolbar() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		findViewById(R.id.layout_back).setOnClickListener(this);
		findViewById(R.id.txt_confirm).setOnClickListener(this);
		((TextView) findViewById(R.id.txt_title)).setText(R.string.str_profile_edit);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.layout_back:
				onBackPressed();
				break;
			case R.id.txt_confirm:

				break;

		}
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
	}
}
