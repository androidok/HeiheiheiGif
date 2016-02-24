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
import com.boredream.hhhgif.base.BaseEntity;
import com.boredream.hhhgif.entity.FileUploadResponse;
import com.boredream.hhhgif.entity.User;
import com.boredream.hhhgif.net.GlideHelper;
import com.boredream.hhhgif.net.HttpRequest;
import com.boredream.hhhgif.net.ObservableDecorator;
import com.boredream.hhhgif.net.SimpleSubscriber;
import com.boredream.hhhgif.utils.ImageUtils;
import com.boredream.hhhgif.utils.UserInfoKeeper;

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
        GlideHelper.showAvatar(this, currentUser.getAvatar(), iv_avatar);
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

    private void updateUserAvatar(final String avatarUrl) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("avatar", avatarUrl);

        Observable<BaseEntity> observable = HttpRequest.getApiService().updateUserById(
                currentUser.getObjectId(), updateMap);
        ObservableDecorator.decorate(this, observable)
                .subscribe(new SimpleSubscriber<BaseEntity>(this) {
                    @Override
                    public void onNext(BaseEntity entity) {
                        dismissProgressDialog();

                        // update currentUser and show avatar
                        currentUser.setAvatar(avatarUrl);
                        UserInfoKeeper.setCurrentUser(currentUser);
                        showUserAvatar();

                        showToast("头像修改成功");
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
                // TODO set name
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
