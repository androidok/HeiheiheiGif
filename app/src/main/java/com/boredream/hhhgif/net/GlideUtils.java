package com.boredream.hhhgif.net;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Glide装饰器
 */
public class GlideUtils {

    /**
     * 为Glide装饰添加默认设置
     *
     * @param request 使用Glide.with().load 获取DrawableTypeRequest
     * @return 返回对象直接调用into方法设置需要加载的控件
     */
    public static DrawableRequestBuilder decorator(DrawableTypeRequest request) {
        return request.diskCacheStrategy(DiskCacheStrategy.ALL) // 在disk中缓存全部尺寸图片
                .crossFade()
                .centerCrop();
    }

}
