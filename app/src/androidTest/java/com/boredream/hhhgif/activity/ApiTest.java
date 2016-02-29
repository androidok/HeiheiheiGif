package com.boredream.hhhgif.activity;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.boredream.hhhgif.entity.Comment;
import com.boredream.hhhgif.entity.User;
import com.boredream.hhhgif.utils.UserInfoKeeper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ApiTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class, true, false);

    @Before
    public void setUserInfo() {
        User user = new User();
        user.setObjectId("jTDo1112");
        user.setUsername("bore");
        UserInfoKeeper.setCurrentUser(user);
    }

    @Test
    public void testAddComment() throws InterruptedException {
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        Comment comment = new Comment();
        comment.setGifId("cb06614952");
        comment.setContent("哈哈哈");
    }

    @Test
    public void testFavGif() throws InterruptedException {


    }
}
