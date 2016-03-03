package com.boredream.hhhgif.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.base.BaseActivity;
import com.boredream.hhhgif.fragment.FragmentController;
import com.boredream.hhhgif.utils.UmengHelper;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private RadioButton rb_home;

    private FragmentController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        controller = new FragmentController(this, R.id.fl_content);
        rb_home.setChecked(true);
        controller.showFragment(0);

        UmengHelper.checkUpdate(this, false, null);
    }

    private void initView() {
        rb_home = (RadioButton) findViewById(R.id.rb_home);
        rb_home.setOnClickListener(this);
        findViewById(R.id.rb_search).setOnClickListener(this);
        findViewById(R.id.rb_fav).setOnClickListener(this);
        findViewById(R.id.rb_more).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_home:
                controller.showFragment(0);
                break;
            case R.id.rb_search:
                controller.showFragment(1);
                break;
            case R.id.rb_fav:
                controller.showFragment(2);
                break;
            case R.id.rb_more:
                controller.showFragment(3);
                break;
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        doubleBackToExitPressedOnce = true;
        showToast("再按一次返回键关闭程序");
        Observable.just(null)
                .delay(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        doubleBackToExitPressedOnce = false;
                    }
                });
    }
}
