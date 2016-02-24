package com.boredream.hhhgif.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.base.BaseActivity;
import com.boredream.hhhgif.base.BaseEntity;
import com.boredream.hhhgif.entity.User;
import com.boredream.hhhgif.net.HttpRequest;
import com.boredream.hhhgif.net.ObservableDecorator;
import com.boredream.hhhgif.net.SimpleSubscriber;
import com.boredream.hhhgif.utils.UserInfoKeeper;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

public class InputActivity extends BaseActivity {

    private EditText et;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        currentUser = UserInfoKeeper.getCurrentUser();

        initView();
    }

    private void initView() {
        initBackTitle("昵称修改")
                .setRightText("保存")
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submit();
                    }
                });
        et = (EditText) findViewById(R.id.et);
        et.setText(currentUser.getUsername());
    }

    private void submit() {
        // validate
        final String username = et.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("username", username);

        showProgressDialog();
        Observable<BaseEntity> observable = HttpRequest.getApiService().updateUserById(
                currentUser.getObjectId(), updateMap);
        ObservableDecorator.decorate(this, observable)
                .subscribe(new SimpleSubscriber<BaseEntity>(this) {
                    @Override
                    public void onNext(BaseEntity entity) {
                        dismissProgressDialog();

                        // update currentUser and show avatar
                        currentUser.setUsername(username);
                        UserInfoKeeper.setCurrentUser(currentUser);

                        showToast("昵称修改成功");
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                        dismissProgressDialog();
                    }
                });
    }
}
