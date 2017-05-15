package nguyenduchai.cse.com.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nguyenduchai.cse.com.fragment.FragmentReportCustom;
import nguyenduchai.cse.com.fragment.FragmentReportDate;
import nguyenduchai.cse.com.fragment.FragmentReportMonth;
import nguyenduchai.cse.com.fragment.FragmentReportYear;

/**
 * Created by Nguyen Duc Hai on 11/28/2015.
 */
public class AdapterViewPageReport extends FragmentPagerAdapter {
    private String []tableTitle;

    public AdapterViewPageReport(FragmentManager fm, String []tableTitle) {
        super(fm);
        this.tableTitle = tableTitle;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new FragmentReportDate();
                break;
            case 1:
                fragment = new FragmentReportMonth();
                break;
            case 2:
                fragment = new FragmentReportYear();
                break;
            case 3:
                fragment = new FragmentReportCustom();
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
