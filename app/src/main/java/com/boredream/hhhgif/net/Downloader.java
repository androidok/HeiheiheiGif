package com.boredream.hhhgif.net;

import android.content.Context;

import com.boredream.hhhgif.entity.Gif;
import com.boredream.hhhgif.utils.FileUtils;
import com.bumptech.glide.load.resource.gif.GifDrawable;

import java.io.File;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Downloader {

    /**
     * 异步保存动态图文件
     *
     * @param gif         动态图数据实体类
     * @param gifDrawable 动态图图片drawable对象
     * @param callback
     * @param context
     */
    public static void saveGif(final Context context, final Gif gif, GifDrawable gifDrawable, Subscriber<File> callback) {
        Observable.just(gifDrawable)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<GifDrawable, File>() {
                    @Override
                    public File call(GifDrawable gifDrawable) {
                        try {
                            byte[] data = gifDrawable.getData();
                            String filename = FileUtils.genGifFilename(gif);
                            return FileUtils.saveImageFile(context, data, filename);
                        } catch (IOException e) {
                            // 抛出runtime异常,Subscriber中的onError会捕获处理
                            throw new RuntimeException("动态图保存失败 : " + e.getMessage());
                        }
                    }
                })
                .subscribe(callback);
    }
}
