package com.junwen.videoplayer.utils;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

import com.junwen.videoplayer.R;
import com.junwen.videoplayer.imservice.manager.SystemBarTintManager;

/**
 * 描述 : 状态栏颜色
 * 作者 :卜俊文
 * 时间 :2016/3/11.
 */
public class SystemColor {
    /**
     * 让Activity状态和通知栏透明
     *
     * @param activity
     */
    public static void windowTransparent(Activity activity) {
        if (android.os.Build.VERSION.SDK_INT > 18) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            // 创建状态栏的管理实例
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            // 激活导航栏设置
            tintManager.setNavigationBarTintEnabled(true);
            // 设置一个颜色给系统栏
            tintManager.setTintColor(activity.getResources().getColor(R.color.coffct));
        }
    }
}
