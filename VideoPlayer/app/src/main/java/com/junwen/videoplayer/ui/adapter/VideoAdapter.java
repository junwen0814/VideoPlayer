package com.junwen.videoplayer.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.junwen.videoplayer.DB.entity.VideoServerEntity;
import com.junwen.videoplayer.R;
import com.junwen.videoplayer.imservice.callback.OnRecyViewItemClickListaner;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * 作者:卜俊文
 * 邮箱:344176791@qq.com
 * 时间:16/5/3 下午8:44
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> implements View.OnClickListener {

    private List<VideoServerEntity> data;
    private Context context;
    private LayoutInflater inflater;
    private OnRecyViewItemClickListaner onRecyViewItemClickListaner;

    public VideoAdapter(List<VideoServerEntity> data, Context context) {
        this.data = data;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_network_item, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VideoServerEntity netWorkEntity = data.get(position);
        holder.text.setText(netWorkEntity.getVideoName());
        Picasso.with(context).load(netWorkEntity.getThumbnail()).placeholder(R.drawable.img_loading).error(R.drawable.img_loading)
                .into(holder.image);
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

    public List<VideoServerEntity> getData() {
        return data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            image = (ImageView) itemView.findViewById(R.id.net_work_img);
            text = (TextView) itemView.findViewById(R.id.net_work_title);
        }
    }

    public void setOnRecyViewItemClickListaner(OnRecyViewItemClickListaner onRecyViewItemClickListaner) {
        this.onRecyViewItemClickListaner = onRecyViewItemClickListaner;
    }
}
