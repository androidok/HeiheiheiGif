package com.boredream.hhhgif.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.adapter.GifInfoAdapter;
import com.boredream.hhhgif.adapter.LoadMoreAdapter;
import com.boredream.hhhgif.base.BaseFragment;
import com.boredream.hhhgif.constants.CommonConstants;
import com.boredream.hhhgif.entity.GifInfo;
import com.boredream.hhhgif.entity.ListResponse;
import com.boredream.hhhgif.net.HttpRequest;
import com.boredream.hhhgif.net.ObservableDecorator;
import com.boredream.hhhgif.utils.DisplayUtils;
import com.boredream.hhhgif.view.GridSpacingDecorator;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class SearchFragment extends BaseFragment implements View.OnClickListener {

    private EditText et_search;
    private ImageView iv_clear;
    private RecyclerView rv_search_his;

    private LoadMoreAdapter adapter;
    private List<GifInfo> infos = new ArrayList<>();

    private String searchKey;
    private int currentPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(activity, R.layout.frag_search, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        et_search = (EditText) view.findViewById(R.id.et_search);
        iv_clear = (ImageView) view.findViewById(R.id.iv_clear);
        rv_search_his = (RecyclerView) view.findViewById(R.id.rv_search_his);

        initTextChangeListener();

        iv_clear.setOnClickListener(this);

        initRecyclerView();
        GifInfoAdapter gifInfoAdapter = new GifInfoAdapter(activity, infos);
        adapter = new LoadMoreAdapter(rv_search_his, gifInfoAdapter, new LoadMoreAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadData(currentPage + 1);
            }
        });
        rv_search_his.setAdapter(adapter);
    }

    private Subscription subscription;
    private void sendSearchRequest(final String s) {
        searchKey = s;

        // send request
        Observable<ListResponse<GifInfo>> observable = HttpRequest.getGifByTitle(searchKey, currentPage);
        subscription = ObservableDecorator.decorate(activity, observable)
                .subscribe(new Action1<ListResponse<GifInfo>>() {
                    @Override
                    public void call(ListResponse<GifInfo> gifInfoListResponse) {
                        Log.i("DDD", "gifInfoListResponse " + gifInfoListResponse.getResults().size());

                        // receive response, set data
                        infos.addAll(gifInfoListResponse.getResults());
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void initTextChangeListener() {
        RxTextView.textChanges(et_search)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<CharSequence, String>() {
                    @Override
                    public String call(CharSequence charSequence) {
                        // clear after modify
                        infos.clear();
                        adapter.notifyDataSetChanged();

                        // cancel last request
                        if (subscription != null) {
                            subscription.unsubscribe();
                        }

                        // init search info
                        currentPage = 1;
                        String key = et_search.getText().toString().trim();
                        return key;
                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        // validate empty
                        return !TextUtils.isEmpty(s);
                    }
                })
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        sendSearchRequest(s);
                    }
                });
    }

    private void initRecyclerView() {
        final StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv_search_his.setLayoutManager(staggeredGridLayoutManager);
        rv_search_his.addItemDecoration(new GridSpacingDecorator(DisplayUtils.dp2px(activity, 8)));
    }

    private void loadData(final int page) {
        showToast("load data ... page = " + page);

        Observable<ListResponse<GifInfo>> observable = HttpRequest.getGifByTitle(searchKey, currentPage);
        ObservableDecorator.decorate(activity, observable)
                .subscribe(new Action1<ListResponse<GifInfo>>() {
                    @Override
                    public void call(ListResponse<GifInfo> gifInfos) {
                        if (gifInfos.getResults().size() > 0) {
                            currentPage = page;
                            infos.addAll(gifInfos.getResults());
                        }

                        adapter.setStatus(gifInfos.getResults().size() == CommonConstants.COUNT_OF_PAGE
                                ? LoadMoreAdapter.STATUS_HAVE_MORE : LoadMoreAdapter.STATUS_LOADED_ALL);
                        if (infos.size() == 0) {
                            adapter.setStatus(LoadMoreAdapter.STATUS_NONE);
                        }

                        adapter.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        // do nothing
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear:
                et_search.setText("");
                break;
        }
    }
}
