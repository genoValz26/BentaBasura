package com.android.bentabasura.benta_basura.Page_Adapters;

/**
 * Created by ccs on 10/18/17.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.bentabasura.benta_basura.Fragments.BuyRaw_TabFragmentItemDetails;
import com.android.bentabasura.benta_basura.Fragments.BuyRaw_TabFragmentItemFeedback;

public class PageAdapterItemDetails_BuyRaw extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PageAdapterItemDetails_BuyRaw(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                BuyRaw_TabFragmentItemDetails tab1 = new BuyRaw_TabFragmentItemDetails();
                return tab1;
            case 1:
                BuyRaw_TabFragmentItemFeedback tab2 = new BuyRaw_TabFragmentItemFeedback();
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