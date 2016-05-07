package com.junwen.videoplayer.imservice.entity;

import java.io.Serializable;

/**
 * 描述 : 视频实体类
 * 作者 : 卜俊文
 * 日期 : 2016/3/13 16:06
 * 邮箱：344176791@qq.com
 */
public class VideoEntity implements Serializable{
    private String displayName;//视频名称
    private long duration; //视频长度
    private long size; //视频大小
    private String path; //视频的绝对路径

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
