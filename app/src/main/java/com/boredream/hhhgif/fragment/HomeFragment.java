package com.boredream.hhhgif.fragment;

import android.os.Bundle;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boredream.bdcodehelper.adapter.LoadMoreAdapter;
import com.boredream.bdcodehelper.entity.PageIndex;
import com.boredream.bdcodehelper.utils.DisplayUtils;
import com.boredream.bdcodehelper.utils.TitleBuilder;
import com.boredream.bdcodehelper.view.GridSpacingDecorator;
import com.boredream.hhhgif.R;
import com.boredream.hhhgif.adapter.GifInfoAdapter;
import com.boredream.hhhgif.adapter.GifLoadMoreAdapter;
import com.boredream.hhhgif.base.BaseFragment;
import com.boredream.hhhgif.constants.CommonConstants;
import com.boredream.hhhgif.entity.Gif;
import com.boredream.hhhgif.entity.ListResponse;
import com.boredream.hhhgif.net.HttpRequest;
import com.boredream.hhhgif.net.ObservableDecorator;
import com.boredream.hhhgif.net.SimpleSubscriber;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * 首页全部动态图列表
 */
public class HomeFragment extends BaseFragment {
    private View view;
    private SwipeRefreshLayout srl_home;
    private RecyclerView rv_home;

    private GifLoadMoreAdapter adapter;
    private List<Gif> infos = new ArrayList<>();

    private PageIndex pageIndex = new PageIndex(1, CommonConstants.COUNT_OF_PAGE);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_home, null);
        initView();
        initData();
        return view;
    }

    private void initView() {
        new TitleBuilder(view).setTitleText("首页");

        srl_home = (SwipeRefreshLayout) view.findViewById(R.id.srl_home);
        rv_home = (RecyclerView) view.findViewById(R.id.rv_home);
        initRecyclerView();
        GifInfoAdapter gifInfoAdapter = new GifInfoAdapter(activity, infos);
        adapter = new GifLoadMoreAdapter(rv_home, gifInfoAdapter, new LoadMoreAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                // 列表拉到底部时,加载下一页
                loadData(pageIndex.toNextPage());
            }
        });
        rv_home.setAdapter(adapter);
        srl_home.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 下拉刷新时,重新加载起始页
                loadData(pageIndex.toStartPage());
            }
        });
    }

    private void initData() {
        setRefreshing(true);
        loadData(pageIndex.toStartPage());
    }

    private void setRefreshing(final boolean refreshing) {
        srl_home.post(new Runnable() {
            @Override
            public void run() {
                srl_home.setRefreshing(refreshing);
            }
        });
    }

    private void initRecyclerView() {
        final StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv_home.setLayoutManager(staggeredGridLayoutManager);
        rv_home.addItemDecoration(new GridSpacingDecorator(DisplayUtils.dp2px(activity, 8)));
    }

    /**
     * 加载动态图列表
     *
     * @param page 页数
     */
    private void loadData(final int page) {
        Observable<ListResponse<Gif>> observable = HttpRequest.getGifs(page);
        ObservableDecorator.decorate(activity, observable)
                .subscribe(new SimpleSubscriber<ListResponse<Gif>>(activity) {
                    @Override
                    public void onNext(ListResponse<Gif> gifInfos) {
                        setRefreshing(false);

                        // 加载成功后更新数据
                        pageIndex.setResponse(adapter, infos, gifInfos.getResults());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        setRefreshing(false);
                    }
                });
    }

}
