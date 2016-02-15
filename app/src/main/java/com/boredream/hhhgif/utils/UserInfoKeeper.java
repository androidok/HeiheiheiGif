package com.boredream.hhhgif.utils;

import android.content.Context;
import android.content.Intent;

import com.boredream.hhhgif.activity.LoginActivity;
import com.boredream.hhhgif.entity.User;

public class UserInfoKeeper {
    public static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
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
