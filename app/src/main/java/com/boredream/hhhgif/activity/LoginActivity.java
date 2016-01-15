package com.boredream.hhhgif.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.base.BaseActivity;
import com.boredream.hhhgif.entity.GifInfo;
import com.boredream.hhhgif.entity.ListResponse;
import com.boredream.hhhgif.net.ErrorAction1;
import com.boredream.hhhgif.net.HttpRequest;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_username;
    private EditText et_password;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        et_username.setText("bore");
        et_password.setText("123456");
    }

    private void initView() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
        }
    }

    private void login() {
//        User user = new User();
//        user.setObjectId("jTDo1112");
//        user.setUsername("bore");
//        UserInfoKeeper.setCurrentUser(user);
//
//        HttpRequest.favGif(LoginActivity.this, "cb06614952",
//                new Action1<BaseEntity>() {
//                    @Override
//                    public void call(BaseEntity entity) {
//                        ToastUtils.showToast(LoginActivity.this, "success " + entity.toString());
//                    }
//                });

        HttpRequest.getFavGifs("yPXrNNNk", 1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new ErrorAction1(this))
                .subscribe(new Action1<ListResponse<GifInfo>>() {
                    @Override
                    public void call(ListResponse<GifInfo> gifInfoListResponse) {
                        Log.i("DDD", "size " + gifInfoListResponse.getResults().size());
                    }
                });

//        String username = et_username.getText().toString();
//        String password = et_password.getText().toString();
//
//        Observable<User> observable = HttpRequest.getApiService().login(username, password);
//        ObservableDecorator.decorate(this, observable)
//                .doOnError(new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        showToast(throwable.getMessage());
//                    }
//                })
//                .subscribe(new Action1<User>() {
//                    @Override
//                    public void call(User user) {
//                        intent2Activity(MainActivity.class);
//                    }
//                });
    }
}
