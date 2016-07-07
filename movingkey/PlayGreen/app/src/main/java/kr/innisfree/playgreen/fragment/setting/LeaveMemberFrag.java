package kr.innisfree.playgreen.fragment.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ParentFrag;
import com.volley.network.NetworkConstantUtil;
import com.volley.network.dto.NetworkJson;

import kr.innisfree.playgreen.R;

/**
 * Created by jooyoung on 2016-04-18.
 */
public class LeaveMemberFrag extends ParentFrag implements View.OnClickListener{

	private TextView txtLeaveMember;
	private OnLeaveMemberListener leaveMemberListener;

	public static LeaveMemberFrag newInstance() {
		return new LeaveMemberFrag();
	}

	public LeaveMemberFrag() {}

	public void setLeaveMemberListener(OnLeaveMemberListener listener){
		this.leaveMemberListener = listener;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_leave_member, null);
		setCutOffBackgroundTouch(view);

		/** Init Toolbar*/
		Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
		((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
		((TextView)view.findViewById(R.id.txt_title)).setText(R.string.str_playgreen_leave_member);
		view.findViewById(R.id.layout_back).setOnClickListener(this);

		txtLeaveMember = (TextView)view.findViewById(R.id.txt_leave_member);
		txtLeaveMember.setEnabled(false);
		txtLeaveMember.setOnClickListener(this);

		view.findViewById(R.id.layout_before_leave).setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.layout_back:
				getActivity().onBackPressed();
				break;
			case R.id.layout_before_leave:
				v.setSelected(!v.isSelected());
				txtLeaveMember.setEnabled(v.isSelected());
				break;
			case R.id.txt_leave_member:
				netFunc.requestLeaveMember();
				break;
		}
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx){
			case NetworkConstantUtil.APIKEY.LEAVE_MEMBER:
				if(leaveMemberListener!=null){
					leaveMemberListener.OnLeaveMember();
				}
				getActivity().onBackPressed();
				break;
		}
	}

	public interface OnLeaveMemberListener{
		void OnLeaveMember();
	}

}

