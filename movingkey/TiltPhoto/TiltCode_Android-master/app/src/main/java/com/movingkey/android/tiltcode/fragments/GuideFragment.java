package com.movingkey.android.tiltcode.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.movingkey.android.tiltcode.R;
import com.movingkey.android.tiltcode.library.HWILib;

/**
 * Created by jhkim on 2016. 4. 15..
 */
public class GuideFragment extends Fragment
{


    public ImageView hwi_backgroundImageV = null;
    public int position = 0;

    public static GuideFragment newInstance(String content)
    {
        GuideFragment fragment = new GuideFragment();

        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ImageView hwi_backgroundImageV = new ImageView(getActivity());
        hwi_backgroundImageV.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));




        RelativeLayout.LayoutParams btnLayout =  new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        btnLayout.rightMargin = HWILib.hwi_func02_getPxFromDP(getActivity(),10);
        btnLayout.topMargin = HWILib.hwi_func02_getPxFromDP(getActivity(),10);
        btnLayout.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        btnLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);


        RelativeLayout layout = new RelativeLayout(getActivity());
        layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        layout.setGravity(Gravity.CENTER);
        layout.addView(hwi_backgroundImageV);




        if(position == 0)
        {
            hwi_backgroundImageV.setImageResource(R.drawable.bg_tutorial_01);

        }
        else if(position == 1)
        {
            hwi_backgroundImageV.setImageResource(R.drawable.bg_tutorial_02);

        }
        else if(position == 2)
        {
            hwi_backgroundImageV.setImageResource(R.drawable.bg_tutorial_03);

        }


        return layout;
    }

}
