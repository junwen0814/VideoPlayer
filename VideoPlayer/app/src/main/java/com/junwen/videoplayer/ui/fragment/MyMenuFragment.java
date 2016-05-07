package com.junwen.videoplayer.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.junwen.videoplayer.R;
import com.junwen.videoplayer.imservice.callback.OnItemSildingClickListener;
import com.mxn.soul.flowingdrawer_core.MenuFragment;


public class MyMenuFragment extends MenuFragment implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView ivMenuUserProfilePhoto;

    private NavigationView navigationView;

    private OnItemSildingClickListener onItemSildingClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container,
                false);
        navigationView = (NavigationView) view.findViewById(R.id.vNavigation);
        ivMenuUserProfilePhoto = (ImageView) view.findViewById(R.id.ivMenuUserProfilePhoto);
        initListener();
        return setupReveal(view);
    }

    private void initListener() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void onOpenMenu() {
    }

    public void onCloseMenu() {
    }

    public void setOnItemSildingClickListener(OnItemSildingClickListener onItemSildingClickListener) {
        this.onItemSildingClickListener = onItemSildingClickListener;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (onItemSildingClickListener != null) {
            onItemSildingClickListener.onItemClick(item.getItemId());
        }
        return false;
    }
}
