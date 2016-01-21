package com.boredream.hhhgif.net;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.boredream.hhhgif.base.BaseEntity;
import com.boredream.hhhgif.constants.CommonConstants;
import com.boredream.hhhgif.entity.AddRelationOperation;
import com.boredream.hhhgif.entity.Comment;
import com.boredream.hhhgif.entity.FileUploadResponse;
import com.boredream.hhhgif.entity.GifInfo;
import com.boredream.hhhgif.entity.IncrementOption;
import com.boredream.hhhgif.entity.ListResponse;
import com.boredream.hhhgif.entity.Operation;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private static final String HOST = "https://api.bmob.cn";

    private static final String BMOB_APP_ID = "a00013136fdecd1ae8b082d217cbdfe1";
    private static final String BMOB_API_KEY = "20af8ccc5c11bd1a391723bff5fb3ad3";

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
                // -H "X-Bmob-Application-Id: Your Application ID" \
                // -H "X-Bmob-REST-API-Key: Your REST API Key" \
                // -H "Content-Type: application/json" \
                Request request = chain.request().newBuilder()
                        .addHeader("X-Bmob-Application-Id", BMOB_APP_ID)
                        .addHeader("X-Bmob-REST-API-Key", BMOB_API_KEY)
                        .addHeader("Content-Type", "application/json")
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
        Observable<ListResponse<GifInfo>> getGifs(
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
        Observable<ListResponse<GifInfo>> getGifByTitle(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("where") String where);

        // 获取动态图对应评论
        @GET("/1/classes/Comment")
        Observable<ListResponse<Comment>> getGifComments(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("where") String where);

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
        @POST("/1/classes/Gif/{objectId}")
        Observable<BaseEntity> favGif(
                @Path("objectId") String gifId,
                @Body Map<String, Operation> option);

        // 用户收藏列表
        @GET("/1/classes/Gif")
        Observable<ListResponse<GifInfo>> getFavGifs(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
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
        @POST("/1/files/{fileName}")
        @Headers("Content-Type: image/png")
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
    public static Observable<ListResponse<GifInfo>> getGifs(int page) {
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
     * 根据昵称模糊搜索用户,分页(默认每页数量为CommonConstants.COUNT_OF_PAGE)
     *
     * @param searchKey 搜索title
     * @param page      页数,从1开始
     */
    public static Observable<ListResponse<GifInfo>> getGifByTitle(String searchKey, int page) {
        BmobService service = getApiService();
        String where = "{\"title\":{\"$regex\":\"*" + searchKey + "*\"}}";
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
                (page - 1) * CommonConstants.COUNT_OF_PAGE, where);
    }

    /**
     * 发送动态图评论
     *
     * @param comment 评论
     */
    public static void addGifComment(final Context context, final Comment comment,
                                     Action1<BaseEntity> call) {
        final BmobService service = getApiService();
        // 顺序调用,先添加评论数据
        service.addGifComment(comment)
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
                .doOnError(new ErrorAction1(context))
                .subscribe(call);
    }

    /**
     * 收藏动态图
     *
     * @param gifId 动态图id
     */
    public static void favGif(final Context context, final String gifId,
                              Action1<BaseEntity> call) {
        final BmobService service = getApiService();

        User currentUser = UserInfoKeeper.getCurrentUser();

        List<AddRelationOperation.RelationObj> relationObjs = new ArrayList<>();
        AddRelationOperation.RelationObj relationObj = new AddRelationOperation.RelationObj();
        relationObj.set__type(AddRelationOperation.RelationObj.POINTER);
        relationObj.setClassName("_USER");
        relationObj.setObjectId(currentUser.getObjectId());
        relationObjs.add(relationObj);

        AddRelationOperation addRelationOperation = new AddRelationOperation();
        addRelationOperation.setObjects(relationObjs);

        Map<String, Operation> option = new HashMap<>();
        option.put("favUsers", addRelationOperation);

        service.favGif(gifId, option)
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
                .doOnError(new ErrorAction1(context))
                .subscribe(call);
    }

    /**
     * 用户收藏列表,分页(默认每页数量为CommonConstants.COUNT_OF_PAGE)
     *
     * @param userId
     * @param page   页数,从1开始
     */
    public static Observable<ListResponse<GifInfo>> getFavGifs(String userId, int page) {
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

    public static void fileUpload(final Context context, String filepath, final Action1<User> call) {
        final BmobService service = getApiService();
        String filename = "img" + System.currentTimeMillis();
        final String encodeFilename = Base64.encodeToString(filename.getBytes(), Base64.DEFAULT);

        final ErrorAction1 errorAction1 = new ErrorAction1(context);

        // get image from file
        int size = DisplayUtils.dp2px(context, 56);
        Glide.with(context).load(filepath).asBitmap().toBytes().into(
                new SimpleTarget<byte[]>(size, size) {
                    @Override
                    public void onResourceReady(final byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
                        // upload image byte[]
                        service.fileUpload(encodeFilename, resource)
                                .flatMap(new Func1<FileUploadResponse, Observable<User>>() {
                                    @Override
                                    public Observable<User> call(FileUploadResponse fileUploadResponse) {
                                        Log.i("DDD", "upload image size = " + resource.length);

                                        // update user avatar
                                        Map<String, Object> updateMap = new HashMap<>();
                                        updateMap.put("avatar", fileUploadResponse.getUrl());

                                        User currentUser = UserInfoKeeper.getCurrentUser();
                                        return service.updateUserById(currentUser.getObjectId(), updateMap);
                                    }
                                })
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnError(errorAction1)
                                .subscribe();
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        // TODO load local file exception
                        errorAction1.call(new Throwable("load local file exception"));
                    }
                });
    }

}
