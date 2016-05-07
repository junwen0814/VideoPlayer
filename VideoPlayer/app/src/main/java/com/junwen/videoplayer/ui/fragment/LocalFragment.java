package com.junwen.videoplayer.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.junwen.videoplayer.R;
import com.junwen.videoplayer.app.IMApplication;
import com.junwen.videoplayer.config.PlayConstant;
import com.junwen.videoplayer.imservice.callback.OnRecyViewItemClickListaner;
import com.junwen.videoplayer.imservice.entity.VideoEntity;
import com.junwen.videoplayer.imservice.event.LocalEvent;
import com.junwen.videoplayer.imservice.manager.IMLocalManager;
import com.junwen.videoplayer.ui.activity.MainActivity;
import com.junwen.videoplayer.ui.activity.PlayActivity;
import com.junwen.videoplayer.ui.adapter.LocalAdapter;
import com.junwen.videoplayer.ui.base.BaseFragment;
import com.junwen.videoplayer.utils.L;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ypy.eventbus.EventBus;


/**
 * 描述 : 本地视频Fragment
 * 作者 :卜俊文
 * 时间 :2016/3/11.
 */
public class LocalFragment extends BaseFragment implements AdapterView.OnItemLongClickListener, OnRecyViewItemClickListaner, XRecyclerView.LoadingListener {

    @ViewInject(R.id.local_listview)
    private XRecyclerView mRecyclerView; //ListVIew

    private LocalAdapter mAdapter; //本地视频适配器


    @Override
    protected void init() {
        setContentView(R.layout.fragment_local);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        setLoadingVisiable(false);
        //获取手机视频
        if (IMApplication.list == null) {
            //如果没有数据，则开启线程加载数据
            setLoadingVisiable(true);
            loadingData();
        } else {
            //如果已经有数据了，就不重复加载数据了
            mAdapter = new LocalAdapter(IMApplication.video_list, IMApplication.list, getActivity(), this);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    protected void initListener() {
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.SquareSpin);
        mRecyclerView.setLoadingMoreEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setLoadingListener(this);
    }

    /**
     * 描述 : 事件接受
     * 作者 : 卜俊文
     * 日期 : 2016/3/12 22:30
     * 邮箱：344176791@qq.com
     */
    public void onEventMainThread(LocalEvent event) {
        switch (event) {
            case LOCAL_ONSUCCESS:
                onLocalLoadingSuccess();
                break;
        }

    }

    /**
     * 描述:当本地视频加载完成后
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    private void onLocalLoadingSuccess() {
        setLoadingVisiable(false);
        if (IMApplication.video_list.size() > 0) {
            //查询到了视频
            mAdapter = new LocalAdapter(IMApplication.video_list, IMApplication.list, getActivity(), this);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.refreshComplete();
        } else {
            Toast.makeText(getActivity(), "暂时未查询到视频", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除EventBus绑定
        L.i("onDestroy");
        EventBus.getDefault().unregister(this);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "长按事件", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 描述:设置加载动画是否显示
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    public void setLoadingVisiable(boolean isVisiable) {
        MainActivity activity = (MainActivity) getActivity();
        activity.setLoadingVisiable(isVisiable);
    }

    /**
     * 描述:加载本地视频文件
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    public void loadingData() {
        IMLocalManager.getInstance().getVideoList(getActivity());
    }

    @Override
    public void onItemClick(int position) {
        VideoEntity item = mAdapter.getData().get(position);
        Intent intent = new Intent(getActivity(), PlayActivity.class);
        intent.putExtra(PlayConstant.VIDEO_TYPE_TAG, PlayConstant.VIDEO_TYPE_LOCAL);
        intent.putExtra(PlayConstant.VIDEO_URL, item.getPath());
        getActivity().startActivity(intent);
    }

    @Override
    public void onRefresh() {
        loadingData();
    }

    @Override
    public void onLoadMore() {
        Toast.makeText(getActivity(), "加载更多", Toast.LENGTH_SHORT).show();
    }

}
