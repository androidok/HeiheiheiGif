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
import com.boredream.hhhgif.adapter.LoadMoreAdapter;
import com.boredream.hhhgif.base.BaseFragment;
import com.boredream.hhhgif.constants.CommonConstants;
import com.boredream.hhhgif.entity.Gif;
import com.boredream.hhhgif.entity.ListResponse;
import com.boredream.hhhgif.net.HttpRequest;
import com.boredream.hhhgif.net.ObservableDecorator;
import com.boredream.hhhgif.net.SimpleSubscriber;
import com.boredream.hhhgif.utils.DisplayUtils;
import com.boredream.hhhgif.utils.TitleBuilder;
import com.boredream.hhhgif.view.GridSpacingDecorator;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class HomeFragment extends BaseFragment {
    private View view;
    private SwipeRefreshLayout srl_home;
    private RecyclerView rv_home;

    private LoadMoreAdapter adapter;
    private List<Gif> infos = new ArrayList<>();

    private int currentPage = 1;

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
        GifInfoAdapter gifInfoAdapter = new GifInfoAdapter(activity, infos);
        adapter = new LoadMoreAdapter(rv_home, gifInfoAdapter,
                new LoadMoreAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadData(currentPage + 1);
            }
        });
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
        rv_home.addItemDecoration(new GridSpacingDecorator(DisplayUtils.dp2px(activity, 8)));
    }

    private void loadData(final int page) {
        Observable<ListResponse<Gif>> observable = HttpRequest.getGifs(page);
        ObservableDecorator.decorate(activity, observable)
                .subscribe(new SimpleSubscriber<ListResponse<Gif>>(activity) {
                    @Override
                    public void onNext(ListResponse<Gif> gifInfos) {
                        srl_home.setRefreshing(false);

                        if(page == 1) {
                            infos.clear();
                        }

                        if (gifInfos.getResults().size() > 0) {
                            currentPage = page;
                            infos.addAll(gifInfos.getResults());
                        }

                        adapter.setStatus(gifInfos.getResults().size() == CommonConstants.COUNT_OF_PAGE
                                ? LoadMoreAdapter.STATUS_HAVE_MORE : LoadMoreAdapter.STATUS_LOADED_ALL);
                        if(infos.size() == 0) {
                            adapter.setStatus(LoadMoreAdapter.STATUS_NONE);
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        srl_home.setRefreshing(false);
                    }
                });
    }

}
