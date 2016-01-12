package com.boredream.hhhgif.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.adapter.GifInfoAdapter;
import com.boredream.hhhgif.constants.CommonConstants;
import com.boredream.hhhgif.entity.GifInfo;
import com.boredream.hhhgif.entity.ListResponse;
import com.boredream.hhhgif.net.HttpRequest;
import com.boredream.hhhgif.net.ObservableDecorator;
import com.boredream.hhhgif.utils.DisplayUtils;
import com.boredream.hhhgif.utils.TitleBuilder;
import com.boredream.hhhgif.view.GridSpacingDecorator;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public class HomeFragment extends BaseFragment {
    private View view;
    private SwipeRefreshLayout srl_home;
    private RecyclerView rv_home;

    private GifInfoAdapter adapter;
    private List<GifInfo> infos = new ArrayList<>();

    private int currentPage = 1;
    private boolean isLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_home, null);
        initView();
        loadData(1);
        return view;
    }

    private void initView() {
        new TitleBuilder(view).setTitleText("首页");

        srl_home = (SwipeRefreshLayout) view.findViewById(R.id.srl_home);
        rv_home = (RecyclerView) view.findViewById(R.id.rv_home);
        initRecyclerView();
        adapter = new GifInfoAdapter(activity, infos);
        rv_home.setAdapter(adapter);
        srl_home.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(1);
            }
        });

    }

    private void initRecyclerView() {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case GifInfoAdapter.VIEW_TYPE_CONTENT:
                        return 1;
                    case GifInfoAdapter.VIEW_TYPE_FOOTER:
                        return 2;
                    default:
                        return -1;
                }
            }
        });
        rv_home.setLayoutManager(gridLayoutManager);
        rv_home.addItemDecoration(new GridSpacingDecorator(DisplayUtils.dp2px(activity, 8)));
        rv_home.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = gridLayoutManager.getItemCount();
                if (!isLoading && lastVisibleItem >= totalItemCount - 1 && dy > 0 && adapter.isHaveMore()) {
                    loadData(currentPage + 1);
                }
            }
        });
    }

    private void loadData(final int page) {
        showToast("load data ... page = " + page);
        isLoading = true;

        Observable<ListResponse<GifInfo>> observable = HttpRequest.getGifInfos(page);
        ObservableDecorator.decorate(activity, observable)
                .subscribe(new Action1<ListResponse<GifInfo>>() {
                    @Override
                    public void call(ListResponse<GifInfo> gifInfos) {
                        isLoading = false;
                        srl_home.setRefreshing(false);
                        if (gifInfos.getResults().size() > 0) {
                            currentPage = page;
                            infos.addAll(gifInfos.getResults());
                        }

                        adapter.setHaveMore(gifInfos.getResults().size() == CommonConstants.COUNT_OF_PAGE);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

}
