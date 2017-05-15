package nguyenduchai.cse.com.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nguyenduchai.cse.com.fragment.FragmentGraphViewCustom;
import nguyenduchai.cse.com.fragment.FragmentGraphViewDate;
import nguyenduchai.cse.com.fragment.FragmentGraphViewMonth;
import nguyenduchai.cse.com.fragment.FragmentGraphViewYear;

/**
 * Created by Nguyen Duc Hai on 12/6/2015.
 */
public class AdapterViewGraphView extends FragmentPagerAdapter{
    private String []tableTitle;

    public AdapterViewGraphView(FragmentManager fm, String []tableTitle) {
        super(fm);
        this.tableTitle = tableTitle;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new FragmentGraphViewDate();
                break;
            case 1:
                fragment = new FragmentGraphViewMonth();
                break;
            case 2:
                fragment = new FragmentGraphViewYear();
                break;
            case 3:
                fragment = new FragmentGraphViewCustom();
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tableTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.tableTitle[position];
    }
}
