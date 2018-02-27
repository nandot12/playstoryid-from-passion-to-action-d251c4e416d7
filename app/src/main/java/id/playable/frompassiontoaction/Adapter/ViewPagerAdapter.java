package id.playable.frompassiontoaction.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import id.playable.frompassiontoaction.components.SlidingTabLayout;

/**
 * Created by Zuhri Utama on 7/15/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter implements SlidingTabLayout.TabIconProvider {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created

    private final ArrayList<Fragment> fragments;

    private int iconRes[];

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], ArrayList<Fragment> fragments, int[] icons) {
        super(fm);

        this.Titles = mTitles;
        this.fragments = fragments;
        this.iconRes = icons;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getPageIconResId(int position) {
        return iconRes[position];
    }
}

