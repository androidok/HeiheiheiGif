package com.boredream.hhhgif.net;

import android.content.Context;

import com.boredream.hhhgif.entity.ErrorResponse;
import com.boredream.hhhgif.utils.ToastUtils;
import com.facebook.stetho.common.LogUtil;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;

import retrofit.HttpException;
import rx.Subscriber;

public class SimpleSubscriber<T> extends Subscriber<T> {

    private Context context;

    public SimpleSubscriber(Context context) {
        this.context = context;
    }

    @Override
    public void onCompleted() {
        // sub
    }

    @Override
    public void onError(Throwable throwable) {
        if (throwable instanceof HttpException) {
            // show error toast
            HttpException exception = (HttpException) throwable;
            ResponseBody responseBody = exception.response().errorBody();
            MediaType type = responseBody.contentType();

            // if data type is application/json
            if (type.type().equals("application") && type.subtype().equals("json")) {
                try {
                    // parse error response
                    ErrorResponse errorResponse = new Gson().fromJson(
                            responseBody.string(), ErrorResponse.class);
                    // TODO custom deal error info
                    ToastUtils.showToast(context, errorResponse.getError());
                    LogUtil.i("DDD", errorResponse.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            // TODO deal other net error
            ToastUtils.showToast(context, throwable.getMessage());
            LogUtil.i("DDD", throwable.getMessage());
        }
    }

    @Override
    public void onNext(T t) {
        // sub
    }


}
