package com.boredream.hhhgif.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.base.BaseActivity;
import com.boredream.hhhgif.net.HttpRequest;
import com.boredream.hhhgif.net.ObservableDecorator;

import rx.Observable;
import rx.functions.Action1;

public class RegistActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_username;
    private EditText et_password;
    private Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initView();
    }

    private void initView() {
        initBackTitle("手机号注册");

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_next = (Button) findViewById(R.id.btn_next);

        btn_next.setOnClickListener(this);
    }

    private void next() {
        // validate
        final String username = et_username.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        final String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            Toast.makeText(this, "请设置登录密码，不少于6位", Toast.LENGTH_SHORT).show();
            return;
        }

        // validate success, do something
        showProgressDialog();
        Observable<Object> observable = HttpRequest.getApiService().requestSmsCode(username);
        ObservableDecorator.decorate(this, observable)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        dismissProgressDialog();

                        Intent intent = new Intent(RegistActivity.this, RegistValidateActivity.class);
                        intent.putExtra("phone", username);
                        intent.putExtra("password", password);
                        startActivity(intent);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        dismissProgressDialog();
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                next();
                break;
        }
    }
}
