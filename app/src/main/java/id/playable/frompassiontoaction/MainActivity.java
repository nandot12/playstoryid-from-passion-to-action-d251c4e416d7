package id.playable.frompassiontoaction;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.playable.frompassiontoaction.Adapter.NavAdapter;
import id.playable.frompassiontoaction.Adapter.ViewPagerAdapter;
import id.playable.frompassiontoaction.Fragment.InfoFragment;
import id.playable.frompassiontoaction.Fragment.PlayerFragment;
import id.playable.frompassiontoaction.Fragment.ProfileFragment;
import id.playable.frompassiontoaction.Model.BookmarkModel;
import id.playable.frompassiontoaction.components.Constants;
import id.playable.frompassiontoaction.components.MusicService;
import id.playable.frompassiontoaction.components.SessionManager;
import id.playable.frompassiontoaction.components.SlidingTabLayout;

import static id.playable.frompassiontoaction.Fragment.PlayerFragment.db;
import static id.playable.frompassiontoaction.Fragment.PlayerFragment.timeElapsed;

/**
 * Created by Zuhri Utama on 7/15/2015.
 */
public class MainActivity extends AppCompatActivity {
    //private static final int NOTIFICATION_ID = 1;
    // Declaring Your View and Variables
    Toolbar toolbar;

    // Tabs
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[];
    int icons[] = {R.drawable.iconprofilebook, R.drawable.iconmenumediaplayer, R.drawable.iconlain};
    //int Numboftabs =2;
    ListView listdrawer;

    int nav_menu[];
    String nav_title[];
    @BindView(R.id.gambaratas)
    ImageView gambaratas;
    @BindView(R.id.nameuser)
    TextView nameuser;
    @BindView(R.id.textlogout)
    TextView textlogout;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.menuLayout)
    RelativeLayout menuLayout;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    // Fragment
    PlayerFragment playerFragment;
    ProfileFragment profileFragment;
    InfoFragment infoFragment;

    TextView textViewlogout;

    private BroadcastReceiver receiver;
    private SessionManager session;

    public static final int RC_REQUEST = 115;
    public static final int BAB_REQUEST = 120;
    public static final int BOOKMARK_REQUEST = 125;
    public static final int LOGIN_REQUEST = 130;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        session = new SessionManager(this);

        toolbar = findViewById(R.id.toolbar);
        ImageView img = toolbar.findViewById(R.id.menu);


       setupViewPager();

        Intent service = new Intent(this, MusicService.class);
        service.setAction(Constants.LOG_TAG);
        service.putExtra("resid", 1);
        startService(service);


        nav_menu = new int[]{R.drawable.iconbookmark, R.drawable.openbookmark, R.drawable.daftarisi, R.drawable.phoneicon};
        nav_title = new String[]{"Bookmark Audio", "Buka Bookmark", "Daftar isi", "Kontak Kami"};

        listdrawer = findViewById(R.id.listdrawer);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.END);
            }
        });
        NavAdapter adapter = new NavAdapter(this, nav_menu, nav_title);
        listdrawer.setAdapter(adapter);

        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
            }
        });

        textViewlogout = findViewById(R.id.textlogout);
        if(session.isLoggedIn()) {
            textViewlogout.setText("Keluar Akun");
            nameuser.setText(session.getName());
            Glide.with(this).load(session.getImage()).error(R.drawable.logonavigasi).into(gambaratas);
        }
        else
            textViewlogout.setText("Masuk Akun");

        textViewlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(session.isLoggedIn()) {
                    confirmLogout();
                }else
                    login();
            }
        });

        listdrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mDrawerLayout.closeDrawers();

                if (position == 0) {
                    if (session.isLoggedIn())
                        addBookmark();
                    else
                        confirmLogin();
                } else if (position == 1) {
                    if (session.isLoggedIn())
                        openBookmark();
                    else
                        confirmLogin();
                } else if (position == 2) {
                    startActivityForResult(new Intent(MainActivity.this,ListAudioActivity.class), BAB_REQUEST);
                } else {
                    startActivity(new Intent(MainActivity.this,KontakKamiActivity.class));
                }
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);


        mDrawerToggle.syncState();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra(Constants.MESSAGE);
                if (s.equals(Constants.ACTION_STOP)) {
                    playerFragment.stopPlayer();
                    finish();
                } else {
                    playerFragment.updateUI(s, intent);
                }
            }
        };
    }

    public void addBookmark() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final TextView title = promptsView.findViewById(R.id.bookmark_title);
        final EditText userInput = promptsView
                .findViewById(R.id.bookmark_message);
        final int time = (int) timeElapsed;

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Simpan",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String msg = userInput.getText().toString();
                                bookmark(time, msg);
                            }
                        })
                .setNegativeButton("Tidak",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void bookmark(int time, String message) {
        BookmarkModel bookmark = new BookmarkModel();
        bookmark.setBab(playerFragment.bab);
        bookmark.setTime(time);
        bookmark.setMessage(message);
        String info = String.format("%02d.%02d", TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed), TimeUnit.MILLISECONDS.toSeconds((long) timeElapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed)));
        db.addBookmark(bookmark);
    }


    private void setupViewPager() {

        profileFragment = new ProfileFragment();
        playerFragment = new PlayerFragment();
        infoFragment = new InfoFragment();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(profileFragment);
        fragments.add(playerFragment);
        fragments.add(infoFragment);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, fragments, icons);

        // Assigning ViewPager View and setting the adapter
        pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(adapter);
        pager.setCurrentItem(1);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    infoFragment.getBanner();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Assiging the Sliding Tab Layout View
        tabs = findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        initViews();
    }

    private void initViews() {
        tabs.setCustomTabView(R.layout.tab_img_layout, R.id.tab_name_img);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.white);
            }
        });
        tabs.setViewPager(pager);
    }

    private void confirmLogin() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Hai Kakak..");
        alertDialog.setMessage("Untuk menggunakan fitur ini, Kakak harus masuk sebagai pengguna terlebih dahulu. Ingin masuk sekarang?");

        alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                login();
            }
        });

        alertDialog.setNegativeButton("Tidak", null);
        alertDialog.show();
    }

    private void confirmLogout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Hai Kak " + session.getName());
        alertDialog.setMessage("Ingin keluar sekarang?");

        alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });

        alertDialog.setNegativeButton("Tidak", null);
        alertDialog.show();
    }

    private void openBookmark() {
        if (session.isLoggedIn()) {
            Intent i = new Intent(this, BookmarkActivity.class);
            startActivityForResult(i, BOOKMARK_REQUEST);
        } else
            login();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case BOOKMARK_REQUEST:
                    BookmarkModel bookmark = (BookmarkModel) data.getSerializableExtra("bookmark");
                    playerFragment.openBab(bookmark.getBab(), bookmark.getTime());
                    break;
                case RC_REQUEST:
                    playerFragment.onActivityResult(requestCode, resultCode, data);
                    break;
                case BAB_REQUEST:
                    playerFragment.openBab(data.getIntExtra("bab", 0), 0);
                    break;
            }
        }
    }

    private void login() {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void logout() {
        session.logout();
        playerFragment.stopPlayer();
        finish();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(Constants.RESULT)
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        playerFragment.stopThread();
        super.onStop();
    }

    public void onResume() {
        super.onResume();
        playerFragment.startThread();
        bindService();
    }

    public void onPause() {
        super.onPause();
        unbindService();
    }

    MusicService myService;
    //private boolean isBound;
    public ServiceConnection myConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.d("ServiceConnection", "connected");
            myService = ((MusicService.MusicBinder) binder).getService();
            //isBound = true;
            playerFragment.preparePlayer(myService);
            if (myService.isPlaying())
                myService.sendNotification(Constants.ACTION_PLAY);
            else
                myService.sendNotification(Constants.ACTION_PAUSE);
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d("ServiceConnection", "disconnected");
            myService = null;
        }
    };

    public void bindService() {
        Intent service = new Intent(this, MusicService.class);
        bindService(service, myConnection, BIND_AUTO_CREATE);
    }

    public void unbindService() {
        unbindService(myConnection);
    }
}
