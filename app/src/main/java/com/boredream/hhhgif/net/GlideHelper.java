package com.boredream.hhhgif.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.boredream.hhhgif.R;
import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Glide装饰器
 */
public class GlideHelper {

    public static void showAvatar(Context context, String avatar, ImageView iv) {
        Glide.with(context)
                .load(avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_account_circle_grey600_24dp)
                .error(R.mipmap.ic_account_circle_grey600_24dp)
                .transform(new GlideCircleTransform(context))
                .into(iv);
    }

    public static void showImage(Context context, String imageUrl, ImageView iv) {
        BitmapRequestBuilder<String, Bitmap> requestBuilder = Glide.with(context)
                .load(imageUrl)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        if (iv.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
            requestBuilder.centerCrop();
        } else {
            requestBuilder.fitCenter();
        }

        requestBuilder.into(iv);
    }
}
