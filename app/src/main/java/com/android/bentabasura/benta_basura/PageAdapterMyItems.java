package com.android.bentabasura.benta_basura;

/**
 * Created by ccs on 10/18/17.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PageAdapterMyItems extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PageAdapterMyItems(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabFragmentTrash tab1 = new TabFragmentTrash();
                return tab1;
            case 1:
                TabFragmentTrash tab2 = new TabFragmentTrash();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}