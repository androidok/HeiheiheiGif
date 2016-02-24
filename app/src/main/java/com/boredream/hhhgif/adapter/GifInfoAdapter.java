package com.boredream.hhhgif.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.activity.GifDetailActivity;
import com.boredream.hhhgif.entity.Gif;
import com.boredream.hhhgif.net.GlideHelper;

import java.util.List;

public class GifInfoAdapter extends RecyclerView.Adapter<GifInfoAdapter.ViewHolder> {

    protected Context context;
    protected List<Gif> datas;

    public GifInfoAdapter(Context context, List<Gif> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_gif;
        public ImageView iv_del_img;
        public TextView tv_title;
        public TextView tv_comment_count;
        public TextView tv_fav_count;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_gif = (ImageView) itemView.findViewById(R.id.iv_gif);
            iv_del_img = (ImageView) itemView.findViewById(R.id.iv_del_img);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_comment_count = (TextView) itemView.findViewById(R.id.tv_comment_count);
            tv_fav_count = (TextView) itemView.findViewById(R.id.tv_fav_count);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid_gif, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Gif gifInfo = datas.get(position);
        holder.tv_title.setText(gifInfo.getTitle());
        holder.tv_comment_count.setText(gifInfo.getCommentCount() + "");
        holder.tv_fav_count.setText(gifInfo.getFavCount() + "");

        GlideHelper.showImage(context, gifInfo.getImgUrl(), holder.iv_gif);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GifDetailActivity.class);
                intent.putExtra("gif", gifInfo);
                v.getContext().startActivity(intent);
            }
        });
    }

}
