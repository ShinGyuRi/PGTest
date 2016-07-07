package tiltcode.movingkey.com.tiltcode_new.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import tiltcode.movingkey.com.tiltcode_new.Model.ListPhotoResult;
import tiltcode.movingkey.com.tiltcode_new.Model.Photo;
import tiltcode.movingkey.com.tiltcode_new.R;
import tiltcode.movingkey.com.tiltcode_new.adapter.GridViewAdapter;
import tiltcode.movingkey.com.tiltcode_new.library.BaseApplication;
import tiltcode.movingkey.com.tiltcode_new.library.ParentFragment;
import tiltcode.movingkey.com.tiltcode_new.library.util.JsinPreference;
import tiltcode.movingkey.com.tiltcode_new.library.util.NetworkUtil;

/**
 * Created by Gyul on 2016-07-07.
 */
public class GridFragment extends ParentFragment {

    public static String TAG = GridFragment.class.getSimpleName();


    GridView gridView;
    GridViewAdapter adapter;

    String username, imgUrl;
    int page;
    public List<Photo> photoList;

    JsinPreference jsinPreference;

    public static GridFragment newInstance() {
        GridFragment frag = new GridFragment();
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grid, container, false);

        jsinPreference = new JsinPreference(BaseApplication.getContext());
        username = jsinPreference.getValue("username", "");

        adapter = new GridViewAdapter();
        gridView = (GridView)view.findViewById(R.id.gridview);

        getPhotoList();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return view;
    }

    public void getPhotoList()  {
        Log.d("TAG", "getCouponList");
        page = 0;

        NetworkUtil.getHttpSerivce().listPhoto(username, String.valueOf(page),
                new Callback<ListPhotoResult>() {
                    @Override
                    public void success(ListPhotoResult listPhotoResult, Response response) {
                        if (listPhotoResult.photo != null)    {
                            photoList = listPhotoResult.photo;

                            if (photoList != null) {
                                for(page=0; page<photoList.size(); page++)  {
                                    Log.d(TAG, "page: "+String.valueOf(page));
                                    imgUrl = photoList.get(page).getImg_path();
                                    Log.d(TAG, "exp: "+photoList.get(page).getImg_path());

                                    gridView.setAdapter(adapter);
                                    adapter.addItem(imgUrl);
                                }
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
    }
}
