package kr.innisfree.playgreen.fragment.camera;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ParentFrag;
import com.airfactory.service.GPSService;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.TextUtil;
import com.volley.network.NetworkConstantUtil;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.NetworkController;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.CameraActivity;
import kr.innisfree.playgreen.adapter.SearchLocationAdapter;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

public class SearchLocationFrag extends ParentFrag implements View.OnClickListener {

    private final static String TAG = "SearchLocationFrag";

    public static final int TYPE_CAMERA = 0;
    public static final int TYPE_CINEMAGRAPH = 1;

    private CameraActivity cameraActivity;

    private Toolbar mToolbar;
    private TextView txtTitle;
    private EditText editLocation;
    private ImageButton imgBtnDeleteLocation;

    private RecyclerView rvLocation;
    private SearchLocationAdapter adapter;
    private ArrayList<NetworkData> itemArray;

    public SearchLocationFrag() {
    }

    public static SearchLocationFrag newInstance() {
        SearchLocationFrag frag = new SearchLocationFrag();
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() instanceof CameraActivity)
            this.cameraActivity = (CameraActivity) getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_search_location, null);

        /** Init Toolbar*/
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        txtTitle = (TextView) view.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.str_search_location_title));

        editLocation = (EditText) view.findViewById(R.id.search_location_edit_location);
        editLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || TextUtil.isNull(s.toString())) {
                    imgBtnDeleteLocation.setVisibility(View.GONE);
                } else {
                    imgBtnDeleteLocation.setVisibility(View.VISIBLE);
                    requestSearchLocation(s.toString());
                }
            }
        });
        editLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                    hiddenKeyboard();
                return false;
            }
        });
        imgBtnDeleteLocation = (ImageButton) view.findViewById(R.id.search_location_imgbtn_delete_location);

        rvLocation = (RecyclerView) view.findViewById(R.id.search_location_rv);
        initRv(rvLocation);

        view.findViewById(R.id.search_location_imgbtn_back).setOnClickListener(this);
        imgBtnDeleteLocation.setOnClickListener(this);
        imgBtnDeleteLocation.setVisibility(View.GONE);

        requestSearchLocation(null);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_location_imgbtn_back:
                hiddenKeyboard();
                ((CameraActivity) getActivity()).onBackPressed();
                break;
        }
    }

    @Override
    public void onNetworkResult(int idx, NetworkJson json) {
        super.onNetworkResult(idx, json);
        switch (idx) {
            case APIKEY.GOOGLE_SEARCH_PLACES:
                break;
        }
    }

    @Override
    public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
        super.onNetworkError(idx, error, json);
        JYLog.D(TAG + "::google_place_api::", new Throwable());
        switch (idx) {
            case APIKEY.GOOGLE_SEARCH_PLACES:
                if (json != null) {
                    itemArray = json.results;
                    adapter.setItemArray(itemArray);
                }
//                if (TextUtil.isNull(editLocation.getText().toString()))
//                    adapter.setItemArray(null);
                break;
        }
    }

    private void initRv(RecyclerView rv) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        adapter = new SearchLocationAdapter(getActivity());
        adapter.setAdapterItemClickListener(new AdapterItemClickListener() {
            @Override
            public void onAdapterItemClick(View view, int position) {
                cameraActivity.location = itemArray.get(position);
                hiddenKeyboard();
                cameraActivity.onBackPressed();
            }
        });

        rv.setHasFixedSize(true);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(adapter);

    }

    private void requestSearchLocation(String location) {

//        https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&name=cruise&key=AddYourOwnKeyHere
        HashMap<String, String> params = new HashMap<String, String>();
        if (GPSService.lastLoc != null) {
            String latitude_logitude = GPSService.lastLoc.getLatitude() + "," + GPSService.lastLoc.getLongitude();
            params.put("location", latitude_logitude);
        }
        params.put("language", Locale.getDefault().getLanguage());
        params.put("radius", String.valueOf(100));
        if(!TextUtil.isNull(location)){
            try {
                params.put("name", URLEncoder.encode(location, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        params.put("key", getString(R.string.google_place_api_server_id));
        StringRequest myReq = netUtil.requestGet(APIKEY.GOOGLE_SEARCH_PLACES, NetworkConstantUtil.URLS.GOOGLE_SEARCH_PLACES, params);
        NetworkController.addToRequestQueue(myReq);
    }

}
