package com.junwen.videoplayer.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.junwen.videoplayer.ui.fragment.NetVideoFragment;

import java.util.List;


public class NetVideoPagerAdapter extends FragmentStatePagerAdapter {

    private List<NetVideoFragment> data;

    public NetVideoPagerAdapter(FragmentManager fm, List<NetVideoFragment> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String key = data.get(position).getArguments().getString("videoType");
        return key;
    }


    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
