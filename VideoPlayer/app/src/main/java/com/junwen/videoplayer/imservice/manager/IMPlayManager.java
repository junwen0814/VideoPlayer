package com.junwen.videoplayer.imservice.manager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.junwen.videoplayer.R;
import com.junwen.videoplayer.utils.ScreenUtils;
import com.zhy.autolayout.AutoRelativeLayout;

import io.vov.vitamio.widget.VideoView;

/**
 * 描述 : 播放模块管理类
 * 作者 : 卜俊文
 * 日期 : 2016/3/13 16:19
 * 邮箱：344176791@qq.com
 */

public class IMPlayManager {

    private static IMPlayManager mPlayManager;

    /**
     * 描述 : 单例模式
     * 作者 : 卜俊文
     * 日期 : 2016/3/13 16:03
     * 邮箱：344176791@qq.com
     */
    public static IMPlayManager getInstance() {
        if (mPlayManager == null) {
            synchronized (IMPlayManager.class) {
                if (mPlayManager == null) {
                    mPlayManager = new IMPlayManager();
                }
            }
        }
        return mPlayManager;
    }

    /**
     * 描述 : 全屏效果
     * 作者 : 卜俊文
     * 日期 : 2016/3/13 16:01
     * 邮箱：344176791@qq.com
     */
    public void toFullScreen(VideoView videoView) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        videoView.setLayoutParams(layoutParams);
    }

    /**
     * 描述 : 根据当前播放状态返回指定图片ID
     * 作者 : 卜俊文
     * 日期 : 2016/3/13 16:26
     * 邮箱：344176791@qq.com
     */
    public int getActionImgByState(boolean isPlaying) {
        return isPlaying ? R.drawable.img_play_pause : R.drawable.img_play_start;
    }

    /**
     * 描述 : 根据X判断是亮度还是音量
     * 作者 : 卜俊文
     * 日期 : 2016/3/19 15:06
     * 邮箱：344176791@qq.com
     */
    public boolean getScreenByX(float x, Context context) {
        //先获取屏幕的宽度
        int screenWidth = ScreenUtils.getScreenWidth(context);
        int width = screenWidth / 2;
        return x < width ? true : false;
    }

    /**
     * 描述 : 根据Y判断点击的是否在控制栏之外,返回是否可以进行调节
     * 作者 : 卜俊文
     * 日期 : 2016/3/19 15:26
     * 邮箱：344176791@qq.com
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public boolean getScreenByY(float y, Context context, AutoRelativeLayout rv_control) {
        int screenHeight = ScreenUtils.getScreenHeight(context);
        float height = screenHeight - rv_control.getY();
        if (y > height) {
            return false;
        }
        return true;
    }

    /**
     * 描述 : 调节亮度
     * 作者 : 卜俊文
     * 日期 : 2016/3/19 19:03
     * 邮箱：344176791@qq.com
     */
    public void adjustLight(Activity activity, float brightness) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = lp.screenBrightness + brightness;
        if (lp.screenBrightness > 1) {
            lp.screenBrightness = 1;
        } else if (lp.screenBrightness < 0.1) {
            lp.screenBrightness = (float) 0.1;
        }
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 描述 : 调节音量
     * 作者 : 卜俊文
     * 日期 : 2016/3/19 19:06
     * 邮箱：344176791@qq.com
     */
    public void adjustVolume(int percent, Context context) {
        AudioManager audioaAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioaAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, percent, AudioManager.FLAG_SHOW_UI);
    }
}
