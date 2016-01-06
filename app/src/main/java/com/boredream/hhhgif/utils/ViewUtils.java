package com.boredream.hhhgif.utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import c.gg.chuantouhua.c.R;
import c.gg.chuantouhua.c.activity.LoginActivity;
import c.gg.chuantouhua.c.activity.community.UserDeatilActivity;
import c.gg.chuantouhua.c.adapter.BannerPagerAdapter;
import c.gg.chuantouhua.c.adapter.GridImgsAdapter;
import c.gg.chuantouhua.c.constants.UserInfoKeeper;
import c.gg.chuantouhua.c.dialog.ImageDialog;
import c.gg.chuantouhua.c.entity.BaseUserEntity;
import c.gg.chuantouhua.c.entity.PostCommentlistResponse;
import c.gg.chuantouhua.c.entity.UserEntity;

/**
 * Created by boredream on 2015/10/29.
 */
public class ViewUtils {

    /**
     * 设置多图,使用 gridview 布局的调用此方法便捷处理
     *
     * @param picUrls 图片集合,单图的新建一个集合添加集合中
     */
    public static void setImageGridView(final ArrayList<String> picUrls, GridView gv_images) {
        final Context context = gv_images.getContext();

        if (picUrls != null && picUrls.size() > 0) {
            gv_images.setVisibility(View.VISIBLE);

            GridImgsAdapter gvAdapter = new GridImgsAdapter(context, picUrls);
            gv_images.setAdapter(gvAdapter);
        } else {
            gv_images.setVisibility(View.GONE);
        }
    }

    /**
     * 设置多图,最多4张,多余的只显示4张并且显示总数
     *
     * @param picUrls 图片集合,单图的新建一个集合添加集合中
     */
    public static void setMax4ImageGridView(final ArrayList<String> picUrls, View include_4_images) {
        final Context context = include_4_images.getContext();

        GridView gv_user_photos = (GridView) include_4_images.findViewById(R.id.gv_user_photos);

        if (picUrls != null && picUrls.size() > 0) {
            include_4_images.setVisibility(View.VISIBLE);

            GridImgsAdapter gvAdapter = new GridImgsAdapter(context, picUrls);
            gvAdapter.setMaxSize(4);
            gv_user_photos.setAdapter(gvAdapter);
            gv_user_photos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ImageDialog dialog = new ImageDialog(context, R.style.custom_dialog);
                    dialog.setBundle(picUrls, position);
                    dialog.show();
                }
            });
        } else {
            include_4_images.setVisibility(View.GONE);
        }
    }

    /**
     * 设置banner图片UI,包括小圆指示器
     *
     * @param include_banner banner布局,使用include标签引用include_banner
     * @param imgUrls        图片url集合
     * @return BannerPagerAdapter适配器, 方便根据adapter获取item数据用; 无图片时返回null
     */
    public static BannerPagerAdapter setBanner(View include_banner, final ArrayList<String> imgUrls) {
        Context context = include_banner.getContext();

        final ViewPager vp_banner = (ViewPager) include_banner.findViewById(R.id.vp_banner);
        final RadioGroup rg_indicator = (RadioGroup) include_banner.findViewById(R.id.rg_indicator);

        // ViewPager
        BannerPagerAdapter adapter = new BannerPagerAdapter(context, imgUrls);
        vp_banner.setAdapter(adapter);

        // Indicator
        setIndicator(context, imgUrls.size(), vp_banner, rg_indicator);

        return adapter;
    }

    public static void setIndicator(Context context, final int size,
                                    final ViewPager vp_banner, final RadioGroup rg_indicator) {
        // 无图片和只有一张时不要indicator
        if (size <= 1) {
            rg_indicator.setVisibility(View.GONE);
            return;
        }
        rg_indicator.setVisibility(View.VISIBLE);

        vp_banner.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // ViewPager和RadioGroup联动
                if (rg_indicator.getChildCount() > 1) {
                    ((RadioButton) rg_indicator.getChildAt(position % size)).setChecked(true);
                }
            }
        });

        // 根据图片数量添加RadioButton
        rg_indicator.removeAllViews();
        for (int i = 0; i < size; i++) {
            RadioButton rb = new RadioButton(context);
            // TODO 圆形大小
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                    DisplayUtils.dp2px(context, 8), DisplayUtils.dp2px(context, 8));
            if (i > 0) {
                params.setMargins(DisplayUtils.dp2px(context, 8), 0, 0, 0);
            }
            rb.setLayoutParams(params);
            rb.setButtonDrawable(new ColorDrawable());
            rb.setBackgroundResource(R.drawable.shape_oval_yellow_stroke2solid_sel);
            rb.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    // 圆不可点击选择
                    return true;
                }
            });
            rg_indicator.addView(rb);
        }

        // 默认选中第一个
        if (rg_indicator.getChildCount() > 0) {
            ((RadioButton) rg_indicator.getChildAt(0)).setChecked(true);
        }
    }

    public static class UserViewHolder {

        private Context context;
        public ImageView iv_avatar;

        private View convertView;
        public TextView tv_name;
        public TextView tv_des;
        public LinearLayout ll;
        public ImageView iv_gender;
        public TextView tv_charm;
        public TextView tv_distance;

        public UserViewHolder(View convertView) {
            this.context = convertView.getContext();
            this.convertView = convertView;

            iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_des = (TextView) convertView.findViewById(R.id.tv_des);
            ll = (LinearLayout) convertView.findViewById(R.id.ll);
            iv_gender = (ImageView) convertView.findViewById(R.id.iv_gender);
            tv_charm = (TextView) convertView.findViewById(R.id.tv_charm);
            tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
        }

        public void setUserInfo(final UserEntity item) {
            ImageLoader.getInstance().displayImage(item.getAvatar(), iv_avatar,
                    ImageOptHelper.getAvatarOptions());
            tv_name.setText(item.getNickname());
            String signature = item.getSignature();
            // 签名没有不显示
            if (TextUtils.isEmpty(signature)) {
                tv_des.setVisibility(View.GONE);
            } else {
                tv_des.setVisibility(View.VISIBLE);
                tv_des.setText(signature);
            }
            // 背景
            ll.setBackgroundResource(item.isMale()
                    ? R.drawable.correct_light_blue
                    : R.drawable.correct_pink);
            // 性别图标
            iv_gender.setImageResource(item.isMale()
                    ? R.drawable.ic_male
                    : R.drawable.ic_female);
            // 魅力值
            tv_charm.setText(item.getCharm() + "");

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseUserEntity userinfo = UserInfoKeeper.getUserinfo();
                    if (userinfo.getIs_tourist()) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.putExtra("checkLogin", true);
                        context.startActivity(intent);
                    } else {
                        // 不能查看自己详情
                        long user_id = item.getUser_id();
                        if (userinfo.getUser_id() == user_id) {
                            return;
                        }

                        Intent intent = new Intent(context, UserDeatilActivity.class);
                        intent.putExtra("userId", user_id);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public static void setListViewType(Context context, PullToRefreshListView listView) {

        ILoadingLayout startLabels = listView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel(context.getResources().getString(R.string.xlistview_header_hint_normal));
        startLabels.setRefreshingLabel(context.getResources().getString(R.string.xlistview_header_hint_loading));
        startLabels.setReleaseLabel(context.getResources().getString(R.string.xlistview_header_hint_ready));

        ILoadingLayout endLabels = listView.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel(context.getResources().getString(R.string.xlistview_footer_hint_normal));// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel(context.getResources().getString(R.string.xlistview_header_hint_loading));// 刷新时
        endLabels.setReleaseLabel(context.getResources().getString(R.string.xlistview_footer_hint_ready));// 下来达到一定距离时，显示的提示

    }

    /**
     * 初始化vebview
     *
     * @param webView
     */
    public static void setWebView(WebView webView) {
        webView.setWebChromeClient(new WebChromeClient());
        webView.setDrawingCacheEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        // 目前，先将放大缩小按钮置为false，因为web.destory的时候，如果这两个按钮没有dismiss在部分机型会报错。
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 解决webview的适配屏幕问题 -- 设置viewport为可用
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);

        webView.getSettings().setDomStorageEnabled(true);

    }

    /**
     * 隐藏软键盘
     *
     * @param editText
     * @param context
     */
    public static void closeKeyBoard(EditText editText, Context context) {
        ((InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 显示软键盘
     *
     * @param editText
     * @param context
     */
    public static void outPutKeyBoard(EditText editText, Context context) {
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) editText
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 评论列表处理
     *
     * @param context
     * @param bean
     */
    public static SpannableStringBuilder addClickableForPLString(final Context context, final PostCommentlistResponse.CommentList bean) {

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        String reUserName = bean.getRe_is_anonymous() == 1 ? "匿名" : bean.getRe_nickname();
        String content = context.getResources().getString(R.string.repay_) + reUserName
                + ":" + bean.getContent();
        ssb.append(content);
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (bean.getRe_is_anonymous() == 1) {
                    return;
                }

                // 评论对象
                Intent intent = new Intent(context, UserDeatilActivity.class);
                intent.putExtra("userId", bean.getRe_user_id());
                context.startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(context.getResources().getColor(
                        R.color.yellow_little)); // 设置文本颜色
                // 去掉下划线
                ds.setUnderlineText(false);
            }

        }, 2, 2 + reUserName.length(), 0);
        return ssb.append("");
    }

    /**
     * 复制剪切板
     *
     * @param text
     * @param context
     */
    public static void copyString(String text, Context context, boolean isQQ) {
        ClipboardManager cmd = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmd.setText(text);
        if (isQQ) {
            ToastUtils.showSHORTToast(context, R.string.copy_qq);
        } else {

            ToastUtils.showSHORTToast(context, R.string.copy_s);
        }
    }

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * listView 无数据
     *
     * @param context
     * @param iconId
     * @param content
     * @return
     */
    public static View setNodataView(Context context, int iconId, String content) {
        View nodaView = LayoutInflater.from(context).inflate(R.layout.item_nodata, null);
        ImageView iv_nodata = (ImageView) nodaView.findViewById(R.id.iv_nodata);
        TextView tv_nodata = (TextView) nodaView.findViewById(R.id.tv_nodata);
        iv_nodata.setImageResource(iconId);
        tv_nodata.setText(content);
        return nodaView;

    }
}
