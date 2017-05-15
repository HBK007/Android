package nguyenduchai.cse.com.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nguyenduchai.cse.com.fragment.FragmentExpense;
import nguyenduchai.cse.com.fragment.FragmentIncome;

/**
 * Created by Nguyen Duc Hai on 11/10/2015.
 */
public class AdapterViewPagerTypeItems extends FragmentPagerAdapter {

    private String[] myTabTitles;
    public AdapterViewPagerTypeItems(FragmentManager fm, String[] myTabTitles) {
        super(fm);
        this.myTabTitles = myTabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new FragmentIncome();
                break;
            case 1:
                fragment = new FragmentExpense();
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
