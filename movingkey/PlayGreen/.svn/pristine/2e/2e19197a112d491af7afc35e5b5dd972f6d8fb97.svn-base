package kr.innisfree.playgreen.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ParentFrag;
import com.moyusoft.util.DeviceUtil;
import com.moyusoft.util.Util;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.user.LoginAct;

/**
 * Created by jooyoung on 2016-02-17.
 */
public class IntroFrag extends ParentFrag implements View.OnClickListener {

	private int position;
	private LinearLayout layoutBG, layoutStart;
	private ImageView imgTitle;
	private TextView txtSubTitle;

	public IntroFrag() {
	}

	@SuppressLint("ValidFragment")
	public IntroFrag(int pos) {
		this.position = pos;
	}

	public static IntroFrag newInstance(int position) {
		IntroFrag frag = new IntroFrag(position);
		return frag;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_intro, null);
		setCutOffBackgroundTouch(view);

		layoutBG = (LinearLayout) view.findViewById(R.id.layout_intro_bg);
		imgTitle = (ImageView) view.findViewById(R.id.img_intro_title);
		txtSubTitle = (TextView) view.findViewById(R.id.txt_intro_subtitle);
		layoutStart = (LinearLayout) view.findViewById(R.id.layout_start);

		if (Build.VERSION.SDK_INT >= 21) {
			layoutBG.setPadding(0, DeviceUtil.getStatusBarHeight(getActivity()), 0, 0);
		}

		if (position == 0) {
//			layoutBG.setBackgroundResource(R.drawable.bg_intro1);
			imgTitle.getLayoutParams().width = getResources().getDimensionPixelOffset(R.dimen.dp_231);
//			imgTitle.setBackgroundResource(R.drawable.img_introtitle1);
			layoutBG.setBackgroundResource(R.drawable.img_intro1_bg);
			imgTitle.getLayoutParams().width = getResources().getDimensionPixelOffset(R.dimen.dp_241);
			imgTitle.setBackgroundResource(R.drawable.img_intro1_text);
			txtSubTitle.setText(R.string.str_intro_subtitle_01);
			layoutStart.setVisibility(View.GONE);
		} else {
//			layoutBG.setBackgroundResource(R.drawable.bg_intro2);
			layoutBG.setBackgroundResource(R.drawable.img_intro2_bg);
			imgTitle.getLayoutParams().width = getResources().getDimensionPixelOffset(R.dimen.dp_243);
			imgTitle.setBackgroundResource(R.drawable.img_introtitle2);
			txtSubTitle.setText(R.string.str_intro_subtitle_02);
			layoutStart.setVisibility(View.VISIBLE);
		}

		layoutStart.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.layout_start:
				Intent i = new Intent(getActivity(), LoginAct.class);
				Util.moveActivity(getActivity(), i, -1, -1, true, false);
				break;
		}
	}
}
