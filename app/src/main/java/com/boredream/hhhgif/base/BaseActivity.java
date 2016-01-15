package com.boredream.hhhgif.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.constants.CommonConstants;
import com.boredream.hhhgif.utils.DialogUtils;
import com.boredream.hhhgif.utils.TitleBuilder;
import com.boredream.hhhgif.utils.ToastUtils;

public class BaseActivity extends AppCompatActivity {

    protected String TAG;
    protected BaseApplication application;
    protected SharedPreferences sp;
    protected Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    private void init() {
        TAG = getClass().getSimpleName();
        application = (BaseApplication) getApplication();
        sp = getSharedPreferences(CommonConstants.SP_NAME, MODE_PRIVATE);
        progressDialog = DialogUtils.createProgressDialog(this);
    }

    /**
     * 左侧有返回键的标题栏
     * <p/>
     * <p>如果在此基础上还要加其他内容,比如右侧有文字按钮,可以获取该方法返回值继续设置其他内容</p>
     *
     * @param title 标题
     */
    protected TitleBuilder initBackTitle(String title) {
        return new TitleBuilder(this)
                .setTitleText(title)
                .setLeftImage(R.mipmap.ic_chevron_left_white)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
    }

    /**
     * 跳转页面,无extra简易型
     *
     * @param tarActivity 目标页面
     */
    protected void intent2Activity(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(this, tarActivity);
        startActivity(intent);
    }

    protected void showToast(String msg) {
        ToastUtils.showToast(this, msg, Toast.LENGTH_SHORT);
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
