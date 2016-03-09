package com.boredream.hhhgif.net;

import android.content.Context;

import com.boredream.bdcodehelper.utils.ToastUtils;
import com.boredream.hhhgif.entity.ErrorResponse;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;

import retrofit.HttpException;
import rx.Subscriber;

/**
 * 通用订阅者,用于统一处理错误回调
 */
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
            // 如果是Retrofit的Http错误,则转换类型,获取信息
            HttpException exception = (HttpException) throwable;
            ResponseBody responseBody = exception.response().errorBody();
            MediaType type = responseBody.contentType();

            // 如果是application/json类型数据,则解析返回内容
            if (type.type().equals("application") && type.subtype().equals("json")) {
                try {
                    // 这里的返回内容是bmob/AVOS/Parse等RestFul api文档中的错误代码和信息对象
                    // 如果是自己搭建服务器,则这里的错误对象可以替换
                    ErrorResponse errorResponse = new Gson().fromJson(
                            responseBody.string(), ErrorResponse.class);
                    // TODO 统一处理错误,可以根据不同code进行特殊处理,我这里只简单的显示了Toast
                    ToastUtils.showToast(context, errorResponse.getError());
                } catch (Exception e) {
                    ToastUtils.showToast(context, throwable.getMessage());
                }
            }
        } else {
            // TODO 统一处理其他类型错误
            ToastUtils.showToast(context, throwable.getMessage());
        }
    }

    @Override
    public void onNext(T t) {
        // sub
    }


}
