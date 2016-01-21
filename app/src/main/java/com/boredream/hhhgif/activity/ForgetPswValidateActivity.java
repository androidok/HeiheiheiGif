package com.boredream.hhhgif.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.base.BaseActivity;
import com.boredream.hhhgif.net.HttpRequest;
import com.boredream.hhhgif.net.ObservableDecorator;
import com.boredream.hhhgif.utils.DateUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;

public class ForgetPswValidateActivity extends BaseActivity implements View.OnClickListener {

    // 总倒计时60秒
    private static final long TOTCAL_TIME = 60 * DateUtils.ONE_SECOND_MILLIONS;
    // 每次减少1秒
    private static final long COUNT_DOWN_INTERVAL = DateUtils.ONE_SECOND_MILLIONS;

    private EditText et_verification_code;
    private TextView tv_code_info;
    private Button btn_next;

    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psw_validate);

        getExtras();
        initView();
        initData();
    }

    private void getExtras() {
        Intent intent = getIntent();
        password = intent.getStringExtra("password");
    }

    private void initView() {
        initBackTitle("手机号验证");

        et_verification_code = (EditText) findViewById(R.id.et_verification_code);
        tv_code_info = (TextView) findViewById(R.id.tv_code_info);
        btn_next = (Button) findViewById(R.id.btn_next);

        btn_next.setOnClickListener(this);
    }

    private void initData() {
        startCountDown();
    }

    /**
     * 开始倒计时
     */
    private void startCountDown() {
        tv_code_info.setText("60秒");
        tv_code_info.setTextColor(getResources().getColor(R.color.txt_gray));

        // 倒计时开始
        CountDownTimer timer = new CountDownTimer(TOTCAL_TIME, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long l) {
                // 重新获取(60)
                String restTime = (int) (l / COUNT_DOWN_INTERVAL) + "秒";
                tv_code_info.setText(restTime);
            }

            @Override
            public void onFinish() {
                tv_code_info.setText("重新获取");
                tv_code_info.setTextColor(getResources().getColor(R.color.txt_link_blue));
            }
        };
        timer.start();
    }

    private void submit() {
        // validate
        String code = et_verification_code.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        // validate success, do something
        Map<String, Object> params = new HashMap<>();
        params.put("password", password);
        Observable<Object> observable = HttpRequest.getApiService().resetPasswordBySmsCode(code, params);
        showProgressDialog();
        ObservableDecorator.decorate(this, observable)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object user) {
                        dismissProgressDialog();
                        clearIntent2Login();
                        showToast("密码重置成功");
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
                submit();
                break;
        }
    }

}
