package com.boredream.hhhgif.net;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 观察者装饰器
 */
public class ObservableDecorator {

    public static <T> Observable<T> decorate(final Context context, Observable<T> observable) {
        return observable.subscribeOn(Schedulers.newThread())
                .delay(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread()) // FIXME temp for debug
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new ErrorAction1(context));
    }
}
