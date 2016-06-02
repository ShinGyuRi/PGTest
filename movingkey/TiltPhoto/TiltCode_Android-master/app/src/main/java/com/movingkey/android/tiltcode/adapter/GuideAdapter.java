package com.movingkey.android.tiltcode.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.movingkey.android.tiltcode.fragments.GuideFragment;

import java.util.ArrayList;

/**
 * Created by jhkim on 2016. 4. 15..
 */
public class GuideAdapter extends FragmentPagerAdapter
{

    ArrayList<GuideFragment> hwi_arrayOfGudeFragment = null;
    public GuideAdapter(FragmentManager fm)
    {
        super(fm);

        hwi_arrayOfGudeFragment = new ArrayList<>();
        hwi_arrayOfGudeFragment.add(makeOneGuideFragment(0));
        hwi_arrayOfGudeFragment.add(makeOneGuideFragment(1));
        hwi_arrayOfGudeFragment.add(makeOneGuideFragment(2));

        this.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position)
    {
        if(hwi_arrayOfGudeFragment != null)
        return hwi_arrayOfGudeFragment.get(position);

        return null;
    }

    @Override
    public int getCount()
    {
        if(hwi_arrayOfGudeFragment != null)
        return hwi_arrayOfGudeFragment.size();

        return 0;
    }


    GuideFragment makeOneGuideFragment(int position)
    {
        GuideFragment oneFragment = new GuideFragment();
        oneFragment.position = position;

        return oneFragment;
    }
}
