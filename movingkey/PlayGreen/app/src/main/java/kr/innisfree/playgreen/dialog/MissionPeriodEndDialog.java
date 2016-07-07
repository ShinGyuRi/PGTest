package kr.innisfree.playgreen.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.moyusoft.util.Definitions;
import com.volley.network.NetworkListener;
import com.volley.network.dto.NetworkJson;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.data.DialogData;
import kr.innisfree.playgreen.listener.DialogListener;

/**
 * Created by jooyoung on 2016-02-22.
 */
public class MissionPeriodEndDialog extends DialogFragment implements View.OnClickListener, NetworkListener {

	private DialogListener dialogListener;

	public MissionPeriodEndDialog() {
	}

	public void setDialogListener(DialogListener listener) {
		this.dialogListener = listener;
	}

	public static MissionPeriodEndDialog newInstance() {
		MissionPeriodEndDialog frag = new MissionPeriodEndDialog();
		return frag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
		setCancelable(false);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dlg_mission_period_end, null);
		view.findViewById(R.id.txt_confirm).setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		DialogData dialogData = new DialogData();
		switch (v.getId()) {
			case R.id.txt_confirm:
				if (dialogListener != null) {
					dialogData.dialogButtonType = Definitions.DIALOG_SELECT.CONFIRM;
					dialogListener.onSendDlgData(dialogData);
				}
				break;
		}
		dismiss();
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
