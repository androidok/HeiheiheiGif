package com.boredream.hhhgif.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.adapter.GifInfoAdapter;
import com.boredream.hhhgif.base.BaseFragment;
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

public class FavFragment extends BaseFragment {
    private View view;
    private SwipeRefreshLayout srl_fav;
    private RecyclerView rv_fav;

    private GifInfoAdapter adapter;
    private List<GifInfo> infos = new ArrayList<>();

    private int currentPage = 1;
    private boolean isLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_fav, null);
        initView();
        loadData(1);
        return view;
    }

    private void initView() {
        new TitleBuilder(view).setTitleText("首页");

        srl_fav = (SwipeRefreshLayout) view.findViewById(R.id.srl_fav);
        rv_fav = (RecyclerView) view.findViewById(R.id.rv_fav);
        initRecyclerView();
        adapter = new GifInfoAdapter(activity, infos);
        rv_fav.setAdapter(adapter);
        srl_fav.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(1);
            }
        });
    }

    private void initRecyclerView() {
        final StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv_fav.setLayoutManager(staggeredGridLayoutManager);
        rv_fav.addItemDecoration(new GridSpacingDecorator(DisplayUtils.dp2px(activity, 8)));
        rv_fav.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int pastVisibleItems = -1;
                int visibleItemCount = staggeredGridLayoutManager.getChildCount();
                int totalItemCount = staggeredGridLayoutManager.getItemCount();
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
        showToast("load data ... page = " + page);
        isLoading = true;

        Observable<ListResponse<GifInfo>> observable = HttpRequest.getGifs(page);
        ObservableDecorator.decorate(activity, observable)
                .subscribe(new Action1<ListResponse<GifInfo>>() {
                    @Override
                    public void call(ListResponse<GifInfo> gifInfos) {
                        isLoading = false;
                        srl_fav.setRefreshing(false);
                        if (gifInfos.getResults().size() > 0) {
                            currentPage = page;
                            infos.addAll(gifInfos.getResults());
                        }

                        adapter.setHaveMore(gifInfos.getResults().size() == CommonConstants.COUNT_OF_PAGE);
                        adapter.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        isLoading = false;
                        srl_fav.setRefreshing(false);
                    }
                });
    }
}
