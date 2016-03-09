package com.boredream.hhhgif.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.BaseMediaObject;
import com.umeng.socialize.media.UMEmoji;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

/**
 * umeng 分享工具类
 */
public class UmengShareUtils {

    // 各个平台的配置
    static {
        //微信
        PlatformConfig.setWeixin("wx5fe70bdd2cbf596b", "f6ad3323507935428b399bfc3524f85c");
        //新浪微博
        PlatformConfig.setSinaWeibo("906049568", "0e00298a1093b4cc6665f455fe8da9db");
        //QQ
        PlatformConfig.setQQZone("1105147805", "kXiKwc5jtxYZAQ2F");
    }

    /**
     * 分享, 支持微信微博QQ三个平台共5种不同分享
     *
     * @param context
     * @param title   标题
     * @param content 内容,不能为空
     * @param media   多媒体数据,可以是UMEmoji, UMImage, UMusic, UMVideo
     */
    public static void share(final Activity context, final String title, final String content, final BaseMediaObject media) {
        final UMShareListener umShareListener = new UMShareListener() {
            @Override
            public void onResult(SHARE_MEDIA platform) {
                Toast.makeText(context, platform.name() + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                Toast.makeText(context, platform.name() + " 分享失败啦", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toast.makeText(context, platform.name() + " 分享取消了", Toast.LENGTH_SHORT).show();
            }
        };

        ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {
            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                ShareAction shareAction = new ShareAction(context)
                        .setPlatform(share_media)
                        .setCallback(umShareListener)
                                // TODO 设置下载地址
                        .withTargetUrl("http://www.baidu.com");
                if (!TextUtils.isEmpty(title)) {
                    shareAction.withTitle(title);
                }
                shareAction.withText(content);

                if (media != null) {
                    if (media instanceof UMEmoji) {
                        shareAction.withMedia((UMEmoji) media);
                    } else if (media instanceof UMImage) {
                        shareAction.withMedia((UMImage) media);
                    } else if (media instanceof UMusic) {
                        shareAction.withMedia((UMusic) media);
                    } else if (media instanceof UMVideo) {
                        shareAction.withMedia((UMVideo) media);
                    }
                }
                shareAction.share();
            }
        };

        new ShareAction(context)
                .setDisplayList(
                        SHARE_MEDIA.SINA,
                        SHARE_MEDIA.QQ,
                        SHARE_MEDIA.QZONE,
                        SHARE_MEDIA.WEIXIN,
                        SHARE_MEDIA.WEIXIN_CIRCLE)
                .setShareboardclickCallback(shareBoardlistener)
                .open();
    }

}
