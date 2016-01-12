package com.boredream.hhhgif.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.entity.GifInfo;
import com.boredream.hhhgif.utils.DisplayUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid_gif, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        GifInfo gifInfo = datas.get(position);
        holder.tv_title.setText(gifInfo.getTitle());
        int[] size = setImageHeight(holder.iv_gif, gifInfo);
        Glide.with(context)
                .load(gifInfo.getThumbnailImgUrl())
                .asBitmap()
                .centerCrop()
                .into(new SimpleTarget<Bitmap>(size[0], size[1]) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        holder.iv_gif.setImageBitmap(resource);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private int[] setImageHeight(ImageView iv, GifInfo gifInfo) {
        int size[] = new int[2];
        // 图片高宽比
        float scale = (float) gifInfo.getHeight() / gifInfo.getWidth();

        // 最大宽高为viewpager的宽高
        int maxWidth = DisplayUtils.dp2px(context, 160);
        int maxHeight = DisplayUtils.dp2px(context, 400);
        int minHeight = DisplayUtils.dp2px(context, 100);

        // 根据图片比例算出ImageView需要高度
        int height = (int) (maxWidth * scale);
        // 最大最小处理
        height = Math.min(height, maxHeight);
        height = Math.max(height, minHeight);

        // set params
        LinearLayout.LayoutParams ivParams = (LinearLayout.LayoutParams) iv.getLayoutParams();
        ivParams.height = height;
        ivParams.width = maxWidth;

        size[0] = maxWidth;
        size[1] = height;
        return size;
    }
}
