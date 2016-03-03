package com.boredream.hhhgif.base;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.boredream.bdcodehelper.utils.DialogUtils;
import com.boredream.bdcodehelper.utils.ToastUtils;
import com.boredream.hhhgif.activity.MainActivity;
import com.boredream.hhhgif.constants.CommonConstants;

public abstract class BaseFragment extends Fragment {

    protected String TAG;

    protected MainActivity activity;
    protected BaseApplication application;
    protected SharedPreferences sp;
    protected Dialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = getClass().getSimpleName();

        activity = (MainActivity) getActivity();
        application = (BaseApplication) activity.getApplication();
        sp = activity.getSharedPreferences(CommonConstants.SP_NAME, Context.MODE_PRIVATE);
        progressDialog = DialogUtils.createProgressDialog(activity);
    }

    protected void intent2Activity(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(activity, tarActivity);
        startActivity(intent);
    }

    protected void showToast(String msg) {
        ToastUtils.showToast(activity, msg, Toast.LENGTH_SHORT);
    }

    protected void showLog(String msg) {
        Log.i(TAG, msg);
    }

    protected void showProgressDialog() {
        progressDialog.show();
    }

    protected void dismissProgressDialog() {
        progressDialog.dismiss();
    }
}
