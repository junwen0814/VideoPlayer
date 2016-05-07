package com.junwen.videoplayer.ui.fragment;

import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.junwen.videoplayer.R;
import com.junwen.videoplayer.imservice.manager.IMNetworkManager;
import com.junwen.videoplayer.ui.adapter.NetVideoPagerAdapter;
import com.junwen.videoplayer.ui.base.BaseFragment;
import com.junwen.videoplayer.utils.L;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述 : 网络视频Fragment
 * 作者 :卜俊文
 * 时间 :2016/3/11.
 */
public class NetworkFragment extends BaseFragment {

    @ViewInject(R.id.network_tablayout)
    private SlidingTabLayout st_layout; //滑动指示器

    @ViewInject(R.id.network_viewpager)
    private ViewPager mViewPager; //Viewpager

    private List<NetVideoFragment> fragments = new ArrayList<>();

    private NetVideoPagerAdapter mAdapter; //适配器

    @Override
    protected void init() {
        setContentView(R.layout.fragment_network);
    }

    @Override
    protected void initData() {
        //组装Fragment
        this.fragments = IMNetworkManager.getInstance().getFragments();
        mAdapter = new NetVideoPagerAdapter(getActivity().getSupportFragmentManager(), this.fragments);
        mViewPager.setAdapter(mAdapter);
        st_layout.setViewPager(mViewPager);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除EventBus绑定
        L.i("onDestroy");
    }

    @Override
    public void onPause() {
        super.onPause();
        L.i("onPause");

    }
}
