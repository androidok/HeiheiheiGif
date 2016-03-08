package com.boredream.hhhgif.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.boredream.hhhgif.activity.LoginActivity;
import com.boredream.hhhgif.base.BaseApplication;
import com.boredream.hhhgif.constants.CommonConstants;
import com.boredream.hhhgif.entity.User;
import com.google.gson.Gson;

public class UserInfoKeeper {
    private static final String SP_KEY_CURRENT_USER = "current_user";
    private static final String SP_KEY_USER_ID = "user_id";
    private static final String SP_KEY_TOKEN = "token";

    private static User currentUser;

    public static User getCurrentUser() {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(
                CommonConstants.SP_NAME, Context.MODE_PRIVATE);
        String userJson = sp.getString(SP_KEY_CURRENT_USER, null);
        if (currentUser == null && !TextUtils.isEmpty(userJson)) {
            currentUser = new Gson().fromJson(userJson, User.class);
        }
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        if (user != null) {
            SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(
                    CommonConstants.SP_NAME, Context.MODE_PRIVATE);
            String userJson = new Gson().toJson(user);
            sp.edit().putString(SP_KEY_CURRENT_USER, userJson).apply();
        }
        currentUser = user;
    }

    public static void clearCurrentUser() {
        currentUser = null;
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(
                CommonConstants.SP_NAME, Context.MODE_PRIVATE);
        sp.edit().remove(SP_KEY_CURRENT_USER).apply();
    }

    public static void saveLoginData(String userid, String token) {
        // 正常逻辑应该是直接用token去获取当前用户信息,不需要id,但是接口没有提供获取当前用户信息接口
        if (TextUtils.isEmpty(userid) || TextUtils.isEmpty(token)) {
            return;
        }

        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(
                CommonConstants.SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(SP_KEY_USER_ID, userid)
                .putString(SP_KEY_TOKEN, token)
                .apply();
    }

    public static String[] getLoginData() {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(
                CommonConstants.SP_NAME, Context.MODE_PRIVATE);
        String userid = sp.getString(SP_KEY_USER_ID, null);
        String token = sp.getString(SP_KEY_TOKEN, null);
        if (TextUtils.isEmpty(userid) || TextUtils.isEmpty(token)) {
            return null;
        }

        String[] loginData = new String[]{userid, token};
        return loginData;
    }

    public static void clearLoginData() {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(
                CommonConstants.SP_NAME, Context.MODE_PRIVATE);
        sp.edit().remove(SP_KEY_USER_ID)
                .remove(SP_KEY_TOKEN)
                .apply();
    }

    public static String getToken() {
        // header用的token,没有时用空字符串,不能为null
        String token = "";
        if (currentUser != null && currentUser.getSessionToken() != null) {
            token = currentUser.getSessionToken();
        }
        return token;
    }

    public static void logout() {
        clearCurrentUser();
        clearLoginData();
    }

    public static boolean checkLogin(Context context) {
        if (currentUser == null) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra("checkLogin", true);
            context.startActivity(intent);
            return false;
        }
        return true;
    }

}
