package com.junwen.videoplayer.imservice.manager;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junwen.videoplayer.DB.DBInterface;
import com.junwen.videoplayer.DB.entity.VideoServerEntity;
import com.junwen.videoplayer.config.VideoConstant;
import com.junwen.videoplayer.imservice.event.VideoEvent;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.RequestQueue;
import com.yolanda.nohttp.Response;
import com.ypy.eventbus.EventBus;

import java.util.List;

/**
 * 作者:卜俊文
 * 邮箱:344176791@qq.com
 * 时间:16/5/3 下午9:14
 */
public class IMVideoManager {
    private static IMVideoManager videoManager;

    /**
     * 描述:单例模式
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    public static IMVideoManager getInstance() {
        if (videoManager == null) {
            synchronized (IMVideoManager.class) {
                if (videoManager == null) {
                    videoManager = new IMVideoManager();
                }
            }
        }
        return videoManager;
    }

    /**
     * 描述:获取所有视频集合
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    public List<VideoServerEntity> getVideoSource() {
        return DBInterface.getInstance().getVideoList();
    }

    /**
     * 描述:加载网络视频
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    public void loadData() {
        RequestQueue requestQueue = NoHttp.newRequestQueue();
        android.util.Log.i("info", "loadData12");
        String url = VideoConstant.VIDEO_URL;
        Request<String> stringRequest = NoHttp.createStringRequest(url, RequestMethod.POST);
        android.util.Log.i("info", "loadData12" + stringRequest.toString());
        requestQueue.add(1, stringRequest, new OnResponseListener<String>() {

            @Override
            public void onStart(int what) {
                android.util.Log.i("info", "onStart");
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                android.util.Log.i("info", "onSucceed" + response.toString());
                String result = response.get();
                android.util.Log.i("info", result);
                if (!TextUtils.isEmpty(result)) {
                    JSONArray objects = JSON.parseArray(result);
                    for (int i = 0; i < objects.size(); i++) {
                        //遍历所有视频，存入数据库
                        VideoServerEntity videoServerEntity = JSONObject.parseObject(objects.get(i).toString(), VideoServerEntity.class);
                        DBInterface.getInstance().saveVideoServet(videoServerEntity);
                    }
                }
                EventBus.getDefault().post(VideoEvent.VIDEO_SUCCESS);
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                android.util.Log.i("info", "onFailed" + exception.getMessage());
                EventBus.getDefault().post(VideoEvent.VIDEO_EXCETION);
            }

            @Override
            public void onFinish(int what) {
                android.util.Log.i("info", "onFinish");
            }
        });

    }
}
