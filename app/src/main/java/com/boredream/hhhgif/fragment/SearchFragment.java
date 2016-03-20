package com.boredream.hhhgif.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.boredream.bdcodehelper.adapter.LoadMoreAdapter;
import com.boredream.bdcodehelper.entity.PageIndex;
import com.boredream.bdcodehelper.utils.DisplayUtils;
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

    private GifLoadMoreAdapter adapter;
    private List<Gif> infos = new ArrayList<>();

    private String searchKey;
    private PageIndex pageIndex = new PageIndex(1, CommonConstants.COUNT_OF_PAGE);

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

        iv_clear.setOnClickListener(this);

        initRecyclerView();
        initTextChangeListener();
    }

    private void initRecyclerView() {
        final StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv_search_his.setLayoutManager(staggeredGridLayoutManager);
        rv_search_his.addItemDecoration(new GridSpacingDecorator(DisplayUtils.dp2px(activity, 8)));

        // 没有下拉刷新,只有一个加载更多
        GifInfoAdapter gifInfoAdapter = new GifInfoAdapter(activity, infos);
        adapter = new GifLoadMoreAdapter(rv_search_his, gifInfoAdapter, new LoadMoreAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadData(searchKey, pageIndex.toNextPage());
            }
        });
        rv_search_his.setAdapter(adapter);
    }

    /**
     * 初始化搜索监听,设置搜索规则
     */
    private void initTextChangeListener() {
        // 监听输入框文字变化
        RxTextView.textChanges(et_search)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<CharSequence, String>() { // 调用接口前先对数据进行预先处理
                    @Override
                    public String call(CharSequence charSequence) {
                        // 每次变化时,先清空当前列表
                        infos.clear();

                        // 利用加载更多已有的几种状态优化搜索显示效果
                        adapter.setStatus(TextUtils.isEmpty(charSequence)
                                ? LoadMoreAdapter.STATUS_NONE  // 如果文字为空,不做任何显示
                                : LoadMoreAdapter.STATUS_HAVE_MORE);  // 非空,会继续搜索,显示加载更多进度框
                        adapter.notifyDataSetChanged();

                        // 取消订阅上一次的请求回调,防止输入新的内容后显示之前文字对应接口返回的数据
                        if (searchSubscription != null) {
                            searchSubscription.unsubscribe();
                        }

                        // 重置页数信息
                        pageIndex.init();

                        // 获取输入框内文字,返回
                        String key = charSequence.toString().trim();
                        return key;
                    }
                })
                .debounce(500, TimeUnit.MILLISECONDS) // * 防止连续快速输入时造成的多次调用接口
                .subscribe(new SimpleSubscriber<String>(activity) {
                    @Override
                    public void onNext(String s) {
                        // 调用搜索接口
                        loadData(s, pageIndex.toStartPage());
                    }
                });
    }

    private Subscription searchSubscription;

    /**
     * 发起搜索接口请求
     *
     * @param key  搜索关键字
     * @param page 页数
     */
    private void loadData(final String key, final int page) {
        searchKey = key;

        // 关键字为空时,不做搜索
        if (TextUtils.isEmpty(key)) {
            return;
        }

        Observable<ListResponse<Gif>> observable = HttpRequest.getGifByTitle(searchKey, page);
        searchSubscription = ObservableDecorator.decorate(activity, observable)
                .subscribe(new SimpleSubscriber<ListResponse<Gif>>(activity) {
                    @Override
                    public void onNext(ListResponse<Gif> gifInfoListResponse) {
                        // 加载成功后更新数据
                        pageIndex.setResponse(adapter, infos, gifInfoListResponse.getResults());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        // 错误时视为未加载到数据
                        pageIndex.setResponse(adapter, infos, null);
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
