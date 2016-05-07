package com.junwen.videoplayer.app;

import android.app.Application;
import android.graphics.Bitmap;

import com.junwen.videoplayer.DB.DBInterface;
import com.junwen.videoplayer.config.BmobConstant;
import com.junwen.videoplayer.imservice.entity.VideoEntity;
import com.yolanda.nohttp.NoHttp;

import java.util.List;

import cn.bmob.v3.Bmob;
import me.drakeet.library.CrashWoodpecker;

/**
 * 描述 :
 * 作者 :卜俊文
 * 时间 :2016/3/11.
 */
public class IMApplication extends Application {

    public static List<Bitmap> list; //本地视频集合
    public static List<VideoEntity> video_list; //本地视频实体

    @Override
    public void onCreate() {
        super.onCreate();
        CrashWoodpecker.init(this);
        DBInterface.getInstance().createDbHelp(getApplicationContext());
        Bmob.initialize(this, BmobConstant.APPLICATION_ID); //初始化Bmob
        NoHttp.init(this);
    }

}
