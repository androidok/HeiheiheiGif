package com.boredream.hhhgif.activity;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.base.BaseActivity;
import com.boredream.hhhgif.fragment.FragmentController;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private FrameLayout fl_content;
    private RadioGroup rg_bottom_tab;
    private RadioButton rb_home;

    private FragmentController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        controller = new FragmentController(this, R.id.fl_content);
        rb_home.setChecked(true);
    }

    private void initView() {
        fl_content = (FrameLayout) findViewById(R.id.fl_content);
        rg_bottom_tab = (RadioGroup) findViewById(R.id.rg_bottom_tab);
        rb_home = (RadioButton) findViewById(R.id.rb_home);
        rg_bottom_tab.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_home:
                controller.showFragment(0);
                break;
            case R.id.rb_search:
                controller.showFragment(1);
                break;
            case R.id.rb_fav:
                controller.showFragment(2);
                break;
            case R.id.rb_settings:
                controller.showFragment(3);
                break;
        }
    }
}
