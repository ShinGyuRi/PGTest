package kr.innisfree.playgreen.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ParentFrag;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.YN;
import com.moyusoft.util.TextUtil;
import com.moyusoft.util.Util;
import com.volley.network.NetworkConstantUtil;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.NetworkConstantUtil.URLS;
import com.volley.network.NetworkController;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import java.net.URLEncoder;
import java.util.HashMap;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.search.ProfileDetailAct;
import kr.innisfree.playgreen.adapter.CommentAdapter;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-03-04.
 */
public class CommentListFrag extends ParentFrag implements AdapterItemClickListener {

	private String category, targetId;

	private RecyclerView recyclerView;
	private CommentAdapter adapter;
	private LinearLayout layoutEmpty;
	private EditText editComment;
	private TextView txtEnter;

	private NetworkData selectItem;

	public CommentListFrag() {
	}

	@SuppressLint("ValidFragment")
	public CommentListFrag(String category, String targetId) {
		this.category = category;
		this.targetId = targetId;
	}

	public static CommentListFrag newInstance(String likeCategory, String targetId) {
		CommentListFrag frag = new CommentListFrag(likeCategory, targetId);
		return frag;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_comment_list, null);
		setCutOffBackgroundTouch(view);

		/** Init Toolbar*/
		Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
		view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hiddenKeyboard();
				getActivity().onBackPressed();
			}
		});
		((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

		layoutEmpty = (LinearLayout) view.findViewById(R.id.empty_view);
		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		adapter = new CommentAdapter(getContext());
		adapter.setAdapterItemClickListener(this);
		recyclerView.setAdapter(adapter);
		editComment = (EditText) view.findViewById(R.id.edit_comment);
		txtEnter = (TextView) view.findViewById(R.id.txt_input);
		txtEnter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String inputComment = editComment.getText().toString();
				if (TextUtil.isNull(inputComment)) {
					Toast.makeText(getContext(), R.string.str_toast_input_comment, Toast.LENGTH_SHORT).show();
					return;
				}

				try {
					inputComment = URLEncoder.encode(inputComment, "UTF-8");
				}catch (Exception e){
				}
				requestCommentRegist(inputComment);
			}
		});

		reuqestCommentList();
		return view;
	}


	@Override
	public void onAdapterItemClick(View view, int position) {
		selectItem = adapter.getItem(position);
		if (selectItem == null) return;
		switch (view.getId()) {
			case R.id.img_item_profile:
				Intent intent = new Intent(getActivity(), ProfileDetailAct.class);
				intent.putExtra(Definitions.INTENT_KEY.MEMB_ID, selectItem.MEMB_ID);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
			case R.id.txt_item_like:
				if (!TextUtil.isNull(selectItem.LIKE_YN) && selectItem.LIKE_YN.equals(YN.YES))
					requestLike(selectItem.COMMENT_ID, YN.NO, selectItem.LIKE_ID);
				else
					requestLike(selectItem.COMMENT_ID, YN.YES, null);
				break;
			case R.id.txt_item_edit:
				//TODO 수정
				break;
			case R.id.txt_item_delete:
				requestCommentDelete(selectItem.COMMENT_ID);
				break;
		}
	}

	public void reuqestCommentList() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
		params.put("COMMENT_CATEGORY", category);
		params.put("TARGET_ID", targetId);
		StringRequest myReq = netUtil.requestPost(APIKEY.COMMENT_LIST, URLS.COMMENT_LIST, params);
		NetworkController.addToRequestQueue(myReq);
	}

	/**
	 * 댓글 좋아요
	 */
	public void requestLike(String targetId, String likeYN, String likeId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
		params.put("LIKE_CATEGORY", "C");
		params.put("TARGET_ID", targetId);
		params.put("LIKE_YN", likeYN);
		if (!TextUtil.isNull(likeId))
			params.put("LIKE_ID", likeId);
		StringRequest myReq = netUtil.requestPost(APIKEY.LIKE_ACTION, URLS.LIKE_ACTION, params);
		NetworkController.addToRequestQueue(myReq);
	}

	public void requestCommentEdit(String commentId, String commentText) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
		params.put("COMMENT_ID", commentId);
		params.put("COMMENT_TEXT", commentText);
		params.put("LOCATION", PlaygreenManager.getLocation());
		StringRequest myReq = netUtil.requestPost(APIKEY.COMMENT_EDIT, URLS.COMMENT_EDIT, params);
		NetworkController.addToRequestQueue(myReq);
	}

	public void requestCommentDelete(String commentId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
		params.put("COMMENT_ID", commentId);
		StringRequest myReq = netUtil.requestPost(APIKEY.COMMENT_DELETE, URLS.COMMENT_DELETE, params);
		NetworkController.addToRequestQueue(myReq);
	}

	public void requestCommentRegist(String comment) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
		params.put("COMMENT_CATEGORY", category);
		params.put("TARGET_ID", targetId);
		params.put("COMMENT_TEXT", comment);
		params.put("LOCATION", PlaygreenManager.getLocation());
		StringRequest myReq = netUtil.requestPost(APIKEY.COMMENT_REGIST, URLS.COMMENT_REGIST, params);
		NetworkController.addToRequestQueue(myReq);
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx) {
			case APIKEY.COMMENT_LIST:
				adapter.setItemArray(json.LIST);
				if (json.LIST == null || json.LIST.size() == 0)
					layoutEmpty.setVisibility(View.VISIBLE);
				else
					layoutEmpty.setVisibility(View.GONE);
				break;
			case APIKEY.LIKE_ACTION:
				if(selectItem!=null){
					if(!TextUtil.isNull(selectItem.LIKE_YN) && selectItem.LIKE_YN.equals(YN.YES)){
						selectItem.LIKE_YN = YN.NO;
					}else{
						selectItem.LIKE_ID = json.LIKE_ID;
						selectItem.LIKE_YN = YN.YES;
					}
					adapter.notifyDataSetChanged();
				}
				break;
			case APIKEY.COMMENT_REGIST:
				Toast.makeText(getContext(), R.string.str_toast_regist_comment_complete, Toast.LENGTH_SHORT).show();
				editComment.setText("");
				reuqestCommentList();
				break;
			case APIKEY.COMMENT_EDIT:
				editComment.setText("");
				reuqestCommentList();
				break;
			case APIKEY.COMMENT_DELETE:
				editComment.setText("");
				reuqestCommentList();
				break;
		}
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
		if (json == null || json.RESULT_CODE == null) return;
		switch (json.RESULT_CODE) {
			case NetworkConstantUtil.NETWORK_RESULT_CODE.NOT_EXIST_DATA:
				layoutEmpty.setVisibility(View.GONE);
				break;
		}
	}
}
