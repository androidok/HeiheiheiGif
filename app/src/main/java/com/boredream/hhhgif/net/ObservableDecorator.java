package com.boredream.hhhgif.net;

import android.content.Context;

import com.boredream.hhhgif.utils.ToastUtils;

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
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if(throwable instanceof HttpException) {
                            ToastUtils.showToast(context, "网络错误");
                        }
                    }
                });
    }
}
