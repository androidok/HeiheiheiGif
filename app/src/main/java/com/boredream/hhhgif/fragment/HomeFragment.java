package com.boredream.hhhgif.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.adapter.GifInfoAdapter;
import com.boredream.hhhgif.entity.GifInfo;
import com.boredream.hhhgif.entity.ListResponse;
import com.boredream.hhhgif.net.HttpRequest;
import com.boredream.hhhgif.net.ObservableDecorator;
import com.boredream.hhhgif.utils.DisplayUtils;
import com.boredream.hhhgif.widget.GridSpacingDecorator;

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
        final StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv_home.setLayoutManager(staggeredGridLayoutManager);
        rv_home.addItemDecoration(new GridSpacingDecorator(2, DisplayUtils.dp2px(activity, 8)));
        rv_home.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int visibleItemCount;
            int totalItemCount;
            int pastVisibleItems;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                visibleItemCount = staggeredGridLayoutManager.getChildCount();
                totalItemCount = staggeredGridLayoutManager.getItemCount();
                int[] firstVisibleItems = null;
                firstVisibleItems = staggeredGridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                    pastVisibleItems = firstVisibleItems[0];
                }

                if (!isLoading && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    loadData(currentPage + 1);
                }
            }
        });
    }

    private void loadData(final int page) {
        isLoading = true;

        Observable<ListResponse<GifInfo>> observable = HttpRequest.getGifInfos(page);
        ObservableDecorator.decorate(activity, observable)
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.i("DDD", "Observable error override " + throwable.getMessage());
                    }
                })
                .subscribe(new Action1<ListResponse<GifInfo>>() {
                    @Override
                    public void call(ListResponse<GifInfo> gifInfos) {
                        isLoading = false;
                        srl_home.setRefreshing(false);
                        currentPage = page;

                        infos.addAll(gifInfos.getResults());
                        adapter.notifyDataSetChanged();
                    }
                });
    }

}
