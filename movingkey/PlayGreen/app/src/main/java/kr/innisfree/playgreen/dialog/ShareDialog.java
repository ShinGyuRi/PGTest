package kr.innisfree.playgreen.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.moyusoft.util.Definitions.DIALOG_SELECT;
import com.volley.network.NetworkListener;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.data.DialogData;
import kr.innisfree.playgreen.listener.DialogListener;

/**
 * Created by jooyoung on 2016-02-22.
 */
public class ShareDialog extends DialogFragment implements View.OnClickListener, NetworkListener {

	private LinearLayout layoutCopyUrl, layoutFacebook, layoutInstagram, layoutKakaostory;
	private DialogListener dialogListener;
	private NetworkData networkDataItem;

	public ShareDialog() {
	}

	public void setDialogListener(DialogListener listener) {
		this.dialogListener = listener;
	}

	public static ShareDialog newInstance() {
		ShareDialog frag = new ShareDialog();
		return frag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dlg_share, null);
		layoutCopyUrl = (LinearLayout) view.findViewById(R.id.layout_copy_url);
		layoutFacebook = (LinearLayout) view.findViewById(R.id.layout_facebook);
		layoutInstagram = (LinearLayout) view.findViewById(R.id.layout_instagram);
		layoutKakaostory = (LinearLayout) view.findViewById(R.id.layout_kakaostory);
		layoutCopyUrl.setOnClickListener(this);
		layoutFacebook.setOnClickListener(this);
		layoutInstagram.setOnClickListener(this);
		layoutKakaostory.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		DialogData dialogData =new DialogData();
		switch (v.getId()) {
			case R.id.layout_copy_url:
				dialogData.dialogButtonType = DIALOG_SELECT.SHARE_COPY_URL;
				break;
			case R.id.layout_facebook:
				dialogData.dialogButtonType = DIALOG_SELECT.SHARE_FACEBOOK;
				break;
			case R.id.layout_instagram:
				dialogData.dialogButtonType = DIALOG_SELECT.SHARE_INSTAGRAM;
				break;
			case R.id.layout_kakaostory:
				dialogData.dialogButtonType = DIALOG_SELECT.SHARE_KAKAOSTORY;
				break;
		}
		if(dialogListener!=null){
			dialogListener.onSendDlgData(dialogData);
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
