package com.boredream.hhhgif.net;

import android.content.Context;

import com.boredream.hhhgif.entity.ErrorResponse;
import com.boredream.hhhgif.utils.ToastUtils;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import retrofit.HttpException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 观察者装饰器
 */
public class ObservableDecorator {

    public static <T> Observable<T> decorate(final Context context, Observable<T> observable) {
        return observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(getErrorAction(context));
    }

    public static Action1<Throwable> getErrorAction(final Context context) {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (throwable instanceof HttpException) {
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
                }
                ToastUtils.showToast(context, "网络错误");
            }
        };
    }
}
