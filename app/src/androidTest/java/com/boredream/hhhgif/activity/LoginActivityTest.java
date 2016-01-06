package com.boredream.hhhgif.activity;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.boredream.hhhgif.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

	@Rule
	public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class, true, false);

	@Test
	public void test() throws InterruptedException {
		Intent intent = new Intent();
		mActivityRule.launchActivity(intent);

		// actions
		onView(withId(R.id.toolbar));
		onView(withId(R.id.et_username)).perform(typeText("bore"), closeSoftKeyboard());
		onView(withId(R.id.et_password)).perform(typeText("123456"), closeSoftKeyboard());
		onView(withId(R.id.btn_login)).perform(click());

		// assertions
		onView(withId(android.R.id.message))
			.inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
			.check(matches(isDisplayed()));

		Thread.sleep(10 * 1000);
	}
}
