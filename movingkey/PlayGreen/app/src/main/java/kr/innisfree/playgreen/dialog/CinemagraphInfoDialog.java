package kr.innisfree.playgreen.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.volley.network.NetworkListener;
import com.volley.network.dto.NetworkJson;

import kr.innisfree.playgreen.R;

/**
 * Created by jooyoung on 2016-02-22.
 */
public class CinemagraphInfoDialog extends DialogFragment implements View.OnClickListener, NetworkListener {

    public CinemagraphInfoDialog() {
    }

    public static CinemagraphInfoDialog newInstance() {
        CinemagraphInfoDialog frag = new CinemagraphInfoDialog();
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
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dlg_cinemagraph_info, null);

        view.findViewById(R.id.cinemagraph_info_btn_close).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cinemagraph_info_btn_close:
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
