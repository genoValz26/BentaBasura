package com.android.bentabasura.benta_basura.Page_Adapters;

/**
 * Created by ccs on 10/18/17.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.bentabasura.benta_basura.Fragments.TabFragmentCraft;
import com.android.bentabasura.benta_basura.Fragments.TabFragmentTrash;

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
                TabFragmentCraft tab2 = new TabFragmentCraft();
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