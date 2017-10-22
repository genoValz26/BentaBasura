package com.android.bentabasura.benta_basura.Page_Adapters;

/**
 * Created by ccs on 10/18/17.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.bentabasura.benta_basura.Fragments.BoughtItemsCraftTab;
import com.android.bentabasura.benta_basura.Fragments.BoughtItemsTrashTab;

public class PageAdapterBoughtItems extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PageAdapterBoughtItems(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                BoughtItemsTrashTab tab1 = new BoughtItemsTrashTab();
                return tab1;
            case 1:
                BoughtItemsCraftTab tab2 = new BoughtItemsCraftTab();
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