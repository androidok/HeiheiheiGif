package com.boredream.hhhgif.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.boredream.bdcodehelper.utils.DisplayUtils;
import com.boredream.bdcodehelper.utils.TitleBuilder;
import com.boredream.bdcodehelper.view.DividerItemDecoration;
import com.boredream.bdcodehelper.view.EmptyItemDecoration;
import com.boredream.hhhgif.R;
import com.boredream.hhhgif.activity.SettingActivity;
import com.boredream.hhhgif.activity.UserInfoEditActivity;
import com.boredream.hhhgif.adapter.MoreRecyclerAdapter;
import com.boredream.hhhgif.base.BaseFragment;
import com.boredream.hhhgif.entity.MoreItem;
import com.boredream.hhhgif.utils.UmengShareUtils;
import com.boredream.hhhgif.utils.UserInfoKeeper;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

public class MoreFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private RecyclerView rv_more;
    private MoreRecyclerAdapter adapter;
    private IWXAPI api;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(activity, R.layout.frag_more, null);
        initView(view);
        initData();

        api = WXAPIFactory.createWXAPI(activity, "wx5fe70bdd2cbf596b", true);
        api.registerApp("wx5fe70bdd2cbf596b");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter.setUser(UserInfoKeeper.getCurrentUser());
        adapter.notifyDataSetChanged();
    }

    private void initView(View view) {
        new TitleBuilder(view).setTitleText("更多");

        rv_more = (RecyclerView) view.findViewById(R.id.rv_more);
    }

    private void initData() {
        // more items
        List<MoreItem> items = new ArrayList<>();
        items.add(new MoreItem(R.mipmap.ic_info_grey600_24dp, "关于"));
        items.add(new MoreItem(R.mipmap.ic_settings_grey600_24dp, "设置"));

        adapter = new MoreRecyclerAdapter(items, this);
        rv_more.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                activity, LinearLayoutManager.VERTICAL, false);
        rv_more.setLayoutManager(linearLayoutManager);
        // 每个item之间的divder线
        rv_more.addItemDecoration(new DividerItemDecoration(activity));
        // 每组item之间的分割
        rv_more.addItemDecoration(new EmptyItemDecoration(
                new Integer[]{0, 1}, DisplayUtils.dp2px(activity, 16)));

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                intent2Activity(UserInfoEditActivity.class);
                break;
            case 1:
//                intent2Activity(AboutActivity.class);
                UmengShareUtils.share(activity,
                        "嘿嘿嘿动态图分享",
                        "嘿嘿嘿动态图给您分享了一张有意思的GIF动态图片~",
                        null); // TODO, 动态图的截图
                break;
            case 2:
                intent2Activity(SettingActivity.class);
                break;
        }
    }
}
