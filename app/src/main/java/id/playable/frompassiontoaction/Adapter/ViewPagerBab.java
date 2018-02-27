package id.playable.frompassiontoaction.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import id.playable.frompassiontoaction.Fragment.BabFragment;

/**
 * Created by Zuhri Utama on 7/15/2015.
 */
public class ViewPagerBab extends FragmentStatePagerAdapter  {
    public ViewPagerBab(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        BabFragment bab = new BabFragment();

        Bundle data = new Bundle();
        data.putInt("pos", position+1);
        bab.setArguments(data);

        return bab;

    }

    @Override
    public int getCount() {
        return 3;
    }

}
