package com.boredream.hhhgif.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.boredream.bdcodehelper.entity.SettingItem;
import com.boredream.bdcodehelper.utils.DisplayUtils;
import com.boredream.bdcodehelper.utils.TitleBuilder;
import com.boredream.bdcodehelper.view.DividerItemDecoration;
import com.boredream.bdcodehelper.view.EmptyItemDecoration;
import com.boredream.hhhgif.R;
import com.boredream.hhhgif.activity.AboutActivity;
import com.boredream.hhhgif.activity.SettingActivity;
import com.boredream.hhhgif.activity.UserInfoEditActivity;
import com.boredream.hhhgif.adapter.MoreRecyclerAdapter;
import com.boredream.hhhgif.base.BaseFragment;
import com.boredream.hhhgif.utils.UserInfoKeeper;

import java.util.ArrayList;
import java.util.List;

public class MoreFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private RecyclerView rv_more;
    private MoreRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(activity, R.layout.frag_more, null);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // 如果未登录进入本页面,然后跳转登录页面成功后返回,此时应该再次更新用户信息
        adapter.setUser(UserInfoKeeper.getCurrentUser());
        adapter.notifyDataSetChanged();
    }

    private void initView(View view) {
        new TitleBuilder(view).setTitleText("更多");

        rv_more = (RecyclerView) view.findViewById(R.id.rv_more);
    }

    private void initData() {
        // more items
        List<SettingItem> items = new ArrayList<>();
        items.add(new SettingItem(
                R.mipmap.ic_info_grey600_24dp,
                "关于",
                null,
                R.mipmap.ic_chevron_right_grey600_24dp
        ));
        items.add(new SettingItem(
                R.mipmap.ic_settings_grey600_24dp,
                "设置",
                null,
                R.mipmap.ic_chevron_right_grey600_24dp
        ));

        adapter = new MoreRecyclerAdapter(items, this);
        rv_more.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                activity, LinearLayoutManager.VERTICAL, false);
        rv_more.setLayoutManager(linearLayoutManager);
        // 每个item之间的分割线
        rv_more.addItemDecoration(new DividerItemDecoration(activity));
        // 每组item之间的分割间隔
        rv_more.addItemDecoration(new EmptyItemDecoration(
                new Integer[]{0, 1}, DisplayUtils.dp2px(activity, 16)));

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case -1:
                // 给特殊的用户信息header位置设为了position=-1的item click事件
                intent2Activity(UserInfoEditActivity.class);
                break;
            case 0:
                intent2Activity(AboutActivity.class);
                break;
            case 1:
                intent2Activity(SettingActivity.class);
                break;
        }
    }
}
