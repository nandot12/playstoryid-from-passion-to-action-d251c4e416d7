package id.playable.frompassiontoaction.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.playable.frompassiontoaction.Adapter.ViewPagerProfil;
import id.playable.frompassiontoaction.R;
import id.playable.frompassiontoaction.components.Constants;

/**
 * Created by Zuhri Utama on 7/23/2015.
 */
public class ProfileFragment extends Fragment {

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.pager)
    ViewPager pager;

    Unbinder unbinder;
    private View v;



    CharSequence Titles[] = {"Testimoni","Penulis","Pengisi Suara"} ;
    int icons[] = {R.drawable.titletestimoni, R.drawable.titlepenulis, R.drawable.titlepengisisuara};
    private Handler durationHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        unbinder = ButterKnife.bind(this, v);
        tabs.addTab(tabs.newTab().setText("TESTIMONI"));
        tabs.addTab(tabs.newTab().setText("PENULIS"));
        tabs.addTab(tabs.newTab().setText("PENGISI SUARA"));


        pager.setOffscreenPageLimit(3);
        ViewPagerProfil   adapter = new ViewPagerProfil(getChildFragmentManager());



        // Assigning ViewPager View and setting the adapter

          pager.setCurrentItem(1);
        pager.setAdapter(adapter);
        //tabs.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return v;
    }

    private void openYoutube() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_URL));
        startActivity(browserIntent);
    }

    //handler to change seekBarTime
    private Runnable updateSeekBarTime = new Runnable() {

        public void run() {
            //get current position
            //int timeElapsed = mMediaPlayer.getCurrentPosition();
            //set seekbar progress
            //seekbar.setProgress((int) timeElapsed);
            //repeat yourself that again in 100 miliseconds
            onStartThread(100);
        }
    };

    public void onStartThread(long delayMillis) {
        durationHandler.postDelayed(updateSeekBarTime, delayMillis);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
