package com.junwen.videoplayer.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.widget.Toast;

import com.flyco.animation.Attention.Swing;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.junwen.videoplayer.DB.entity.NetWorkEntity;
import com.junwen.videoplayer.R;
import com.junwen.videoplayer.config.NetWorkConstant;
import com.junwen.videoplayer.config.PlayConstant;
import com.junwen.videoplayer.imservice.callback.OnRecyViewItemClickListaner;
import com.junwen.videoplayer.imservice.event.NetworkEvent;
import com.junwen.videoplayer.imservice.manager.IMNetworkManager;
import com.junwen.videoplayer.ui.activity.PlayActivity;
import com.junwen.videoplayer.ui.adapter.NetVIdeoGridAdapter;
import com.junwen.videoplayer.ui.base.BaseFragment;
import com.junwen.videoplayer.utils.L;
import com.junwen.videoplayer.utils.NetWorkUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述 : 选项卡的个别Fragment
 * 作者 : 卜俊文
 * 日期 : 2016/3/21 2:28
 * 邮箱：344176791@qq.com
 */
public class NetVideoFragment extends BaseFragment implements XRecyclerView.LoadingListener, OnRecyViewItemClickListaner {

    @ViewInject(R.id.net_recyclerview)
    private XRecyclerView mRecyclerView;

    private String videoType;


    private NetVIdeoGridAdapter mAdapter;

    @Override
    protected void init() {
        setContentView(R.layout.fragment_net_item);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        Bundle arguments = getArguments();
        initRecyView();
        videoType = arguments.getString(NetWorkConstant.VIDEO_TYPE);
        mAdapter = new NetVIdeoGridAdapter(new ArrayList<NetWorkEntity>(), getActivity());
        mRecyclerView.setAdapter(mAdapter);
        initGridView();
    }

    private void initRecyView() {
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.SquareSpin);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    }

    private void initGridView() {
        updateDataSource(IMNetworkManager.getInstance().getVideoInfo(videoType));
    }


    public void onEventMainThread(NetworkEvent event) {
        switch (event) {
            case NETWORK_EVENT_SUCCESS:
                onLoadNetworkSuccess();
                break;
            case NETWORK_EVENT_FAIL:
                L.i("No Select Data");
                mRecyclerView.refreshComplete();
                break;
            case NETWORK_EVENT_EXCEPTION:
                L.i("Seclect Exception");
                break;
        }
    }

    /**
     * 描述 : 当获取完成的时候
     * 作者 : 卜俊文
     * 日期 : 2016/3/21 2:24
     * 邮箱：344176791@qq.com
     */
    private void onLoadNetworkSuccess() {
        updateDataSource(IMNetworkManager.getInstance().getVideoInfo(videoType));
    }

    /**
     * 描述 : 更新适配器数据
     * 作者 : 卜俊文
     * 日期 : 2016/3/21 2:24
     * 邮箱：344176791@qq.com
     */
    public void updateDataSource(List<NetWorkEntity> data) {
        mAdapter.getData().clear();
        if (data.size() <= 0) {
            loadingData();
        } else {
//            setLoadingVisiable(false);
            mAdapter.getData().addAll(data);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.refreshComplete();
            if (data.size() > 10) {
                mRecyclerView.setLoadingMoreEnabled(true);
            }
        }
    }

    /**
     * 描述:加载网络视频
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    private void loadingData() {
//        setLoadingVisiable(true);
        IMNetworkManager.getInstance().getVideoInfoByType(videoType, getActivity());
    }

    @Override
    protected void initListener() {
        mRecyclerView.setLoadingListener(this);
        mAdapter.setOnRecyViewItemClickListaner(this);
    }


    @Override
    public void onRefresh() {
        if (NetWorkUtils.isNetworkVisiable(getActivity()) != NetWorkUtils.STATE_ERROE) {
            IMNetworkManager.getInstance().getVideoInfoByType(videoType, getActivity());
        } else {
            Toast.makeText(getActivity(), "网络不可用，请检查后重试!", Toast.LENGTH_SHORT).show();
            mRecyclerView.refreshComplete();
        }
    }

    @Override
    public void onLoadMore() {
        Toast.makeText(getActivity(), "加载更多", Toast.LENGTH_SHORT).show();
    }

    /**
     * 描述 : 当点击某个视频时
     * 作者 : 卜俊文
     * 日期 : 2016/3/21 2:51
     * 邮箱：344176791@qq.com
     */
    @Override
    public void onItemClick(int position) {
        int networkVisiable = NetWorkUtils.isNetworkVisiable(getActivity());
        if (networkVisiable != NetWorkUtils.STATE_ERROE) {
            if (networkVisiable == NetWorkUtils.STATE_CONNETCT_MOBLIC) {
                //如果是手机网络手机的话，就要提示用户确定是否要继续播放视频
                showNotWifi(position);
                return;
            }
            jumpPlayActivity(position);
        } else {
            Toast.makeText(getActivity(), "网络不可用，请检查后重试!", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 描述:弹出提示框
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    private void showNotWifi(final int position) {
        final NormalDialog dialog = new NormalDialog(getActivity());
        dialog.isTitleShow(false);
        dialog.content("当前是非WIFI状态，是否继续播放？");
        dialog.contentTextSize(Color.parseColor("#000000"));
        dialog.btnText("取消", "继续");
        dialog.showAnim(new Swing());
        dialog.dismissAnim(new FadeExit());
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
            }
        }, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
                jumpPlayActivity(position);
            }
        });
        dialog.show();
    }

    /**
     * 描述:跳转播放界面
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    public void jumpPlayActivity(int position) {
        NetWorkEntity netWorkEntity = mAdapter.getData().get(position);
        Intent intent = new Intent(getActivity(), PlayActivity.class);
        intent.putExtra(PlayConstant.VIDEO_TYPE_TAG, PlayConstant.VIDEO_TPYE_NETWORK);
        intent.putExtra(PlayConstant.VIDEO_URL, netWorkEntity.getVideoUrl());
        getActivity().startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
