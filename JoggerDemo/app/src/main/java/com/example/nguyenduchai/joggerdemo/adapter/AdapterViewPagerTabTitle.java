package com.example.nguyenduchai.joggerdemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.nguyenduchai.joggerdemo.fragment.FragmentHome;
import com.example.nguyenduchai.joggerdemo.fragment.FragmentMap;
import com.example.nguyenduchai.joggerdemo.fragment.FragmentMusic;

/**
 * Created by Nguyen Duc Hai on 5/14/2017.
 */

public class AdapterViewPagerTabTitle extends FragmentPagerAdapter {
    private String[] myTabTitles;

    public AdapterViewPagerTabTitle(FragmentManager fm, String[] myTabTitles) {
        super(fm);
        this.myTabTitles = myTabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new FragmentHome();
                break;
            case 1:
                fragment = new FragmentMap();
                break;
            case 2:
                fragment = new FragmentMusic();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return myTabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return myTabTitles[position];
    }
}
