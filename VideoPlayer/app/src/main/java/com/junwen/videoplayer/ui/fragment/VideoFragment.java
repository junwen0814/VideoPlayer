package com.junwen.videoplayer.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.flyco.animation.Attention.Swing;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.junwen.videoplayer.DB.DBInterface;
import com.junwen.videoplayer.DB.entity.VideoServerEntity;
import com.junwen.videoplayer.R;
import com.junwen.videoplayer.config.PlayConstant;
import com.junwen.videoplayer.imservice.callback.OnRecyViewItemClickListaner;
import com.junwen.videoplayer.imservice.event.VideoEvent;
import com.junwen.videoplayer.imservice.manager.IMVideoManager;
import com.junwen.videoplayer.ui.activity.PlayActivity;
import com.junwen.videoplayer.ui.adapter.VideoAdapter;
import com.junwen.videoplayer.ui.base.BaseFragment;
import com.junwen.videoplayer.utils.NetWorkUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者:卜俊文
 * 邮箱:344176791@qq.com
 * 时间:16/5/3 下午8:30
 */
public class VideoFragment extends BaseFragment implements XRecyclerView.LoadingListener, OnRecyViewItemClickListaner {

    @ViewInject(R.id.video_recyclerview)
    private XRecyclerView mRecyclerView;

    private VideoAdapter mAdapter;

    @Override
    protected void init() {
        setContentView(R.layout.fragment_video);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        initRecyView();
        mAdapter = new VideoAdapter(new ArrayList<VideoServerEntity>(), getActivity());
        mRecyclerView.setAdapter(mAdapter); //设置适配器
        updateDataSource(IMVideoManager.getInstance().getVideoSource()); //初始化数据
    }

    /**
     * 描述:初始化RecyView
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    private void initRecyView() {
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.SquareSpin);
        mRecyclerView.setLoadingMoreEnabled(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.offsetChildrenVertical(10);
        gridLayoutManager.offsetChildrenHorizontal(10);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void initListener() {
        mRecyclerView.setLoadingListener(this);
        mAdapter.setOnRecyViewItemClickListaner(this);
    }

    /**
     * 描述 : 更新适配器数据
     * 作者 : 卜俊文
     * 日期 : 2016/3/21 2:24
     * 邮箱：344176791@qq.com
     */
    public void updateDataSource(List<VideoServerEntity> data) {
        Log.d("VideoFragment", "updateDataSource");
        mAdapter.getData().clear();
        if (data.size() <= 0) {
            loadingData(); //如果数据库没有查到，则去网络上查找
        } else {
            //数据库有数据的话，先更新上去
            mAdapter.getData().addAll(data);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.refreshComplete();
            if (data.size() > 10) {
                mRecyclerView.setLoadingMoreEnabled(true);
            }
        }
    }


    /**
     * 描述:Event回调
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    public void onEventMainThread(VideoEvent event) {
        switch (event) {
            case VIDEO_EXCETION:
                onException();
                break;
            case VIDEO_FAIL:
                onFail();
                break;
            case VIDEO_SUCCESS:
                onDataSuccess();
                break;
        }
        mRecyclerView.refreshComplete();
    }

    /**
     * 描述:当获取失败的时候
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    private void onFail() {
        Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
    }

    /**
     * 描述:当获取异常的时候
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    private void onException() {
        Toast.makeText(getActivity(), "视频获取异常，请检查服务器是否启动", Toast.LENGTH_SHORT).show();
    }

    /**
     * 描述:当数据获取成功后
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    private void onDataSuccess() {
        List<VideoServerEntity> videoList = DBInterface.getInstance().getVideoList();
        if (videoList.size() > 0) {
            updateDataSource(videoList);
        } else {
            Toast.makeText(getActivity(), "未查到数据", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 描述:加载数据
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    private void loadingData() {
        Log.d("VideoFragment", "loadingData1");
        IMVideoManager.getInstance().loadData();
    }

    @Override
    public void onRefresh() {
        loadingData();
    }

    @Override
    public void onLoadMore() {

    }

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
     * 描述:跳转播放界面
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    public void jumpPlayActivity(int position) {
        VideoServerEntity videoServerEntity = mAdapter.getData().get(position);
        Intent intent = new Intent(getActivity(), PlayActivity.class);
        intent.putExtra(PlayConstant.VIDEO_TYPE_TAG, PlayConstant.VIDEO_TPYE_NETWORK);
        intent.putExtra(PlayConstant.VIDEO_URL, videoServerEntity.getVideoUrl());
        startActivity(intent);
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
}
