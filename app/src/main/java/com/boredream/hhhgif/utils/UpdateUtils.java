package com.boredream.hhhgif.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.boredream.hhhgif.R;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class UpdateUtils {

    /**
     * 检测版本更新
     */
    public static void checkUpdate(final Context context, UmengUpdateListener listener) {
        UmengUpdateAgent.setUpdateCheckConfig(false);
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.Yes: // has update
                        showUpdateConfirmDialog(context, updateInfo);
                        break;
                    case UpdateStatus.No: // has no update
                        ToastUtils.showToast(context, "当前已经是最新版本");
                        break;
                    case UpdateStatus.NoneWifi: // none wifi
                        showNoWifiConfirmDialog(context, updateInfo);
                        break;
                    case UpdateStatus.Timeout: // time out
                        ToastUtils.showToast(context, "网络连接超时，请重新尝试");
                        break;
                }
            }
        });
        UmengUpdateAgent.forceUpdate(context);
    }

    /**
     * 无Wifi状态确认更新对话框
     */
    private static void showNoWifiConfirmDialog(final Context context, final UpdateResponse updateInfo) {
        DialogUtils.showCommonDialog(context, "没有wifi连接，是否继续选择更新？",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showUpdateConfirmDialog(context, updateInfo);
                    }
                });
    }

    /**
     * 显示版本信息
     *
     * @param context
     * @param updateInfo
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
                        ToastUtils.showToast(context, "开始下载安装包...");
                        UmengUpdateAgent.startDownload(context, updateInfo);
                    }
                })
                .setNegativeButton("以后再说", null)
                .show();
    }
}
