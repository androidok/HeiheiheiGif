package com.boredream.hhhgif.net;

import android.content.Context;
import android.util.Log;

import com.boredream.hhhgif.entity.ErrorResponse;
import com.boredream.hhhgif.utils.ToastUtils;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;

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
            ResponseBody responseBody = exception.response().errorBody();
            MediaType type = responseBody.contentType();
            if (type.type().equals("application") && type.subtype().equals("json")) {
                try {
                    ErrorResponse errorResponse = new Gson().fromJson(
                            responseBody.string(), ErrorResponse.class);
                    // TODO custom error info
                    ToastUtils.showToast(context, errorResponse.getError());
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.i("DDD", throwable.getMessage());
            ToastUtils.showToast(context, "网络错误");
        }
    }
}
