package com.boredream.hhhgif.net;

import com.boredream.hhhgif.constants.CommonConstants;
import com.boredream.hhhgif.utils.AppUtils;
import com.bumptech.glide.load.resource.gif.GifDrawable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Downloader {
    public static void saveGif(GifDrawable gif, Subscriber<File> callback) {
        Observable.just(gif)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<GifDrawable, File>() {
                    @Override
                    public File call(GifDrawable gifDrawable) {
                        try {
                            byte[] data = gifDrawable.getData();
                            String filename = "hhhgif_" + System.currentTimeMillis() + ".gif";
                            return saveFile(data, filename);
                        } catch (IOException e) {
                            // 抛出runtime异常,Subscriber中的onError会捕获处理
                            throw new RuntimeException("动态图保存失败 : " + e.getMessage());
                        }
                    }
                })
                .subscribe(callback);
    }

    /**
     * 保存文件, 需要放在子线程中执行
     *
     * @param data
     * @param filename
     * @return 保存成功返回文件, 失败返回null
     */
    public static File saveFile(byte[] data, String filename) throws IOException {
        File sdPath = AppUtils.getSDPath();
        if (sdPath == null) {
            // SD卡不可用时也定义为IO异常
            throw new IOException("SD卡路径不存在");
        }

        File dir = new File(sdPath, CommonConstants.DIR_NAME);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, filename);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(data);
        fos.close();

        return file;
    }
}
