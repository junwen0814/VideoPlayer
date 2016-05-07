package com.junwen.videoplayer.imservice.support;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.junwen.videoplayer.imservice.service.IMService;

/**
 * IMService绑定
 *
 * @modify yingmu
 * 1. 供上层使用【activity】
 * 同层次的manager没有必要使用。
 */
public abstract class IMServiceConnector {

    public abstract void onIMServiceConnected();

    public abstract void onServiceDisconnected();

    private IMService imService;

    public IMService getIMService() {
        return imService;
    }

    // todo eric when to release?
    private ServiceConnection imServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // todo eric when to unbind the service?
            // TODO Auto-generated method stub
            IMServiceConnector.this.onServiceDisconnected();
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) { //绑定成功后就会执行当前方法
            // TODO Auto-generated method stub

            if (imService == null) {
                IMService.IMServiceBinder binder = (IMService.IMServiceBinder) service;
                imService = binder.getService();
                if (imService == null) {
                    return;
                }
            }
            IMServiceConnector.this.onIMServiceConnected();
        }
    };

    public boolean connect(Context ctx) {
        return bindService(ctx);
    }

    public void disconnect(Context ctx) {
        unbindService(ctx);
        IMServiceConnector.this.onServiceDisconnected();
    }

    public boolean bindService(Context ctx) {

        Intent intent = new Intent();
        intent.setClass(ctx, IMService.class);

        if (!ctx.bindService(intent, imServiceConnection, Context.BIND_AUTO_CREATE)) {
            return false;
        } else {
            return true;
        }
    }

    public void unbindService(Context ctx) {
        try {
            // todo eric .check the return value .check the right place to call it
            ctx.unbindService(imServiceConnection);
        } catch (IllegalArgumentException exception) {
        }
    }
}
