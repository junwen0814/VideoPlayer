package com.junwen.videoplayer.imservice.manager;

import android.content.Context;

import com.junwen.videoplayer.imservice.network.VideoListTask;

/**
 * 描述 : 本地视频管理类
 * 作者 :卜俊文
 * 时间 :2016/3/11.
 */
public class IMLocalManager extends IMManager {

    private static IMLocalManager mLocalManager;

    /**
     * 描述 :单例模式
     * 作者 :卜俊文
     * 时间 :2016/3/11 23:13
     */

    public static IMLocalManager getInstance() {
        if (mLocalManager == null) {
            synchronized (IMLocalManager.class) {
                if (mLocalManager == null) {
                    mLocalManager = new IMLocalManager();
                }
            }
        }
        return mLocalManager;
    }


    /**
     * 描述 : 获取视频集合
     * 作者 : 卜俊文
     * 日期 : 2016/3/12 21:57
     * 邮箱：344176791@qq.com
     */
    public void getVideoList(Context context) {
        new VideoListTask(context).start();
    }


    @Override
    public void doOnStart() {

    }

    @Override
    public void reset() {

    }
}
