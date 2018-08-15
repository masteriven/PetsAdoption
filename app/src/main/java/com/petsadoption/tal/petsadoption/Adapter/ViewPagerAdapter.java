package com.petsadoption.tal.petsadoption.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.petsadoption.tal.petsadoption.Fragment.AreaSearchFragment;
import com.petsadoption.tal.petsadoption.Fragment.NewPetsFragment;
import com.petsadoption.tal.petsadoption.Fragment.SearchPetFragment;

/**
 * Created by Tal on 9/12/2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show SearchPetFragment
                return NewPetsFragment.fragment(0, "בעלי חיים חדשים");
            case 1: // Fragment # 0 - This will show SearchPetFragment different title
                return SearchPetFragment.fragment(1, "חיפוש בעל חיים");
            default:
                return null;
        }


    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show SearchPetFragment
                return "בעלי חיים חדשים";
            case 1: // Fragment # 0 - This will show SearchPetFragment different title
                return "חיפוש בעל חיים";
            default:
                return null;
        }
    }



    @Override
    public int getCount() {
        return 2;
    }



}
