package kr.innisfree.playgreen.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.PrefUtil;
import com.volley.network.NetworkListener;
import com.volley.network.dto.NetworkJson;

import kr.innisfree.playgreen.R;

/**
 * Created by jooyoung on 2016-02-22.
 */
public class GuideDialog extends DialogFragment implements View.OnClickListener, NetworkListener {

	private CheckBox cbNeverSee;
	private TextView txtClose;
	private PrefUtil prefUtil;

	public GuideDialog() {
	}

	boolean isPGNowGuide;


	@SuppressLint("ValidFragment")
	public GuideDialog(boolean isPGNow) {
		this.isPGNowGuide = isPGNow;
	}

	public static GuideDialog newInstance(boolean isPGNowGuide) {
		GuideDialog frag = new GuideDialog(isPGNowGuide);
		return frag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
	}

	@Override
	public void onStart() {
		super.onStart();
		if (getDialog() != null) {
			getDialog().getWindow().setDimAmount(0);
			getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = null;
		if (isPGNowGuide)
			view = inflater.inflate(R.layout.view_guide_pgnow, null);
		else
			view = inflater.inflate(R.layout.view_guide, null);

		if (prefUtil == null) prefUtil = PrefUtil.getInstance();
		cbNeverSee = (CheckBox) view.findViewById(R.id.cb_never_see);
		cbNeverSee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isPGNowGuide) {
					if (isChecked) {
						prefUtil.putPreference(Definitions.PREFKEY.IS_PGNOW_GUIDE_VIEW_NEVER_SEE_BOOL, true);
					} else {
						prefUtil.putPreference(Definitions.PREFKEY.IS_PGNOW_GUIDE_VIEW_NEVER_SEE_BOOL, false);
					}
				} else {
					if (isChecked) {
						prefUtil.putPreference(Definitions.PREFKEY.IS_GUIDE_VIEW_NEVER_SEE_BOOL, true);
					} else {
						prefUtil.putPreference(Definitions.PREFKEY.IS_GUIDE_VIEW_NEVER_SEE_BOOL, false);
					}
				}


			}
		});
		txtClose = (TextView) view.findViewById(R.id.txt_close);
		txtClose.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.txt_close:
				dismiss();
				break;
		}
	}

	@Override
	public void onNetworkStart(int idx) {

	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {

	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {

	}
}
