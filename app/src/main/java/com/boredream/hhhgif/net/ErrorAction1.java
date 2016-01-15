package com.boredream.hhhgif.net;

import android.content.Context;
import android.util.Log;

import com.boredream.hhhgif.utils.ToastUtils;

import java.io.IOException;

import retrofit.HttpException;
import rx.functions.Action1;

/**
 * 错误回调处理
 */
public class ErrorAction1 implements Action1<Throwable> {
    private Context context;

    public ErrorAction1(Context context) {
        this.context = context;
    }

    @Override
    public void call(Throwable throwable) {
        if (throwable instanceof HttpException) {
            // show error toast
            HttpException exception = (HttpException) throwable;
            // log detail
            try {
                String errorContent = exception.response().errorBody().string();
                Log.i("DDD", "on retrofit HttpError ... " + errorContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("DDD", throwable.getMessage());
            ToastUtils.showToast(context, "服务器忙,");
        }
    }
}
