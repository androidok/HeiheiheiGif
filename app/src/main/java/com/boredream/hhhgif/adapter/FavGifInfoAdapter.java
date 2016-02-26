package com.boredream.hhhgif.adapter;

import android.content.Context;
import android.view.View;

import com.boredream.hhhgif.entity.Gif;

import java.util.List;

public class FavGifInfoAdapter extends GifInfoAdapter {

    public FavGifInfoAdapter(Context context, List<Gif> datas) {
        super(context, datas);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        holder.iv_del_img.setVisibility(View.VISIBLE);
        holder.iv_del_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRemoveGifFavListener != null) {
                    onRemoveGifFavListener.onRemoveGifFav(position);
                }
            }
        });
    }

    public interface OnRemoveGifFavListener {
        void onRemoveGifFav(int position);
    }

    private OnRemoveGifFavListener onRemoveGifFavListener;

    public void setOnRemoveGifFavListener(OnRemoveGifFavListener onRemoveGifFavListener) {
        this.onRemoveGifFavListener = onRemoveGifFavListener;
    }
}
