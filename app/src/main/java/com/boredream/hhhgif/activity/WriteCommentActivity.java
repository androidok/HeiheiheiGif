package com.boredream.hhhgif.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.base.BaseActivity;
import com.boredream.hhhgif.base.BaseEntity;
import com.boredream.hhhgif.entity.Comment;
import com.boredream.hhhgif.entity.Gif;
import com.boredream.hhhgif.entity.Pointer;
import com.boredream.hhhgif.entity.User;
import com.boredream.hhhgif.net.HttpRequest;
import com.boredream.hhhgif.net.ObservableDecorator;
import com.boredream.hhhgif.net.SimpleSubscriber;
import com.boredream.hhhgif.utils.UserInfoKeeper;

import rx.Observable;

public class WriteCommentActivity extends BaseActivity {

    private Gif gif;
    private EditText et_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);
        initExtras();
        initView();
    }

    private void initExtras() {
        gif = (Gif) getIntent().getSerializableExtra("gif");
    }

    private void initView() {
        initBackTitle("写评论")
                .setRightText("完成")
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submit();
                    }
                });

        et_content = (EditText) findViewById(R.id.et_content);
    }

    private void submit() {
        // validate
        String content = et_content.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 新建评论对象
        User currentUser = UserInfoKeeper.getCurrentUser();
        currentUser.set__type(Pointer.TYPE);
        currentUser.setClassName("_User");

        Comment comment = new Comment();
        comment.setGifId(gif.getObjectId());
        comment.setUser(currentUser);
        comment.setContent(content);

        // 提交新建的评论对象至服务器
        showProgressDialog();
        Observable<BaseEntity> observable = HttpRequest.addGifComment(comment);
        ObservableDecorator.decorate(this, observable)
                .subscribe(new SimpleSubscriber<BaseEntity>(this) {
                    @Override
                    public void onNext(BaseEntity entity) {
                        dismissProgressDialog();
                        showToast("评论成功");

                        commentSuccess();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                        dismissProgressDialog();
                    }
                });
    }

    /**
     * 评论成功
     */
    private void commentSuccess() {
        // 设置成功结果同时关闭页面
        setResult(RESULT_OK);
        finish();
    }
}
