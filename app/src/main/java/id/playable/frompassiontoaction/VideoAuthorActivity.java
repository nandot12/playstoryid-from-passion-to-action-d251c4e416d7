package id.playable.frompassiontoaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer.Provider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.playable.frompassiontoaction.components.Constants;
import id.playable.frompassiontoaction.components.SessionManager;

public class VideoAuthorActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    @BindView(R.id.youtube_view)
    YouTubePlayerView youTubeView;
    @BindView(R.id.textskip)
    TextView textskip;
    @BindView(R.id.imgskip)
    ImageView imgskip;

    private static final int RECOVERY_REQUEST = 1;
    private MyPlayerStateChangeListener playerStateChangeListener;
    private MyPlaybackEventListener playbackEventListener;
    private YouTubePlayer player;
    private SessionManager session;
    private int SKIP_LIMIT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_autor);
        ButterKnife.bind(this);

        session = new SessionManager(this);

        textskip.setVisibility(View.INVISIBLE);
        imgskip.setVisibility(View.INVISIBLE);
        
        playerStateChangeListener = new MyPlayerStateChangeListener();
        playbackEventListener = new MyPlaybackEventListener();

        youTubeView.initialize(Constants.YOUTUBE_API_KEY, this);


    }

    @OnClick({R.id.textskip, R.id.imgskip})
    public void onViewClicked(View view) {
        int skip_count = session.getSkipCount();

        if(skip_count>SKIP_LIMIT)
            session.saveSetting("isFirstStart", false);
        else
            session.saveSetting("skipCount", skip_count++);

        switch (view.getId()) {
            case R.id.textskip:
                showMain();
                break;
            case R.id.imgskip:
                showMain();
                break;
        }
    }

    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {
            player.play();
        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {

        }

        @Override
        public void onVideoEnded() {
            session.saveSetting("isFirstStart", false);
            showMain();
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            showSkipButton();
        }
    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {

        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to user action or call to pause().
        }

        @Override
        public void onStopped() {
            // Called when playback stops for a reason other than being paused.
        }

        @Override
        public void onBuffering(boolean b) {
            // Called when buffering starts or ends.
        }

        @Override
        public void onSeekTo(int i) {
            // Called when a jump in playback position occurs, either
            // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()
        }
    }

    private void showSkipButton(){
        textskip.setVisibility(View.VISIBLE);
        imgskip.setVisibility(View.VISIBLE);
    }

    private void showMain() {
        startActivity(new Intent(VideoAuthorActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {

        this.player = player;

        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);

        if (!wasRestored) {
            player.cueVideo("ji3GxpJ8FL0");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showSkipButton();
            }
        }, 10000);
    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            showSkipButton();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Constants.YOUTUBE_API_KEY, this);
        }
    }

    protected Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

}
