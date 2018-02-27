package id.playable.frompassiontoaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import id.playable.frompassiontoaction.components.SessionManager;

public class SplashScreenActivity extends Activity {

    //Set waktu lama splashscreen
    private static int splashInterval = 2000; // 2 seconds

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splashscreen);

        session = new SessionManager(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showMain();
            }
        }, splashInterval);
    }

    private void showMain() {
        Intent i = null;
        if(session.isFirstStart())
            i = new Intent(SplashScreenActivity.this, VideoAuthorActivity.class);
        else
            i = new Intent(SplashScreenActivity.this, MainActivity.class);

        startActivity(i);
        finish();
    }

}