package com.android.bentabasura.benta_basura.Page_Adapters;

/**
 * Created by ccs on 10/18/17.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.bentabasura.benta_basura.Fragments.TabFragmentReservedCraft;
import com.android.bentabasura.benta_basura.Fragments.TabFragmentReservedTrash;


public class PageAdapterReservedItems extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PageAdapterReservedItems(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabFragmentReservedTrash tab1 = new TabFragmentReservedTrash();
                return tab1;
            case 1:
                TabFragmentReservedCraft tab2 = new TabFragmentReservedCraft();
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