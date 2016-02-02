package com.boredream.hhhgif.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.adapter.GifDetailAdapter;
import com.boredream.hhhgif.adapter.LoadMoreAdapter;
import com.boredream.hhhgif.base.BaseActivity;
import com.boredream.hhhgif.base.BaseEntity;
import com.boredream.hhhgif.constants.CommonConstants;
import com.boredream.hhhgif.entity.Comment;
import com.boredream.hhhgif.entity.GifInfo;
import com.boredream.hhhgif.entity.ListResponse;
import com.boredream.hhhgif.net.HttpRequest;
import com.boredream.hhhgif.net.ObservableDecorator;
import com.boredream.hhhgif.utils.TitleBuilder;
import com.boredream.hhhgif.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public class GifDetailActivity extends BaseActivity implements View.OnClickListener {

    private SwipeRefreshLayout srl_gifdetail;
    private RecyclerView rv_gifdetail;
    private LinearLayout ll_comment;
    private LinearLayout ll_fav;
    private LinearLayout ll_download;

    private LoadMoreAdapter adapter;
    private List<Comment> infos = new ArrayList<>();
    private GifInfo gif;

    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_detail);
        initExtras();
        initView();
        loadData(1);
    }

    private void initExtras() {
        gif = (GifInfo) getIntent().getSerializableExtra("gif");
    }

    private void initView() {
        new TitleBuilder(this).setTitleText("动态图详情");

        srl_gifdetail = (SwipeRefreshLayout) findViewById(R.id.srl_gifdetail);
        rv_gifdetail = (RecyclerView) findViewById(R.id.rv_gifdetail);
        ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
        ll_fav = (LinearLayout) findViewById(R.id.ll_fav);
        ll_download = (LinearLayout) findViewById(R.id.ll_download);

        ll_comment.setOnClickListener(this);
        ll_fav.setOnClickListener(this);
        ll_download.setOnClickListener(this);

        GifDetailAdapter gifDetailAdapter = new GifDetailAdapter(this, infos);
        gifDetailAdapter.setGifInfo(gif);
        adapter = new LoadMoreAdapter(rv_gifdetail, gifDetailAdapter,
                new LoadMoreAdapter.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        loadData(currentPage + 1);
                    }
                });
        rv_gifdetail.setAdapter(adapter);

        srl_gifdetail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(1);
            }
        });
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        rv_gifdetail.setLayoutManager(linearLayoutManager);
        // 每个item之间的divder线
        rv_gifdetail.addItemDecoration(new DividerItemDecoration(this));
    }

    private void loadData(final int page) {
        showToast("load data ... page = " + page);

        Observable<ListResponse<Comment>> observable = HttpRequest.getGifComments(gif.getObjectId(), page);
        ObservableDecorator.decorate(this, observable)
                .subscribe(new Action1<ListResponse<Comment>>() {
                    @Override
                    public void call(ListResponse<Comment> gifInfos) {
                        srl_gifdetail.setRefreshing(false);

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
                        srl_gifdetail.setRefreshing(false);
                    }
                });
    }

    private void favGif() {
        showProgressDialog();
        HttpRequest.favGif(this, gif.getObjectId())
                .subscribe(new Action1<BaseEntity>() {
                    @Override
                    public void call(BaseEntity entity) {
                        dismissProgressDialog();

                        showToast("收藏成功");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        dismissProgressDialog();

                        showToast("收藏失败");
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_comment:

                break;
            case R.id.ll_fav:
                favGif();
                break;
            case R.id.ll_download:

                break;
        }
    }

}
