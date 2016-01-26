package com.boredream.hhhgif.activity;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.boredream.hhhgif.base.BaseEntity;
import com.boredream.hhhgif.entity.Comment;
import com.boredream.hhhgif.entity.User;
import com.boredream.hhhgif.net.HttpRequest;
import com.boredream.hhhgif.utils.ToastUtils;
import com.boredream.hhhgif.utils.UserInfoKeeper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.functions.Action1;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class ApiTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class, true, false);

    @Test
    public void testAddComment() throws InterruptedException {
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        Comment comment = new Comment();
        comment.setGifId("cb06614952");
        comment.setUser("jTDo1112");
        comment.setContent("哈哈哈");
        HttpRequest.addGifComment(mActivityRule.getActivity(), comment,
                new Action1<BaseEntity>() {
                    @Override
                    public void call(BaseEntity entity) {
                        ToastUtils.showToast(mActivityRule.getActivity(), "success " + entity.toString());

                        // assertions
                        onView(withId(android.R.id.message))
                                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                                .check(matches(withText(startsWith("success"))));
                    }
                });


    }

    @Test
    public void testFavGif() throws InterruptedException {
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        User user = new User();
        user.setObjectId("jTDo1112");
        user.setUsername("bore");
        UserInfoKeeper.setCurrentUser(user);

        HttpRequest.favGif(mActivityRule.getActivity(), "cb06614952",
                new Action1<BaseEntity>() {
                    @Override
                    public void call(BaseEntity entity) {
                        ToastUtils.showToast(mActivityRule.getActivity(), "success " + entity.toString());

                        // assertions
                        onView(withId(android.R.id.message))
                                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                                .check(matches(withText(startsWith("success"))));
                    }
                });


    }
}
