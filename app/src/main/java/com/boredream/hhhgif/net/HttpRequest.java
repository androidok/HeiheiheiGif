package com.boredream.hhhgif.net;

import android.os.AsyncTask;

import com.boredream.hhhgif.base.BaseEntity;
import com.boredream.hhhgif.constants.CommonConstants;
import com.boredream.hhhgif.entity.GifInfo;
import com.boredream.hhhgif.entity.ListResponse;
import com.boredream.hhhgif.entity.User;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

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
        Observable<User> userLogin(
                @Query("username") String username,
                @Query("password") String password);

        // 用户注册
        @POST("/users/new")
        Call<User> userRegist(@Body User user);

        // 添加数据
        @POST("/1/classes/GameScore")
        Call<BaseEntity> addGameScore(@Body Object obj);

        // 分页获取动态图数据
        @GET("/1/classes/Gif")
        Observable<ListResponse<GifInfo>> getGifInfo(
                @Query("limit") int perPageCount,
                @Query("skip") int page);
    }

    public static BmobService getApiService() {
        BmobService service = retrofit.create(BmobService.class);
        return service;
    }

    /**
     * 获取动态图数据,分页
     *
     * @param page 从1开始
     */
    public static Observable<ListResponse<GifInfo>> getGifInfos(int page) {
        BmobService service = getApiService();
        return service.getGifInfo(CommonConstants.COUNT_OF_PAGE, (page - 1) * CommonConstants.COUNT_OF_PAGE);
    }

}
