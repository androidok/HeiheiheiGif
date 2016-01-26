package com.boredream.hhhgif.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.adapter.GifInfoAdapter;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class SearchFragment extends BaseFragment implements View.OnClickListener {

    private EditText et_search;
    private ImageView iv_clear;
    private TextView tv_clear_his;
    private RecyclerView rv_search_his;

    private GifInfoAdapter adapter;
    private List<GifInfo> infos = new ArrayList<>();

    private int currentPage = 1;
    private boolean isLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(activity, R.layout.frag_search, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        et_search = (EditText) view.findViewById(R.id.et_search);
        iv_clear = (ImageView) view.findViewById(R.id.iv_clear);
        tv_clear_his = (TextView) view.findViewById(R.id.tv_clear_his);
        rv_search_his = (RecyclerView) view.findViewById(R.id.rv_search_his);

        RxTextView.textChanges(et_search)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<CharSequence, String>() {
                    @Override
                    public String call(CharSequence charSequence) {
                        // clear after modify
                        infos.clear();
                        adapter.notifyDataSetChanged();

                        String search = et_search.getText().toString().trim();
                        return search;
                    }
                })
                .debounce(500, TimeUnit.MILLISECONDS)
                .flatMap(new Func1<String, Observable<ListResponse<GifInfo>>>() {
                    @Override
                    public Observable<ListResponse<GifInfo>> call(String s) {
                        // get infos from network
                        Observable<ListResponse<GifInfo>> observable = HttpRequest.getGifByTitle(s, currentPage);
                        return ObservableDecorator.decorate(activity, observable);
                    }
                })
                .subscribe(new Action1<ListResponse<GifInfo>>() {
                    @Override
                    public void call(ListResponse<GifInfo> gifInfoListResponse) {
                        // add infos to adapter
                        infos.addAll(gifInfoListResponse.getResults());
                        adapter.notifyDataSetChanged();
                    }
                });

        iv_clear.setOnClickListener(this);
        tv_clear_his.setOnClickListener(this);

        initRecyclerView();
        adapter = new GifInfoAdapter(activity, infos);
        rv_search_his.setAdapter(adapter);
    }

    private void initRecyclerView() {
        final StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv_search_his.setLayoutManager(staggeredGridLayoutManager);
        rv_search_his.addItemDecoration(new GridSpacingDecorator(DisplayUtils.dp2px(activity, 8)));
        rv_search_his.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                .delay(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResponse<GifInfo>>() {
                    @Override
                    public void call(ListResponse<GifInfo> gifInfos) {
                        isLoading = false;
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
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear:
                et_search.setText("");
                break;
            case R.id.tv_clear_his:

                break;
        }
    }
}
