package com.junwen.videoplayer.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.junwen.videoplayer.R;
import com.junwen.videoplayer.imservice.entity.VideoEntity;
import com.junwen.videoplayer.utils.FileUtils;
import com.junwen.videoplayer.utils.ImageUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by 俊文 on 2016/3/12.
 */
public class LocalAdapters extends BaseAdapter {

    private List<VideoEntity> data;
    private Context context;
    private LayoutInflater inflater;
    private List<Bitmap> list;

    public LocalAdapters(List<VideoEntity> data, Context context, List<Bitmap> list) {
        this.data = data;
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    public List<VideoEntity> getData() {
        return data;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_local, null);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.item_local_img);
            holder.name = (TextView) convertView.findViewById(R.id.item_local_name);
            holder.duration = (TextView) convertView.findViewById(R.id.item_local_duration);
            holder.size = (TextView) convertView.findViewById(R.id.item_local_size);
            convertView.setTag(holder);
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        VideoEntity entity = data.get(position);
        holder.name.setText(entity.getDisplayName());
        holder.size.setText(FileUtils.FormetFileSize(entity.getSize()));
        holder.duration.setText(FileUtils.stringForTime((int) entity.getDuration()));
        holder.img.setImageBitmap(ImageUtils.getBitmap(list.get(position),180,130));
        return convertView;
    }

    public class ViewHolder {
        ImageView img;
        TextView name;
        TextView duration;
        TextView size;
    }
}
