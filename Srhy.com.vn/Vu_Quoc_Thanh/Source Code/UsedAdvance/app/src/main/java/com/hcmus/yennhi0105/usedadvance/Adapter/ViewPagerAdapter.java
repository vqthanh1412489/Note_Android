package com.hcmus.yennhi0105.usedadvance.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 17/03/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> arrayFragment = new ArrayList<Fragment>();
    public ViewPagerAdapter(FragmentManager fm) {
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

    public void addFragment(Fragment fragment) {
        arrayFragment.add(fragment);
    }
}
