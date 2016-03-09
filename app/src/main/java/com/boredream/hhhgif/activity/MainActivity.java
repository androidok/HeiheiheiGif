package com.boredream.hhhgif.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RadioButton;

import com.boredream.bdcodehelper.fragment.FragmentController;
import com.boredream.hhhgif.R;
import com.boredream.hhhgif.base.BaseActivity;
import com.boredream.hhhgif.fragment.FavFragment;
import com.boredream.hhhgif.fragment.HomeFragment;
import com.boredream.hhhgif.fragment.MoreFragment;
import com.boredream.hhhgif.fragment.SearchFragment;
import com.boredream.hhhgif.utils.UmengUpdateUtils;

import java.util.ArrayList;
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

        // 如果是退出应用flag,则直接关闭当前页面,不加载UI
        boolean exit = getIntent().getBooleanExtra("exit", false);
        if (exit) {
            finish();
            return;
        }

        initView();
        initData();
    }

    private void initView() {
        rb_home = (RadioButton) findViewById(R.id.rb_home);
        rb_home.setOnClickListener(this);

        // FIXME 这里我自定义了RadioButton控件,但是checked监听失效了,所以使用OnClick监听代替
        findViewById(R.id.rb_search).setOnClickListener(this);
        findViewById(R.id.rb_fav).setOnClickListener(this);
        findViewById(R.id.rb_more).setOnClickListener(this);
    }

    private void initData() {
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new HomeFragment());
        fragments.add(new SearchFragment());
        fragments.add(new FavFragment());
        fragments.add(new MoreFragment());
        controller = new FragmentController(this, R.id.fl_content, fragments);
        // 默认选择第一个首页fragment
        rb_home.setChecked(true);
        controller.showFragment(0);

        // 非强制检测更新,有wifi时提示,不需要额外回调处理
        UmengUpdateUtils.checkUpdate(this, false, null);
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
        // 双击返回键关闭程序
        // 如果两秒重置时间内再次点击返回,则退出程序
        if (doubleBackToExitPressedOnce) {
            exit();
            return;
        }

        doubleBackToExitPressedOnce = true;
        showToast("再按一次返回键关闭程序");
        Observable.just(null)
                .delay(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        // 延迟两秒后重置标志位为false
                        doubleBackToExitPressedOnce = false;
                    }
                });
    }
}
