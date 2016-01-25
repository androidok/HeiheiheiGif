package com.boredream.hhhgif.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.base.BaseActivity;
import com.boredream.hhhgif.fragment.FragmentController;

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
}
