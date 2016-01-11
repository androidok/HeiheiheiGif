package com.boredream.hhhgif.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.entity.GifInfo;
import com.bumptech.glide.Glide;

import java.util.List;

public class GifInfoAdapter extends RecyclerView.Adapter<GifInfoAdapter.ViewHolder> {
    private Context context;
    private List<GifInfo> datas;

    public GifInfoAdapter(Context context, List<GifInfo> datas) {
        this.context = context;
        this.datas = datas;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_gif;
        public TextView tv_title;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_gif = (ImageView) itemView.findViewById(R.id.iv_gif);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid_gif, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        GifInfo gifInfo = datas.get(position);
        Glide.with(context).load(gifInfo.getThumbnailImgUrl()).centerCrop().crossFade().into(holder.iv_gif);
        holder.tv_title.setText(gifInfo.getTitle());
    }
}
