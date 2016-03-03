package com.boredream.hhhgif.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.bdcodehelper.utils.DialogUtils;
import com.boredream.bdcodehelper.utils.ImageUtils;
import com.boredream.bdcodehelper.view.DividerItemDecoration;
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
import com.boredream.hhhgif.net.Downloader;
import com.boredream.hhhgif.net.HttpRequest;
import com.boredream.hhhgif.net.ObservableDecorator;
import com.boredream.hhhgif.net.SimpleSubscriber;
import com.boredream.hhhgif.utils.FileUtils;
import com.boredream.hhhgif.utils.UserInfoKeeper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public class GifDetailActivity extends BaseActivity implements View.OnClickListener, GifDetailAdapter.OnGifLoadedListener {

    private static final int REQUEST_CODE_WRITE_COMMENT = 110;

    private SwipeRefreshLayout srl_gifdetail;
    private RecyclerView rv_gifdetail;
    private LinearLayout ll_comment;
    private LinearLayout ll_fav;
    private TextView tv_fav;
    private LinearLayout ll_download;
    private TextView tv_download;

    private GifDetailAdapter gifDetailAdapter;
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
        tv_download = (TextView) findViewById(R.id.tv_download);

        ll_comment.setOnClickListener(this);
        ll_fav.setOnClickListener(this);
        ll_download.setOnClickListener(this);

        gifDetailAdapter = new GifDetailAdapter(this, infos);
        gifDetailAdapter.setOnGifLoadedListener(this);
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

    @Override
    protected void onStart() {
        super.onStart();
        checkDownloadStatus();
    }

    @Override
    public void onGifLoaded() {
        checkDownloadStatus();
    }

    /**
     * 验证下载情况
     */
    private void checkDownloadStatus() {
        boolean isDownloaded = FileUtils.isExist(FileUtils.genGifFilename(gif));
        if (isDownloaded) {
            // 图片文件已下载
            tv_download.setTextColor(getResources().getColor(R.color.txt_gray));
            tv_download.setText("已下载");
        } else {
            // 如果未下载,则判断当前图片是否已经加载
            if (gifDetailAdapter.loadedGif == null) {
                // 未加载图片
                tv_download.setTextColor(getResources().getColor(R.color.txt_light_gray));
            } else {
                // 已加载图片
                tv_download.setTextColor(getResources().getColor(R.color.txt_gray));
            }
            tv_download.setText("下载");
        }
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
                        showFavStatus(userListResponse.getResults().contains(UserInfoKeeper.getCurrentUser()));
                    }
                });
    }

    private void showFavStatus(boolean isFaved) {
        this.isFaved = isFaved;
        tv_fav.setText(isFaved ? "已收藏" : "收藏");
    }

    private void favGif() {
        showProgressDialog();
        HttpRequest.favGif(gif.getObjectId())
                .subscribe(new Action1<BaseEntity>() {
                    @Override
                    public void call(BaseEntity entity) {
                        dismissProgressDialog();

                        showToast("收藏成功");
                        showFavStatus(true);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        dismissProgressDialog();

                        // TODO 判断如果是重复收藏则也视为成功

                        showToast("收藏失败");
                        showFavStatus(false);
                    }
                });
    }

    private void removeGifFav() {
        showProgressDialog();
        Observable<BaseEntity> observable = HttpRequest.removeFavGif(gif.getObjectId());
        ObservableDecorator.decorate(this, observable)
                .subscribe(new SimpleSubscriber<BaseEntity>(this) {
                    @Override
                    public void onNext(BaseEntity entity) {
                        dismissProgressDialog();

                        showToast("取消收藏成功");
                        showFavStatus(false);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                        dismissProgressDialog();

                        // TODO 判断如果是重复取消则也视为取消成功
                        showToast("取消收藏失败");
                        showFavStatus(true);
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
                    if (isFaved) {
                        removeGifFav();
                    } else {
                        favGif();
                    }
                }
                break;
            case R.id.ll_download:
                if (gifDetailAdapter.loadedGif == null) {
                    showToast("动态图尚未加载完成,暂时无法下载,请耐心等待...");
                    return;
                }

                boolean isDownloaded = FileUtils.isExist(FileUtils.genGifFilename(gif));
                if (isDownloaded) {
                    // 如果已经下载,则用第三方应用打开
                    showOpenImageByOtherAppConcirmDialog();
                    return;
                }

                Downloader.saveGif(this, gif, gifDetailAdapter.loadedGif,
                        new SimpleSubscriber<File>(this) {
                            @Override
                            public void onNext(File file) {
                                showToast("动态图保存成功,保存路径为 " + file.getAbsolutePath());
                                checkDownloadStatus();
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                super.onError(throwable);
                                checkDownloadStatus();
                            }
                        });
                break;
        }
    }

    private void showOpenImageByOtherAppConcirmDialog() {
        DialogUtils.showCommonDialog(this, "该图片已经加载,是否使用其他图片软件进行浏览？",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file = FileUtils.getFile(FileUtils.genGifFilename(gif));
                        ImageUtils.openImageByOtherApp(GifDetailActivity.this, Uri.fromFile(file));
                    }
                });
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
