package com.boredream.hhhgif.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.boredream.hhhgif.entity.Gif;
import com.boredream.hhhgif.utils.DialogUtils;
import com.boredream.hhhgif.utils.ToastUtils;

import java.util.List;

public class FavGifInfoAdapter extends GifInfoAdapter {

    public FavGifInfoAdapter(Context context, List<Gif> datas) {
        super(context, datas);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        final Gif gifInfo = datas.get(position);
        holder.iv_del_img.setVisibility(View.VISIBLE);
        holder.iv_del_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFavGif(gifInfo);
            }
        });
    }

    private void removeFavGif(Gif gifInfo) {
        DialogUtils.showConfirmDialog(context, "确认删除该收藏动态图？",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtils.showToast(context, "remove fav");
                    }
                });
    }
}
