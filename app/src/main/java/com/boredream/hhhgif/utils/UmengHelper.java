package com.boredream.hhhgif.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.boredream.bdcodehelper.utils.DialogUtils;
import com.boredream.bdcodehelper.utils.NetUtils;
import com.boredream.bdcodehelper.utils.ToastUtils;
import com.boredream.hhhgif.R;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * umeng
 */
public class UmengHelper {

    static {
        PlatformConfig.setWeixin("wx4e89f9488edf8902", "f1b32a6bf1fa8415f1464ba97a929ddd");
        PlatformConfig.setSinaWeibo("906049568", "0e00298a1093b4cc6665f455fe8da9db");
        PlatformConfig.setQQZone("1105147805", "kXiKwc5jtxYZAQ2F");
    }

    ////////////////////////////////////////    update   //////////////////////////////////////////

    /**
     * 检测版本更新
     *
     * @param context
     * @param isForceCheck 是否强制检测更新, false-WiFi情况下才提示更新,true-无论什么网络环境都会提示更新
     * @param listener     代理回调,可以在其中进行进度框等相关助理
     */
    public static void checkUpdate(final Context context, final boolean isForceCheck, final UmengUpdateListener listener) {
        // 自定义dialog,没有复制umeng包中的res资源文件,所以关闭其res检测
        UmengUpdateAgent.setUpdateCheckConfig(false);
        // 自定义dialog,不需要umeng自动弹出更新对话框
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.Yes: // has update
                        showUpdateConfirmDialog(context, updateInfo);
                        break;
                    case UpdateStatus.No: // has no update
                        if (isForceCheck) {
                            ToastUtils.showToast(context, "当前已经是最新版本");
                        }
                        break;
                    case UpdateStatus.NoneWifi: // none wifi
                        showUpdateConfirmDialog(context, updateInfo);
                        break;
                    case UpdateStatus.Timeout: // time out
                        if (isForceCheck) {
                            ToastUtils.showToast(context, "网络连接超时，请重新尝试");
                        }
                        break;
                }

                if (listener != null) {
                    listener.onUpdateReturned(updateStatus, updateInfo);
                }
            }
        });

        if (isForceCheck) {
            UmengUpdateAgent.update(context);
        } else {
            UmengUpdateAgent.forceUpdate(context);
        }
    }

    /**
     * 无Wifi状态确认更新对话框
     */
    private static void showNoWifiConfirmDialog(final Context context, final UpdateResponse updateInfo) {
        DialogUtils.showCommonDialog(context, "没有wifi连接，是否继续选择更新？",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startDownload(context, updateInfo);
                    }
                });
    }

    /**
     * 显示更新对话框,包含版本相关信息
     */
    private static void showUpdateConfirmDialog(final Context context, final UpdateResponse updateInfo) {
        String size = FileUtils.formetFileSize(Long.parseLong(updateInfo.target_size));
        String content = String.format(context.getResources().getString(R.string.update_info),
                updateInfo.version, size, updateInfo.updateLog);

        new AlertDialog.Builder(context)
                .setTitle("发现新版本")
                .setMessage(content)
                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (NetUtils.isWifi(context)) {
                            startDownload(context, updateInfo);
                        } else {
                            showNoWifiConfirmDialog(context, updateInfo);
                        }
                    }
                })
                .setNegativeButton("以后再说", null)
                .show();
    }

    private static void startDownload(Context context, UpdateResponse updateInfo) {
        ToastUtils.showToast(context, "开始下载安装包...");
        UmengUpdateAgent.startDownload(context, updateInfo);
    }

    ////////////////////////////////////////    share   //////////////////////////////////////////
    private static ShareAction shareAction;

    public static void share(final Activity activity, String title, String content, UMImage image,
                             final UMShareListener umShareListener) {
        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]{
                SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.SINA,
                SHARE_MEDIA.QQ,
                SHARE_MEDIA.QZONE};

        shareAction = new ShareAction(activity).setDisplayList(displaylist)
                .withTargetUrl("http://www.baidu.com")
                .setListenerList(umShareListener, umShareListener)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        shareAction.setPlatform(share_media).share();
                    }
                });

        if (!TextUtils.isEmpty(title)) {
            shareAction.withTitle(title);
        }
        if (!TextUtils.isEmpty(content)) {
            shareAction.withText(content);
        }
        if (image != null) {
            shareAction.withMedia(image);
        }

        shareAction.open();
    }

}
