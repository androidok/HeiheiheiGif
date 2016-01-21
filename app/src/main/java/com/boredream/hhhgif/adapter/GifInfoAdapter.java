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

    public static final int VIEW_TYPE_CONTENT = 0;
    public static final int VIEW_TYPE_FOOTER = 1;

    private Context context;
    private List<GifInfo> datas;
    private boolean haveMore;

    public boolean isHaveMore() {
        return haveMore;
    }

    public void setHaveMore(boolean haveMore) {
        this.haveMore = haveMore;
    }

    @Override
    public int getItemViewType(int position) {
        // 如果有更多,且是最后一个,则为footer类型
        return haveMore && position == getItemCount() - 1
                ? VIEW_TYPE_FOOTER : VIEW_TYPE_CONTENT;
    }

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
        return datas.size() + (haveMore ? 1 : 0);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == VIEW_TYPE_FOOTER) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.footer_progress, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_grid_gif, parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_FOOTER) {
            return;
        }

        GifInfo gifInfo = datas.get(position);
        holder.tv_title.setText(gifInfo.getTitle());
        Glide.with(context)
                .load(gifInfo.getImgUrl())
                .crossFade()
                .centerCrop()
                .into(holder.iv_gif);
    }

}
