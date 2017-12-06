package com.android.bentabasura.benta_basura.Page_Adapters;

/**
 * Created by ccs on 10/18/17.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.bentabasura.benta_basura.Fragments.Admin_TabFragmentCraft;
import com.android.bentabasura.benta_basura.Fragments.Admin_TabFragmentTrash;


public class Admin_PageAdapterCraftTrash extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public Admin_PageAdapterCraftTrash(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Admin_TabFragmentTrash tab1 = new Admin_TabFragmentTrash();
                return tab1;
            case 1:
                Admin_TabFragmentCraft tab2 = new Admin_TabFragmentCraft();
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