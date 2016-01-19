package com.boredream.hhhgif.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.base.BaseActivity;
import com.boredream.hhhgif.entity.User;
import com.boredream.hhhgif.net.HttpRequest;
import com.boredream.hhhgif.net.ObservableDecorator;

import rx.Observable;
import rx.functions.Action1;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private TextView tv_forget_psw;
    private LinearLayout ll_regist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        et_username.setText("bore");
        et_password.setText("123456");
    }

    private void initView() {
        initBackTitle("登录");

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_forget_psw = (TextView) findViewById(R.id.tv_forget_psw);
        ll_regist = (LinearLayout) findViewById(R.id.ll_regist);

        btn_login.setOnClickListener(this);
        tv_forget_psw.setOnClickListener(this);
        ll_regist.setOnClickListener(this);
    }

    private void login() {
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();

        Observable<User> observable = HttpRequest.getApiService().login(username, password);
        ObservableDecorator.decorate(this, observable)
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        intent2Activity(MainActivity.class);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        showLog("登录失败 " + throwable.getMessage());
                        showToast("登录失败");
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_forget_psw:
                break;
            case R.id.ll_regist:
                intent2Activity(RegistActivity.class);
                break;
        }
    }
}
