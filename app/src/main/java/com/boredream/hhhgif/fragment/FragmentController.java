package com.boredream.hhhgif.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class FragmentController {

	private int containerId;
	private FragmentManager fm;
	private ArrayList<Fragment> fragments;
	
	public FragmentController(AppCompatActivity activity, int containerId) {
		this.containerId = containerId;
		this.fm = activity.getSupportFragmentManager();
		initFragment();
	}

	public void initFragment() {
		fragments = new ArrayList<Fragment>();
		fragments.add(new HomeFragment());
		fragments.add(new SearchFragment());
		fragments.add(new FavFragment());
		fragments.add(new MoreFragment());

		FragmentTransaction ft = fm.beginTransaction();
		for (int i = 0 ; i < fragments.size() ; i++){
			ft.add(containerId, fragments.get(i), String.valueOf(i));
		}
		ft.commit();
	}

	public void showFragment(int position) {
		hideFragments();
		Fragment fragment = fragments.get(position);
		FragmentTransaction ft = fm.beginTransaction();
		ft.show(fragment);
		ft.commit();
	}
	
	public void hideFragments() {
		FragmentTransaction ft = fm.beginTransaction();
		for(Fragment fragment : fragments) {
			if(fragment != null) {
				ft.hide(fragment);
			}
		}
		ft.commit();
	}
	
	public Fragment getFragment(int position) {
		return fragments.get(position);
	}
}