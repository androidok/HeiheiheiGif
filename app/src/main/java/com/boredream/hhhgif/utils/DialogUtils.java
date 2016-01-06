package com.boredream.hhhgif.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by moyun on 2015/11/11.
 */
public class DialogUtils {

    public static Dialog createProgressDialog(Context context) {
        return createProgressDialog(context, false);
    }

    public static Dialog createProgressDialog(Context context, boolean needCancle) {
        Dialog dialog = new ProgressDialog(context);
        dialog.setCancelable(needCancle);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

}
