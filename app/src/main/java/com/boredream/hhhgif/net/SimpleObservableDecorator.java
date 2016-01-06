package com.boredream.hhhgif.net;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 观察者装饰器
 */
public class SimpleObservableDecorator {

    public static <T> void decorate(Observable<T> observable, Action1<T> action) {
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action);
    }
}
