package com.boredream.hhhgif.net;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;

import com.boredream.hhhgif.base.BaseEntity;
import com.boredream.hhhgif.constants.CommonConstants;
import com.boredream.hhhgif.entity.Comment;
import com.boredream.hhhgif.entity.FileUploadResponse;
import com.boredream.hhhgif.entity.Gif;
import com.boredream.hhhgif.entity.IncrementOption;
import com.boredream.hhhgif.entity.ListResponse;
import com.boredream.hhhgif.entity.Operation;
import com.boredream.hhhgif.entity.Pointer;
import com.boredream.hhhgif.entity.Relation;
import com.boredream.hhhgif.entity.RelationTo;
import com.boredream.hhhgif.entity.UpdatePswRequest;
import com.boredream.hhhgif.entity.User;
import com.boredream.hhhgif.entity.Where;
import com.boredream.hhhgif.utils.DisplayUtils;
import com.boredream.hhhgif.utils.UserInfoKeeper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.google.gson.Gson;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class HttpRequest {

    // Bmob
//    private static final String HOST = "https://api.bmob.cn";
//    public static final String FILE_HOST = "http://file.bmob.cn/";
//
//    private static final String APP_ID_NAME = "X-Bmob-Application-Id";
//    private static final String APP_KEY_NAME = "X-Bmob-REST-API-Key";
//
//    private static final String APP_ID_VALUE = "a00013136fdecd1ae8b082d217cbdfe1";
//    private static final String API_KEY_VALUE = "20af8ccc5c11bd1a391723bff5fb3ad3";

    // LeanCloud
    private static final String HOST = "https://api.leancloud.cn";
    public static final String FILE_HOST = "";

    private static final String APP_ID_NAME = "X-LC-Id";
    private static final String APP_KEY_NAME = "X-LC-Key";

    private static final String APP_ID_VALUE = "fiAcYdKc5P320bGboETDUOll-gzGzoHsz";
    private static final String API_KEY_VALUE = "mBSR0LSPnj7Te4eg3Fon8af9";

    private static Retrofit retrofit;
    private static OkHttpClient httpClient;

    public static OkHttpClient getHttpClient() {
        return httpClient;
    }

    static {
        // OkHttpClient
        httpClient = new OkHttpClient();

        // 统一添加的Header
        httpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                // -H "Content-Type: application/json" \
                Request request = chain.request().newBuilder()
                        .addHeader(APP_ID_NAME, APP_ID_VALUE)
                        .addHeader(APP_KEY_NAME, API_KEY_VALUE)
                        .build();
                return chain.proceed(request);
            }
        });
        httpClient.networkInterceptors().add(new StethoInterceptor()); // stetho 浏览器调试工具

        // log
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().add(interceptor);

        // Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create()) // gson
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // rxjava 响应式编程
                .client(httpClient)
                .callbackExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                .build();
    }

    public interface BmobService {
        // 登录用户
        @GET("/1/login")
        Observable<User> login(
                @Query("username") String username,
                @Query("password") String password);

        // 手机获取验证码
        @POST("/1/requestSmsCode")
        Observable<Object> requestSmsCode(
                @Body Map<String, Object> mobilePhoneNumber);

        // 手机验证注册
        @POST("/1/users")
        Observable<User> userRegist(
                @Body User user);

        // 忘记密码重置
        @PUT("/1/resetPasswordBySmsCode/{smsCode}")
        Observable<Object> resetPasswordBySmsCode(
                @Path("smsCode") String smsCode,
                @Body Map<String, Object> password);

        // 旧密码修改新密码
        @POST(" /1/updateUserPassword/{objectId}")
        Observable<User> updateUserPassword(
                @Path("smsCode") String smsCode,
                @Body UpdatePswRequest updatePswRequest);

        // 分页获取动态图数据
        @GET("/1/classes/Gif")
        Observable<ListResponse<Gif>> getGifs(
                @Query("limit") int perPageCount,
                @Query("skip") int page);

        // 根据昵称搜索用户
        @GET("/1/classes/_User")
        Observable<ListResponse<User>> getUserByName(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("where") String where);

        // 根据标题搜索动态图
        @GET("/1/classes/Gif")
        Observable<ListResponse<Gif>> getGifByTitle(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("where") String where);

        // 获取动态图对应评论
        @GET("/1/classes/Comment")
        Observable<ListResponse<Comment>> getGifComments(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("where") String where,
                @Query("include") String include);

        // 发送动态图评论
        @POST("/1/classes/Comment")
        Observable<BaseEntity> addGifComment(
                @Body Comment comment);

        // 更新动态图
        @PUT("/1/classes/Gif/{objectId}")
        Observable<BaseEntity> updateGifWithOperation(
                @Path("objectId") String gifId,
                @Body Map<String, Operation> option);

        // 收藏动态图
        @PUT("/1/classes/Gif/{objectId}")
        Observable<BaseEntity> favGif(
                @Path("objectId") String gifId,
                @Body Map<String, Relation> relation);

        // 用户收藏列表
        @GET("/1/classes/Gif")
        Observable<ListResponse<Gif>> getFavGifs(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("where") String where);

        // 动态图收藏用户列表
        @GET("/1/classes/_User")
        Observable<ListResponse<User>> getGifFavUsers(
                @Query("where") String where);

        // 获取用户详情
        @GET("/1/users/{objectId}")
        Observable<User> getUserById(
                @Path("objectId") String userId);

        // 修改用户详情(注意, 提交什么参数修改什么参数)
        @PUT("/1/users/{objectId}")
        Observable<User> updateUserById(
                @Path("objectId") String userId,
                @Body Map<String, Object> updateInfo);

        // 上传图片接口
        @Headers("Content-Type: image/jpeg")
        @POST("/1/files/{fileName}")
        Observable<FileUploadResponse> fileUpload(
                @Path("fileName") String fileName,
                @Body byte[] imageBytes);

    }

    public static BmobService getApiService() {
        BmobService service = retrofit.create(BmobService.class);
        return service;
    }

    /**
     * 获取动态图数据,分页(默认每页数量为CommonConstants.COUNT_OF_PAGE)
     *
     * @param page 从1开始
     */
    public static Observable<ListResponse<Gif>> getGifs(int page) {
        BmobService service = getApiService();
        return service.getGifs(CommonConstants.COUNT_OF_PAGE,
                (page - 1) * CommonConstants.COUNT_OF_PAGE);
    }

    /**
     * 根据昵称模糊搜索用户,分页(默认每页数量为CommonConstants.COUNT_OF_PAGE)
     *
     * @param searchKey 搜索昵称
     * @param page      页数,从1开始
     */
    public static Observable<ListResponse<User>> getUserByName(String searchKey, int page) {
        BmobService service = getApiService();
        String where = "{\"username\":{\"$regex\":\"" + searchKey + ".*\"}}";
        return service.getUserByName(CommonConstants.COUNT_OF_PAGE,
                (page - 1) * CommonConstants.COUNT_OF_PAGE, where);
    }

    /**
     * 根据标题模糊搜索动态图,分页(默认每页数量为CommonConstants.COUNT_OF_PAGE)
     *
     * @param searchKey 搜索title
     * @param page      页数,从1开始
     */
    public static Observable<ListResponse<Gif>> getGifByTitle(String searchKey, int page) {
        BmobService service = getApiService();
        String where = "{\"title\":{\"$regex\":\"" + searchKey + ".*\"}}";
        return service.getGifByTitle(CommonConstants.COUNT_OF_PAGE,
                (page - 1) * CommonConstants.COUNT_OF_PAGE, where);
    }

    /**
     * 获取动态图对应评论,分页(默认每页数量为CommonConstants.COUNT_OF_PAGE)
     *
     * @param gifId 所属动态图id
     * @param page  页数,从1开始
     */
    public static Observable<ListResponse<Comment>> getGifComments(String gifId, int page) {
        BmobService service = getApiService();
        String where = "{\"gifId\":\"" + gifId + "\"}";
        return service.getGifComments(CommonConstants.COUNT_OF_PAGE,
                (page - 1) * CommonConstants.COUNT_OF_PAGE, where, "user");
    }

    /**
     * 发送动态图评论
     *
     * @param comment 评论
     */
    public static Observable<BaseEntity> addGifComment(final Context context, final Comment comment) {
        final BmobService service = getApiService();
        // 顺序调用,先添加评论数据
        return service.addGifComment(comment)
                .flatMap(new Func1<BaseEntity, Observable<BaseEntity>>() {
                    @Override
                    public Observable<BaseEntity> call(BaseEntity entity) {
                        // 再更新动态图评论数
                        Map<String, Operation> option = new HashMap<>();
                        IncrementOption incrementOption = new IncrementOption();
                        incrementOption.setAmount(1);
                        option.put("commentCount", incrementOption);
                        return service.updateGifWithOperation(comment.getGifId(), option);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new ErrorAction1(context));
    }

    /**
     * 收藏动态图
     *
     * @param gifId 动态图id
     */
    public static Observable<BaseEntity> favGif(final Context context, final String gifId) {
        final BmobService service = getApiService();

        User currentUser = UserInfoKeeper.getCurrentUser();

        Pointer user = new Pointer("_User", currentUser.getObjectId());
        Relation userRelation = new Relation(user);

        Map<String, Relation> favUsersRelation = new HashMap<>();
        favUsersRelation.put("favUsers", userRelation);

        return service.favGif(gifId, favUsersRelation)
                .flatMap(new Func1<BaseEntity, Observable<BaseEntity>>() {
                    @Override
                    public Observable<BaseEntity> call(BaseEntity entity) {
                        Map<String, Operation> option = new HashMap<>();
                        IncrementOption incrementOption = new IncrementOption();
                        incrementOption.setAmount(1);
                        option.put("favCount", incrementOption);
                        return service.updateGifWithOperation(gifId, option);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new ErrorAction1(context));
    }

    /**
     * 用户收藏列表,分页(默认每页数量为CommonConstants.COUNT_OF_PAGE)
     *
     * @param userId
     * @param page   页数,从1开始
     */
    public static Observable<ListResponse<Gif>> getFavGifs(String userId, int page) {
        BmobService service = getApiService();

        Where userIdEqaulWhere = new Where();
        userIdEqaulWhere.setClassName("_User");
        HashMap<String, String> userIdMap = new HashMap<>();
        userIdMap.put("objectId", userId);
        userIdEqaulWhere.setWhere(userIdMap);

        Map<String, Where> inQuerymap = new HashMap<>();
        inQuerymap.put(Where.OP_INQUERY, userIdEqaulWhere);

        Map<String, Map<String, Where>> whereMap = new HashMap<>();
        whereMap.put("favUsers", inQuerymap);

        String where = new Gson().toJson(whereMap);
        return service.getFavGifs(CommonConstants.COUNT_OF_PAGE,
                (page - 1) * CommonConstants.COUNT_OF_PAGE, where);
    }

    /**
     * 动态图收藏用户列表,分页(默认每页数量为CommonConstants.COUNT_OF_PAGE)
     *
     * @param gifId
     */
    public static Observable<ListResponse<User>> getGifFavUsers(String gifId) {
        BmobService service = getApiService();

        RelationTo relationTo = new RelationTo();
        relationTo.setKey("favUsers");
        Pointer gifPointer = new Pointer("Gif", gifId);
        relationTo.setObject(gifPointer);

        Map<String, RelationTo> relationToMap = new HashMap<>();
        relationToMap.put(RelationTo.OP_RELATEDTO, relationTo);

        String where = new Gson().toJson(relationToMap);
        return service.getGifFavUsers(where);
    }

    public static void fileUpload(final Context context, Uri uri, final Action1<FileUploadResponse> call, final Action1<Throwable> errorCall) {
        final BmobService service = getApiService();
        final String filename = "img" + System.currentTimeMillis() + ".jpg";

        // get image from local
        int size = DisplayUtils.dp2px(context, 56);
        Glide.with(context).load(uri).asBitmap().toBytes().into(
                new SimpleTarget<byte[]>(size, size) {
                    @Override
                    public void onResourceReady(final byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
                        // upload image byte[]
                        byte[] image = Base64.encode(resource, Base64.DEFAULT);

                        Observable<FileUploadResponse> observable = service.fileUpload(filename, image);
                        ObservableDecorator.decorate(context, observable)
                                .subscribe(call, errorCall);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        errorCall.call(new Throwable("load local file exception"));
                    }
                });
    }

}
