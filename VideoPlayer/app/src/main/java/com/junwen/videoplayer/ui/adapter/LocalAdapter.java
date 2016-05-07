package com.junwen.videoplayer.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.junwen.videoplayer.R;
import com.junwen.videoplayer.imservice.callback.OnRecyViewItemClickListaner;
import com.junwen.videoplayer.imservice.entity.VideoEntity;
import com.junwen.videoplayer.utils.FileUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * 作者:卜俊文
 * 邮箱:344176791@qq.com
 * 时间:4/17/16 12:11 AM
 */
public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.ViewHolder> implements View.OnClickListener {


    private List<VideoEntity> data;
    private Context context;
    private LayoutInflater inflater;
    private List<Bitmap> list;
    private OnRecyViewItemClickListaner onRecyViewItemClickListaner;

    public LocalAdapter(List<VideoEntity> data, List<Bitmap> list, Context context, OnRecyViewItemClickListaner onRecyViewItemClickListaner) {
        this.data = data;
        this.list = list;
        this.context = context;
        this.onRecyViewItemClickListaner = onRecyViewItemClickListaner;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_local, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VideoEntity entity = data.get(position);
        holder.name.setText(entity.getDisplayName());
        holder.size.setText(FileUtils.FormetFileSize(entity.getSize()));
        holder.duration.setText(FileUtils.stringForTime((int) entity.getDuration()));
        holder.img.setImageBitmap(list.get(position));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v) {
        if (onRecyViewItemClickListaner != null) {
            onRecyViewItemClickListaner.onItemClick((Integer) v.getTag());
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView name;
        public TextView duration;
        public TextView size;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            img = (ImageView) itemView.findViewById(R.id.item_local_img);
            name = (TextView) itemView.findViewById(R.id.item_local_name);
            duration = (TextView) itemView.findViewById(R.id.item_local_duration);
            size = (TextView) itemView.findViewById(R.id.item_local_size);
        }
    }

    public List<VideoEntity> getData() {
        return data;
    }

}
