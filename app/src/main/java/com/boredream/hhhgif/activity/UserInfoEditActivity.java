package com.boredream.hhhgif.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.base.BaseActivity;
import com.boredream.hhhgif.entity.FileUploadResponse;
import com.boredream.hhhgif.entity.User;
import com.boredream.hhhgif.net.GlideUtils;
import com.boredream.hhhgif.net.HttpRequest;
import com.boredream.hhhgif.net.ObservableDecorator;
import com.boredream.hhhgif.net.SimpleSubscriber;
import com.boredream.hhhgif.utils.ImageUtils;
import com.boredream.hhhgif.utils.UserInfoKeeper;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

public class UserInfoEditActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_avatar;
    private LinearLayout ll_avatar;
    private TextView tv_username;
    private LinearLayout ll_username;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edit);

        initView();
        initData();
    }

    private void initData() {
        currentUser = UserInfoKeeper.getCurrentUser();
        showUserAvatar();
        tv_username.setText(currentUser.getUsername());
    }

    private void showUserAvatar() {
        GlideUtils.decorator(Glide.with(this).load(currentUser.getAvatar()))
                .placeholder(R.mipmap.ic_account_circle_grey600_24dp)
                .into(iv_avatar);
    }

    private void initView() {
        initBackTitle("修改个人资料");

        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        ll_avatar = (LinearLayout) findViewById(R.id.ll_avatar);
        tv_username = (TextView) findViewById(R.id.tv_username);
        ll_username = (LinearLayout) findViewById(R.id.ll_username);

        ll_avatar.setOnClickListener(this);
        ll_username.setOnClickListener(this);
    }

    private void setAvatarImage(Uri uri) {
        showProgressDialog();

        HttpRequest.fileUpload(this, uri, new SimpleSubscriber<FileUploadResponse>(this) {
            @Override
            public void onNext(FileUploadResponse fileUploadResponse) {
                // update user avatar to server
                updateUserAvatar(HttpRequest.FILE_HOST + fileUploadResponse.getUrl());
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                dismissProgressDialog();
            }
        });
    }

    private void updateUserAvatar(String avatarUrl) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("avatar", avatarUrl);

        Observable<User> observable = HttpRequest.getApiService().updateUserById(
                currentUser.getObjectId(), updateMap);
        ObservableDecorator.decorate(this, observable)
                .subscribe(new SimpleSubscriber<User>(this) {
                    @Override
                    public void onNext(User user) {
                        dismissProgressDialog();

                        // update currentUser and show avatar
                        currentUser.setAvatar(user.getAvatar());
                        UserInfoKeeper.setCurrentUser(currentUser);
                        showUserAvatar();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dismissProgressDialog();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_avatar:
                ImageUtils.showImagePickDialog(this);
                break;
            case R.id.ll_username:
                // FIXME test
                Map<String, Object> updateMap = new HashMap<>();
                updateMap.put("avatar", "http://file.bmob.cn/M03/B0/A1/oYYBAFbNGraAfn8aAAEDGXUStq4417.jpg");

                Observable<User> observable = HttpRequest.getApiService().updateUserById(
                        currentUser.getObjectId(), updateMap);
                ObservableDecorator.decorate(this, observable)
                        .subscribe(new SimpleSubscriber<User>(this) {

                            @Override
                            public void onNext(User user) {
                                dismissProgressDialog();

                                // update currentUser and show avatar
                                currentUser.setAvatar(user.getAvatar());
                                UserInfoKeeper.setCurrentUser(currentUser);
                                showUserAvatar();
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                super.onError(throwable);
                                dismissProgressDialog();
                            }
                        });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        Uri uri;
        switch (requestCode) {
            case ImageUtils.REQUEST_CODE_FROM_ALBUM:
                uri = data.getData();
//                ImageUtils.cropImage(this, uri);

                setAvatarImage(uri);
                break;
            case ImageUtils.REQUEST_CODE_FROM_CAMERA:
                uri = ImageUtils.imageUriFromCamera;
//                ImageUtils.cropImage(this, uri);

                setAvatarImage(uri);
                break;
            case ImageUtils.REQUEST_CODE_CROP_IMAGE:
                setAvatarImage(ImageUtils.cropImageUri);
                break;
        }
    }

}
