package com.boredream.umeng;

import android.content.Intent;
import android.os.Bundle;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.umeng.socialize.media.WBShareCallBackActivity;

public class WBShareActivity extends WBShareCallBackActivity{

    public WBShareActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        super.onResponse(baseResponse);
    }
}
