package id.playable.frompassiontoaction;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.playable.frompassiontoaction.Adapter.ViewPagerBab;

public class ListAudioActivity extends AppCompatActivity {

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.pager)
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_audio);
        ButterKnife.bind(this);

        tabs.addTab(tabs.newTab().setText("BAB 1"));
        tabs.addTab(tabs.newTab().setText("BAB 2"));
        tabs.addTab(tabs.newTab().setText("BAB 3"));


        pager.setOffscreenPageLimit(3);
        ViewPagerBab adapter = new ViewPagerBab(getSupportFragmentManager());

        pager.setCurrentItem(1);
        pager.setAdapter(adapter);
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

    }
}