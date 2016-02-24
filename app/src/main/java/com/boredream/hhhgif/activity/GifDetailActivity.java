package com.boredream.hhhgif.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.adapter.GifDetailAdapter;
import com.boredream.hhhgif.adapter.LoadMoreAdapter;
import com.boredream.hhhgif.base.BaseActivity;
import com.boredream.hhhgif.base.BaseEntity;
import com.boredream.hhhgif.entity.Comment;
import com.boredream.hhhgif.entity.Gif;
import com.boredream.hhhgif.entity.ListResponse;
import com.boredream.hhhgif.entity.PageIndex;
import com.boredream.hhhgif.entity.User;
import com.boredream.hhhgif.net.HttpRequest;
import com.boredream.hhhgif.net.ObservableDecorator;
import com.boredream.hhhgif.net.SimpleSubscriber;
import com.boredream.hhhgif.utils.UserInfoKeeper;
import com.boredream.hhhgif.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public class GifDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_WRITE_COMMENT = 110;

    private SwipeRefreshLayout srl_gifdetail;
    private RecyclerView rv_gifdetail;
    private LinearLayout ll_comment;
    private LinearLayout ll_fav;
    private TextView tv_fav;
    private LinearLayout ll_download;

    private LoadMoreAdapter adapter;
    private List<Comment> infos = new ArrayList<>();
    private Gif gif;
    private boolean isFaved;

    private PageIndex pageIndex = new PageIndex(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_detail);
        initExtras();
        initView();

        showProgressDialog();
        loadData(pageIndex.startPage());
    }

    private void initExtras() {
        gif = (Gif) getIntent().getSerializableExtra("gif");
    }

    private void initView() {
        initBackTitle("动态图详情");

        srl_gifdetail = (SwipeRefreshLayout) findViewById(R.id.srl_gifdetail);
        rv_gifdetail = (RecyclerView) findViewById(R.id.rv_gifdetail);
        ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
        ll_fav = (LinearLayout) findViewById(R.id.ll_fav);
        tv_fav = (TextView) findViewById(R.id.tv_fav);
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
                        loadData(pageIndex.nextPage());
                    }
                });
        rv_gifdetail.setAdapter(adapter);

        srl_gifdetail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(pageIndex.startPage());
            }
        });
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        rv_gifdetail.setLayoutManager(linearLayoutManager);
        // 每个item之间的divder线
        rv_gifdetail.addItemDecoration(new DividerItemDecoration(this));
    }

    private void loadData(final int page) {
        Observable<ListResponse<Comment>> observable = HttpRequest.getGifComments(gif.getObjectId(), page);
        ObservableDecorator.decorate(this, observable)
                .subscribe(new SimpleSubscriber<ListResponse<Comment>>(this) {
                    @Override
                    public void onNext(ListResponse<Comment> gifInfos) {
                        srl_gifdetail.setRefreshing(false);
                        dismissProgressDialog();

                        pageIndex.setResponse(adapter, infos, gifInfos.getResults());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        srl_gifdetail.setRefreshing(false);
                        dismissProgressDialog();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (UserInfoKeeper.getCurrentUser() != null) {
            getFavStatus();
        }
    }

    private void getFavStatus() {
        Observable<ListResponse<User>> observable = HttpRequest.getGifFavUsers(gif.getObjectId());
        ObservableDecorator.decorate(this, observable)
                .subscribe(new Action1<ListResponse<User>>() { // doesn't need error handler
                    @Override
                    public void call(ListResponse<User> userListResponse) {
                        isFaved = userListResponse.getResults().contains(UserInfoKeeper.getCurrentUser());
                        tv_fav.setText(isFaved ? "已收藏" : "收藏");
                    }
                });
    }

    private void favGif() {
        if (isFaved) {
            showToast("已收藏过该动态图");
            return;
        }

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
                if (UserInfoKeeper.checkLogin(this)) {
                    Intent intent = new Intent(this, WriteCommentActivity.class);
                    intent.putExtra("gif", gif);
                    startActivityForResult(intent, REQUEST_CODE_WRITE_COMMENT);
                }
                break;
            case R.id.ll_fav:
                if (UserInfoKeeper.checkLogin(this)) {
                    favGif();
                }
                break;
            case R.id.ll_download:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_WRITE_COMMENT:
                showProgressDialog();
                loadData(pageIndex.startPage());
                break;
        }
    }
}
