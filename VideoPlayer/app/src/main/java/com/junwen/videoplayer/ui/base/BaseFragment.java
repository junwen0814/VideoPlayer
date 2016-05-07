package com.junwen.videoplayer.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;

/**
 * Created by 俊文 on 2016/3/12.
 */
public abstract class BaseFragment extends Fragment {

    private View layoutView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewUtils.inject(this, layoutView);
        initData();
        initListener();
        return layoutView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * 描述 : 初始化
     * 作者 : 卜俊文
     * 日期 : 2016/3/12 21:40
     * 邮箱：344176791@qq.com
     */
    protected abstract void init();

    /**
     * 描述 : 初始化数据
     * 作者 : 卜俊文
     * 日期 : 2016/3/12 21:41
     * 邮箱：344176791@qq.com
     */
    protected abstract void initData();

    /**
     * 描述 : 初始化监听
     * 作者 : 卜俊文
     * 日期 : 2016/3/12 21:41
     * 邮箱：344176791@qq.com
     */
    protected abstract void initListener();

    /**
     * 描述 : 设置视图
     * 作者 : 卜俊文
     * 日期 : 2016/3/12 21:38
     * 邮箱：344176791@qq.com
     */
    protected void setContentView(int layoutId) {
        layoutView = LayoutInflater.from(getActivity()).inflate(layoutId, null);
    }
}
