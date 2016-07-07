package kr.innisfree.playgreen.fragment.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ParentAct;
import com.ParentFrag;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.moyusoft.util.Definitions.DIALOG_SELECT;
import com.moyusoft.util.Definitions.YN;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.TextUtil;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.NetworkConstantUtil.NETWORK_RESULT_CODE;
import com.volley.network.NetworkConstantUtil.URLS;
import com.volley.network.NetworkController;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import java.util.HashMap;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.MainAct;
import kr.innisfree.playgreen.adapter.TimelineAdapter;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.data.DialogData;
import kr.innisfree.playgreen.dialog.ShareDialog;
import kr.innisfree.playgreen.fragment.ReportFrag;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;
import kr.innisfree.playgreen.listener.DialogListener;

/**
 * Created by jooyoung on 2016-02-24.
 */
public class DetailPlayGreen21Frag extends ParentFrag implements SwipeRefreshLayout.OnRefreshListener, AdapterItemClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TimelineAdapter timelineAdapter;
    private LinearLayout layoutEmpty;

    private String listType;
    private int page;
    private static int previousPage = 0;

    public DetailPlayGreen21Frag() {
    }

    @SuppressLint("ValidFragment")
    public DetailPlayGreen21Frag(String listType) {
        this.listType = listType;
    }

    public static DetailPlayGreen21Frag newInstance(String listType) {
        DetailPlayGreen21Frag frag = new DetailPlayGreen21Frag(listType);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home_detail_playgreen21, null);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onAdapterItemClick(View view, int position) {
        final NetworkData item = timelineAdapter.getItem(position);
        switch (view.getId()) {
            case R.id.img_item_profile:
                //TODO 프로필 상세페이지
                break;
            case R.id.txt_item_like_count:
                ((MainAct) getActivity()).gotoLikeList("T", item.TIMELINE_ID);
                break;
            case R.id.btn_item_follow:
                requestFollow();
                break;
            case R.id.btn_item_like:
                if (!TextUtil.isNull(item.LIKE_YN) && item.LIKE_YN.equals(YN.YES))
                    requestLike(item.TIMELINE_ID, YN.NO, item.LIKE_ID);
                else
                    requestLike(item.TIMELINE_ID, YN.YES, null);
                break;
            case R.id.btn_item_reply:
                ((MainAct) getActivity()).gotoCommentList("T", item.TIMELINE_ID);
                break;
            case R.id.btn_item_share:
                ShareDialog dialog = new ShareDialog();
                dialog.setDialogListener(dialogListener);
                ((ParentAct) getActivity()).dialogShow(dialog, "share");
                break;
            case R.id.btn_item_etc:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(R.array.alert_etc, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ((MainAct) getActivity()).gotoReport(ReportFrag.NOTIFY_CATEGORY_T, item.TIMELINE_ID);
                    }
                });
                builder.show();
                break;
        }
    }

    public DialogListener dialogListener = new DialogListener() {
        @Override
        public void onSendDlgData(DialogData dialogData) {
            //TODO 공유
            switch (dialogData.dialogButtonType) {
                case DIALOG_SELECT.SHARE_COPY_URL:

                    break;
                case DIALOG_SELECT.SHARE_FACEBOOK:

                    break;
                case DIALOG_SELECT.SHARE_INSTAGRAM:

                    break;
                case DIALOG_SELECT.SHARE_KAKAOSTORY:

                    break;
            }
        }
    };

    public void requestTimeline() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        params.put("CATEGORY", listType);
        params.put("PAGE", page + "");
        StringRequest myReq = netUtil.requestPost(APIKEY.TIMELINE_LIST, URLS.TIMELINE_LIST, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestLike(String targetId, String likeYN, String likeId) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        params.put("LIKE_CATEGORY", "T");
        params.put("TARGET_ID", targetId);
        params.put("LIKE_YN", likeYN);
        if (!TextUtil.isNull(likeId))
            params.put("LIKE_ID", likeId);
        StringRequest myReq = netUtil.requestPost(APIKEY.LIKE_ACTION, URLS.LIKE_ACTION, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestFollow() {
        //TODO follow
    }

    @Override
    public void onNetworkResult(int idx, NetworkJson json) {
        super.onNetworkResult(idx, json);
        switch (idx) {
            case APIKEY.TIMELINE_LIST:
                timelineAdapter.setItemArray(json.LIST);
//                if (json.LIST != null && json.LIST.size() > 0)
//                    previousPage = json.LIST.get(0).PAGE;
                break;
            case APIKEY.LIKE_ACTION:
                requestTimeline();
                break;
        }
    }

    @Override
    public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
        super.onNetworkError(idx, error, json);
        if (json == null) return;
        switch (json.RESULT_CODE) {
            case NETWORK_RESULT_CODE.NOT_EXIST_DATA:
                layoutEmpty.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123 && resultCode == getActivity().RESULT_OK) {
            JYLog.D(new Throwable());
            requestTimeline();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
