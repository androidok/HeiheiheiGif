package com.boredream.hhhgif.adapter;

import android.support.v7.widget.RecyclerView;

import com.boredream.bdcodehelper.adapter.LoadMoreAdapter;
import com.boredream.hhhgif.R;

public class GifLoadMoreAdapter extends LoadMoreAdapter {

    public GifLoadMoreAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter, OnLoadMoreListener onLoadMoreListener) {
        // 自定义加载更多progressbar的drawable
        super(recyclerView, adapter, onLoadMoreListener, recyclerView.getContext().getResources().getDrawable(R.drawable.oval_progress));
    }
}
