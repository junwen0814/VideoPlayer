package com.junwen.videoplayer.imservice.manager;

import android.content.Context;
import android.os.Bundle;

import com.junwen.videoplayer.DB.DBInterface;
import com.junwen.videoplayer.DB.entity.NetWorkEntity;
import com.junwen.videoplayer.config.NetWorkConstant;
import com.junwen.videoplayer.imservice.event.NetworkEvent;
import com.junwen.videoplayer.ui.fragment.NetVideoFragment;
import com.junwen.videoplayer.utils.L;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 描述 : 网络视频管理类
 * 作者 :卜俊文
 * 时间 :2016/3/11.
 */
public class IMNetworkManager extends IMManager {

    private static IMNetworkManager mNetworkManager;

    /**
     * 描述 :单例模式
     * 作者 :卜俊文
     * 时间 :2016/3/11 23:15
     */

    public static IMNetworkManager getInstance() {
        if (mNetworkManager == null) {
            synchronized (IMNetworkManager.class) {
                if (mNetworkManager == null) {
                    mNetworkManager = new IMNetworkManager();
                }
            }
        }
        return mNetworkManager;
    }

    @Override
    public void doOnStart() {

    }

    @Override
    public void reset() {

    }

    /**
     * 描述 : 返回所有标题
     * 作者 : 卜俊文
     * 日期 : 2016/3/20 13:00
     * 邮箱：344176791@qq.com
     */
    public String[] getTitles() {
        return new String[]{
                NetWorkConstant.VIDEO_TPYE_HOT,
                NetWorkConstant.VIDEO_TYPE_AMUSE,
                NetWorkConstant.VIDEO_TYPE_GOODESS,
                NetWorkConstant.VIDEO_TYPE_DANCE,
                NetWorkConstant.VIDEO_TYPE_MUSIC,
                NetWorkConstant.VIDEO_TYPE_CATE,
                NetWorkConstant.VIDEO_TYPE_TRAVEL,
                NetWorkConstant.VIDEO_TYPE_GOD,
                NetWorkConstant.VIDEO_TYPE_POSTURE,
                NetWorkConstant.VIDEO_TYPE_PET};
    }

    /**
     * 描述 : 获取所有Fragment
     * 作者 : 卜俊文
     * 日期 : 2016/3/20 13:45
     * 邮箱：344176791@qq.com
     */
    public List<NetVideoFragment> getFragments() {
        List<NetVideoFragment> fragments = new ArrayList<>();
        String[] titles = getTitles();
        for (String title : titles) {
            Bundle bundle = new Bundle();
            bundle.putString(NetWorkConstant.VIDEO_TYPE, title);
            NetVideoFragment netVideoFragment = new NetVideoFragment();
            netVideoFragment.setArguments(bundle);
            fragments.add(netVideoFragment);
        }
        return fragments;
    }

    /**
     * 描述 : 根据视频类型获取视频集合
     * 作者 : 卜俊文
     * 日期 : 2016/3/20 14:18
     * 邮箱：344176791@qq.com
     */
    public void getVideoInfoByType(final String videoType, Context context) {
        BmobQuery<NetWorkEntity> list = new BmobQuery<NetWorkEntity>();
        list.addWhereEqualTo("videoType", videoType);
        list.findObjects(context, new FindListener<NetWorkEntity>() {
            @Override
            public void onSuccess(List<NetWorkEntity> list) {
                L.i("Select " + "type" + videoType + "number" + list.size());
                if (list.size() > 0) {
                    L.i("size > 0  " + videoType);
                    DBInterface.getInstance().saveNetworkList(list);
                    noticeEvent(NetworkEvent.NETWORK_EVENT_SUCCESS);
                } else {
                    noticeEvent(NetworkEvent.NETWORK_EVENT_FAIL);
                }
            }

            @Override
            public void onError(int i, String s) {
                L.i("error" + s);
                noticeEvent(NetworkEvent.NETWORK_EVENT_EXCEPTION);
            }
        });
    }

    public void noticeEvent(NetworkEvent event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 描述 : 获取数据库中指定类型的所有视频
     * 作者 : 卜俊文
     * 日期 : 2016/3/21 2:21
     * 邮箱：344176791@qq.com
     */
    public List<NetWorkEntity> getVideoInfo(String videoType) {
        return DBInterface.getInstance().getNetworkList(videoType);
    }

}
