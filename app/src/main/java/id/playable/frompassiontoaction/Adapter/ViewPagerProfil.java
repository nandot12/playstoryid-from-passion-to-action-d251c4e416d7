package id.playable.frompassiontoaction.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import id.playable.frompassiontoaction.Fragment.FragmentPengisiSuara;
import id.playable.frompassiontoaction.Fragment.FragmentPenulis;
import id.playable.frompassiontoaction.Fragment.TestimoniFragment;

/**
 * Created by Zuhri Utama on 7/15/2015.
 */
public class ViewPagerProfil extends FragmentStatePagerAdapter  {

 // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created





    public ViewPagerProfil(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new TestimoniFragment();
        }
        else if (position == 1){
            return new FragmentPenulis();
        }
        else {
            return new FragmentPengisiSuara();
        }
    }

    // This method return the titles for the Tabs in the Tab Strip


    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return 3;
    }

}