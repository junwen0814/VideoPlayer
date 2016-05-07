package com.junwen.videoplayer.imservice.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.junwen.videoplayer.imservice.manager.IMLocalManager;
import com.junwen.videoplayer.imservice.manager.IMNetworkManager;

/**
 * 描述 : 服务
 * 作者 :卜俊文
 * 时间 :2016/2/28.
 */
public class IMService extends Service {

    private IMServiceBinder binder = new IMServiceBinder();
    private IMLocalManager localManager = IMLocalManager.getInstance();
    private IMNetworkManager networkManager = IMNetworkManager.getInstance();

    public class IMServiceBinder extends Binder {

        public IMService getService() {
            return IMService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    // 负责初始化 每个manager
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //应用开启初始化 下面这几个怎么释放
        Context ctx = getApplicationContext();
        localManager.onStartIMManager(ctx);
        networkManager.onStartIMManager(ctx);
        return START_STICKY;
    }

    public IMLocalManager getLocalManager() {
        return localManager;
    }

    public void setLocalManager(IMLocalManager localManager) {
        this.localManager = localManager;
    }

    public IMNetworkManager getNetworkManager() {
        return networkManager;
    }

    public void setNetworkManager(IMNetworkManager networkManager) {
        this.networkManager = networkManager;
    }
}
