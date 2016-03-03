package com.boredream.hhhgif.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.boredream.bdcodehelper.utils.AppUtils;
import com.boredream.bdcodehelper.view.DividerItemDecoration;
import com.boredream.hhhgif.R;
import com.boredream.hhhgif.adapter.SettingRecyclerAdapter;
import com.boredream.hhhgif.base.BaseActivity;
import com.boredream.hhhgif.entity.MoreItem;
import com.boredream.hhhgif.utils.UmengHelper;
import com.boredream.hhhgif.utils.UserInfoKeeper;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private RecyclerView rv_setting;
    private Button btn_logout;

    private SettingRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initData();
    }

    private void initView() {
        initBackTitle("设置");

        rv_setting = (RecyclerView) findViewById(R.id.rv_setting);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(this);
    }

    private void initData() {
        // more items
        List<MoreItem> items = new ArrayList<>();
        MoreItem item1 = new MoreItem(R.mipmap.ic_cached_grey600_24dp, "检查更新");
        item1.rightText = AppUtils.getAppVersionName(this);
        items.add(item1);
        items.add(new MoreItem(R.mipmap.ic_announcement_grey600_24dp, "反馈"));

        adapter = new SettingRecyclerAdapter(items, this);
        rv_setting.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        rv_setting.setLayoutManager(linearLayoutManager);
        // 每个item之间的divder线
        rv_setting.addItemDecoration(new DividerItemDecoration(this));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                showProgressDialog();
                UmengHelper.checkUpdate(this, true, new UmengUpdateListener() {
                    @Override
                    public void onUpdateReturned(int i, UpdateResponse updateResponse) {
                        dismissProgressDialog();
                    }
                });
                break;
            case 1:
                intent2Activity(FeedBackActivity.class);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                UserInfoKeeper.logout();
                clearIntent2Login();
                break;
        }
    }
}
