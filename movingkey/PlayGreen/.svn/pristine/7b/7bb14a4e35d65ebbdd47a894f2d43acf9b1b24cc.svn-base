package kr.innisfree.playgreen.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ParentFrag;
import com.moyusoft.util.TextUtil;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.pg21.PG21TodayMissionAct;

/**
 * Created by jooyoung on 2016-03-14.
 */
public class PG21MissionMakeFrag extends ParentFrag implements View.OnClickListener {

	private View view;
	private EditText editMyMission;

	public PG21MissionMakeFrag() {
	}

	public static PG21MissionMakeFrag newInstance() {
		return new PG21MissionMakeFrag();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frag_pg21_make_mission, null);
		setCutOffBackgroundTouch(view);
		initToolbar();

		editMyMission = (EditText)view.findViewById(R.id.edit_my_mission);
		editMyMission.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length()>10){
					editMyMission.removeTextChangedListener(this);
					editMyMission.setText(s.subSequence(0, 10));
					editMyMission.addTextChangedListener(this);
					Toast.makeText(getContext(), R.string.str_toast_input_under_10, Toast.LENGTH_SHORT).show();
					editMyMission.setSelection(10);
				}
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		return view;
	}

	private void initToolbar() {
		Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
		view.findViewById(R.id.layout_back).setOnClickListener(this);
		view.findViewById(R.id.txt_confirm).setOnClickListener(this);
		((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.layout_back:
				hiddenKeyboard();
				getActivity().onBackPressed();
				break;
			case R.id.txt_confirm:
				if(!TextUtil.isNull(editMyMission.getText().toString())){
					if(getActivity()!=null && getActivity() instanceof PG21TodayMissionAct){
						((PG21TodayMissionAct)getActivity()).gotoImageUploadAct("",editMyMission.getText().toString());
					}
//					Intent intent = new Intent(getActivity(), PG21MissionImageAct.class);
//					Util.moveActivity(getActivity(), intent, 0, 0, false, false, Definitions.ACTIVITY_REQUEST_CODE.PG21_TODAY_MISSION_UPLOAD);
				}else{
					Toast.makeText(getContext(), R.string.str_toast_input_my_mission, Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}
}
