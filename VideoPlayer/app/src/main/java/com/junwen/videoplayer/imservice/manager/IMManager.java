package com.junwen.videoplayer.imservice.manager;

import android.content.Context;

/**
 * 描述 :管理类基类
 * 作者 :卜俊文
 * 时间 :2016/2/28.
 */
public abstract class IMManager {

    protected Context ctx;

    protected void setContext(Context context) {
        if (context == null) {
            throw new RuntimeException("context is null");
        }
        ctx = context;
    }
    public void  onStartIMManager(Context ctx){
        setContext(ctx);
        doOnStart();
    }
    /**
     * imService 服务建立的时候
     * 初始化所有的manager 调用onStartIMManager会调用下面的方法
     */
    public abstract  void doOnStart();
    /**
     * 上下文环境的更新
     * 1. 环境变量的clear
     * 2. eventBus的清空
     * */
    public abstract void reset();
}
