package com.junwen.videoplayer.imservice.network;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;

import com.junwen.videoplayer.app.IMApplication;
import com.junwen.videoplayer.imservice.entity.VideoEntity;
import com.junwen.videoplayer.imservice.event.LocalEvent;
import com.junwen.videoplayer.utils.ImageUtils;
import com.junwen.videoplayer.utils.L;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.provider.MediaStore;

/**
 * 描述 : 获取影片集合
 * 作者 : 卜俊文
 * 日期 : 2016/3/12 22:02
 * 邮箱：344176791@qq.com
 */
public class VideoListTask extends Thread {

    private Context context;

    public VideoListTask(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        super.run();
        List<VideoEntity> list = new ArrayList<VideoEntity>();
        List<Bitmap> bitmaps = new ArrayList<>();
        //根据内容提供者获取所有影片
        Uri uri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] pojection = {
                MediaStore.Video.Media.DISPLAY_NAME, //视频名字
                MediaStore.Video.Media.DURATION, //视频时长
                MediaStore.Video.Media.SIZE, //视频大小
                MediaStore.Video.Media.DATA //视频在SD卡的绝对路径
        };
        Cursor cursor = context.getContentResolver().query(uri, pojection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                VideoEntity entity = new VideoEntity();
                String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                entity.setDisplayName(displayName);
                entity.setDuration(duration);
                entity.setSize(size);
                entity.setPath(path);
                list.add(entity);
                Bitmap bitmap = ImageUtils.getBitmap(ImageUtils.getVideoThumbnail(path), 180, 130);
                bitmaps.add(bitmap);
            }
        }
        L.i("查询到" + list.size());
        IMApplication.list = bitmaps;
        IMApplication.video_list = list;
        EventBus.getDefault().post(LocalEvent.LOCAL_ONSUCCESS);
    }
}
