package com.boredream.hhhgif.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.adapter.GifInfoAdapter;
import com.boredream.hhhgif.adapter.LoadMoreAdapter;
import com.boredream.hhhgif.base.BaseFragment;
import com.boredream.hhhgif.constants.CommonConstants;
import com.boredream.hhhgif.entity.GifInfo;
import com.boredream.hhhgif.entity.ListResponse;
import com.boredream.hhhgif.entity.User;
import com.boredream.hhhgif.net.HttpRequest;
import com.boredream.hhhgif.net.ObservableDecorator;
import com.boredream.hhhgif.utils.DisplayUtils;
import com.boredream.hhhgif.utils.TitleBuilder;
import com.boredream.hhhgif.utils.UserInfoKeeper;
import com.boredream.hhhgif.view.GridSpacingDecorator;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public class FavFragment extends BaseFragment {
    private View view;
    private SwipeRefreshLayout srl_fav;
    private RecyclerView rv_fav;
    private View ll_no_login;
    private Button btn_login;

    private LoadMoreAdapter adapter;
    private List<GifInfo> infos = new ArrayList<>();

    private int currentPage = 1;
    private User currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_fav, null);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        currentUser = UserInfoKeeper.getCurrentUser();
        srl_fav.setVisibility(currentUser == null ? View.GONE : View.VISIBLE);

        if(currentUser != null && infos.size() == 0 && !srl_fav.isRefreshing()) {
            srl_fav.setRefreshing(true);
            loadData(1);
        }
    }

    private void initView() {
        new TitleBuilder(view).setTitleText("首页");

        srl_fav = (SwipeRefreshLayout) view.findViewById(R.id.srl_fav);
        rv_fav = (RecyclerView) view.findViewById(R.id.rv_fav);
        ll_no_login = view.findViewById(R.id.ll_no_login);
        btn_login = (Button) view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoKeeper.checkLogin(activity);
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        GifInfoAdapter gifInfoAdapter = new GifInfoAdapter(activity, infos);
        adapter = new LoadMoreAdapter(rv_fav, gifInfoAdapter, new LoadMoreAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadData(currentPage + 1);
            }
        });
        rv_fav.setAdapter(adapter);
        srl_fav.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(1);
            }
        });

        final StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv_fav.setLayoutManager(staggeredGridLayoutManager);
        rv_fav.addItemDecoration(new GridSpacingDecorator(DisplayUtils.dp2px(activity, 8)));
    }

    private void loadData(final int page) {
        Observable<ListResponse<GifInfo>> observable = HttpRequest.getFavGifs(currentUser.getObjectId(), currentPage);
        ObservableDecorator.decorate(activity, observable)
                .subscribe(new Action1<ListResponse<GifInfo>>() {
                    @Override
                    public void call(ListResponse<GifInfo> gifInfos) {
                        srl_fav.setRefreshing(false);

                        if(page == 1) {
                            infos.clear();
                        }

                        if (gifInfos.getResults().size() > 0) {
                            currentPage = page;
                            infos.addAll(gifInfos.getResults());
                        }

                        adapter.setStatus(gifInfos.getResults().size() == CommonConstants.COUNT_OF_PAGE
                                ? LoadMoreAdapter.STATUS_HAVE_MORE : LoadMoreAdapter.STATUS_LOADED_ALL);

                        adapter.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        srl_fav.setRefreshing(false);
                    }
                });
    }
}
