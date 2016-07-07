package kr.innisfree.playgreen.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.moyusoft.util.BitmapCircleResize;
import com.moyusoft.util.Definitions.DIALOG_SELECT;
import com.moyusoft.util.TextUtil;
import com.squareup.picasso.Picasso;
import com.volley.network.NetworkListener;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.data.DialogData;
import kr.innisfree.playgreen.listener.DialogListener;

/**
 * Created by jooyoung on 2016-02-22.
 */
public class FollowCancelDialog extends DialogFragment implements View.OnClickListener, NetworkListener {

	private DialogListener dialogListener;
	private NetworkData userData;

	private ImageView imgProfile;
	private TextView txtMessage;

	public FollowCancelDialog() {}

	public void setupDialog(NetworkData data,  DialogListener listener){
		userData = data;
		dialogListener = listener;
	}

	public static FollowCancelDialog newInstance() {
		FollowCancelDialog frag = new FollowCancelDialog();
		return frag;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (getDialog() != null) {
			getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dlg_follow_cancel, null);

		imgProfile = (ImageView)view.findViewById(R.id.img_profile);
		txtMessage = (TextView)view.findViewById(R.id.txt_message);

		if(userData!=null){
			if(!TextUtil.isNull(userData.MEMB_IMG)){
				Picasso.with(getContext()).load(userData.MEMB_IMG)
						.transform(new BitmapCircleResize(getContext(), getResources().getDimensionPixelOffset(R.dimen.dp_56))).into(imgProfile);
			}
			if(!TextUtil.isNull(userData.MEMB_NAME)){
				txtMessage.setText(getString(R.string.str_follow_cancel_confirm_message, userData.MEMB_NAME));
			}
		}

		view.findViewById(R.id.txt_confirm).setOnClickListener(this);
		view.findViewById(R.id.txt_cancel).setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		DialogData dialogData =new DialogData();
		switch (v.getId()) {
			case R.id.txt_confirm:
				dialogData.dialogButtonType = DIALOG_SELECT.CONFIRM;
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
