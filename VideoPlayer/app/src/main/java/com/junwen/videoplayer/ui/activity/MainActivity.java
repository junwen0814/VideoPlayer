package com.junwen.videoplayer.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.junwen.videoplayer.R;
import com.junwen.videoplayer.imservice.callback.OnItemSildingClickListener;
import com.junwen.videoplayer.ui.fragment.LocalFragment;
import com.junwen.videoplayer.ui.fragment.MyMenuFragment;
import com.junwen.videoplayer.ui.fragment.NetworkFragment;
import com.junwen.videoplayer.ui.fragment.VideoFragment;
import com.junwen.videoplayer.utils.SystemColor;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mingle.widget.LoadingView;
import com.mxn.soul.flowingdrawer_core.FlowingView;
import com.mxn.soul.flowingdrawer_core.LeftDrawerLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemSildingClickListener {

    @ViewInject(R.id.id_drawerlayout)
    private LeftDrawerLayout mLeftDrawerLayout; //侧滑layout

    @ViewInject(R.id.main_loading)
    private LoadingView loadingView; //加载动画

    private FragmentManager mFragmentManager; //Fragment管理器

    private LocalFragment local; //本地Fragment

    private NetworkFragment network; //网络Fragment

    private VideoFragment video;

    private MyMenuFragment mMenuFragment; //侧滑布局

    private List<Fragment> fragments = new ArrayList<>();

    private final int REPLACE_ID = R.id.main_container; //Fragment替放ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        init();
        initListener();
    }

    /**
     * 描述 : 初始化监听
     * 作者 : 卜俊文
     * 日期 : 2016/3/15 0:42
     * 邮箱：344176791@qq.com
     */
    private void initListener() {
        mMenuFragment.setOnItemSildingClickListener(this);
    }

    /**
     * 描述 :初始化基本
     * 作者 :卜俊文
     * 时间 :2016/3/11 21:55
     */
    private void init() {
        //设置任务栏颜色
        SystemColor.windowTransparent(MainActivity.this);
        //设置标题
        setupToolbar();
        setTitle("本地视频");
        //初始化侧滑菜单
        mLeftDrawerLayout = (LeftDrawerLayout) findViewById(R.id.id_drawerlayout);
        FragmentManager fm = getSupportFragmentManager();
        mMenuFragment = (MyMenuFragment) fm.findFragmentById(R.id.id_container_menu);
        FlowingView mFlowingView = (FlowingView) findViewById(R.id.sv);
        if (mMenuFragment == null) {
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment = new MyMenuFragment()).commit();
        }
        mLeftDrawerLayout.setFluidView(mFlowingView);
        mLeftDrawerLayout.setMenuFragment(mMenuFragment);
        //初始化Fragment
        initFragment();
        //添加替换Fragmnet
        mFragmentManager = getSupportFragmentManager();
        setDefultFragment();
    }

    /**
     * 描述 : 初始化Fragment集合
     * 作者 : 卜俊文
     * 日期 : 2016/3/20 22:48
     * 邮箱：344176791@qq.com
     */
    private void initFragment() {
        local = new LocalFragment();
        network = new NetworkFragment();
        video = new VideoFragment();
        fragments.add(local);
        fragments.add(network);
        fragments.add(video);
    }

    /**
     * 描述 : 初始化
     * 作者 : 卜俊文
     * 日期 : 2016/3/20 22:43
     * 邮箱：344176791@qq.com
     */
    private void setDefultFragment() {
        for (Fragment fg : fragments) {
            mFragmentManager.beginTransaction().add(REPLACE_ID, fg).commit();
        }
        showFragment(local);
    }

    /**
     * 描述 : 设置当前显示的Fragment
     * 作者 : 卜俊文
     * 日期 : 2016/3/20 22:44
     * 邮箱：344176791@qq.com
     */
    private void showFragment(Fragment fragment) {
        for (Fragment fg : fragments) {
            mFragmentManager.beginTransaction().hide(fg).commit();
        }
        mFragmentManager.beginTransaction().show(fragment).commit();
    }

    /**
     * 描述 : 设置标题
     * 作者 : 卜俊文
     * 日期 : 2016/3/15 0:13
     * 邮箱：344176791@qq.com
     */
    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLeftDrawerLayout.toggle();
            }
        });
    }

    @Override
    public void onItemClick(int id) {
        switch (id) {
            case R.id.menu_local:
                //打开本地视频
                showFragment(local);
                setTitle("本地视频");
                break;
            case R.id.menu_network:
                //打开网络视频
                showFragment(network);
                setTitle("网络视频");
                break;
            case R.id.menu_video:
                //打开视频区
                showFragment(video);
                setTitle("视频区");
                break;
        }
        mLeftDrawerLayout.closeDrawer();
    }

    /**
     * 描述: 设置加载动画是否显示
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    public void setLoadingVisiable(boolean isVisiable) {
        loadingView.setVisibility((isVisiable) ? View.VISIBLE : View.GONE);
    }
}
