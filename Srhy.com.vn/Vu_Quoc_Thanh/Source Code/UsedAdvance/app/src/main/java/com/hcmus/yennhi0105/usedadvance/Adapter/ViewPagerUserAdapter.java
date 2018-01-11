package com.hcmus.yennhi0105.usedadvance.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 25/03/2017.
 */

public class ViewPagerUserAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> arrayFragment = new ArrayList<Fragment>();
    private ArrayList<String> arrayTitle = new ArrayList<String>();

    public ViewPagerUserAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return arrayFragment.get(position);
    }

    @Override
    public int getCount() {
        return arrayFragment.size();
    }

    public void AddTab(Fragment fragment, String title) {
        arrayFragment.add(fragment);
        arrayTitle.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return arrayTitle.get(position);
    }
}
