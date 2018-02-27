package id.playable.frompassiontoaction.components;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;

import id.playable.frompassiontoaction.MainActivity;
import id.playable.frompassiontoaction.Model.ContentModel;
import id.playable.frompassiontoaction.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {

    private static String mUrl;
    private static int mResID;
    private static MusicService mInstance = null;

    private MediaPlayer mMediaPlayer = null;    // The Media Player
    private int mBufferPosition;
    private static String mSongTitle;
    private static String mSongPicUrl;

    NotificationManager mNotificationManager;
    NotificationCompat mNotification = null;
    final int NOTIFICATION_ID = 1;
    private boolean isAutoStart = false;
    private LocalBroadcastManager broadcaster;
    private int bab = 1;
    private ArrayList<ContentModel> playlist = new ArrayList<>();
    private DatabaseHandler db;


    // indicates the state our service:
    public enum State {
        Retrieving, // the MediaRetriever is retrieving music
        Stopped, // media player is stopped and not prepared to play
        Preparing, // media player is preparing...
        StandBy, // media player is ready to play
        Playing, // playback active (media player ready!). (but the media player may actually be
        // paused in this state if we don't have audio focus. But we stay in this state
        // so that we know we have to resume playback once we get focus back)
        Paused
        // playback paused (media player ready!)
    }

    State mState = State.Retrieving;

    @Override
    public void onCreate() {
        mInstance = this;
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        broadcaster = LocalBroadcastManager.getInstance(this);

        db = new DatabaseHandler(getApplication());

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnCompletionListener(this);

        playlist = db.getContents(0);
    }

    public State getState() {
        return mState;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null)
            return START_STICKY;
        String action = intent.getAction();
        if (action.equals(Constants.LOG_TAG)) {
            if (mState == State.Retrieving) {
                int bab = intent.getIntExtra("resid", 0);
                File file = new File(getExternalFilesDir(null) + "/playstory/" + bab + ".mp3");
                setDataSource(file, false);
                //setDataSource(resid, false);
            }
        } else if (action.equals(Constants.ACTION_PLAY)) {
            startMusic();
            sendNotification(Constants.ACTION_PLAY);
            sendResult(Constants.ACTION_PLAY);
        } else if (action.equals(Constants.ACTION_PAUSE)) {
            pauseMusic();
            sendNotification(Constants.ACTION_PAUSE);
            sendResult(Constants.ACTION_PAUSE);
        } else if (action.equals(Constants.ACTION_PREVIOUS)) {
            previous();
        } else if (action.equals(Constants.ACTION_NEXT)) {
            next();
        } else if (action.equals(Constants.ACTION_STOP)) {
            mNotificationManager.cancel(NOTIFICATION_ID);
            sendResult(Constants.ACTION_STOP);
            stopSelf();
        }
        return START_STICKY;
    }


    public void previous() {
        if (bab > 1) {
            int i = bab - 1;
            File file = new File(getExternalFilesDir(null) + "/playstory/" + i + ".mp3");
            if (file.exists())
                changeBab(i, 0);
        }
    }

    public void next() {
        if (bab < playlist.size()) {
            int i = bab + 1;
            File file = new File(getExternalFilesDir(null) + "/playstory/" + i + ".mp3");
            if (file.exists())
                changeBab(i, 0);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(isAutoStart)
            sendResult(Constants.ACTION_NEXT);
    }

    public void stopMusic() {
        mMediaPlayer.stop();
        mState = State.Stopped;
        mNotificationManager.cancel(NOTIFICATION_ID);
        stopSelf();
    }

    public void setDataSource(File file, boolean autoStart) {
        if (mMediaPlayer != null) {
            try {
                if (mMediaPlayer.isPlaying())
                    mMediaPlayer.stop();
                mMediaPlayer.reset();
            } catch (IllegalStateException E) {

            }
        }
        mState = State.Preparing;
        isAutoStart = autoStart;
        try {
            //AssetFileDescriptor afd = getApplicationContext().getResources().openRawResourceFd(resID);
            if (!file.exists()) return;
            mMediaPlayer.setDataSource(file.getAbsolutePath());
            //afd.close();
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void restartMusic() {
        mMediaPlayer.pause();
        mMediaPlayer.seekTo(0);
        mMediaPlayer.start();
    }

    public int getBab(){
        return bab;
    }

    public void changeBab(int bab, int time) {
        this.bab = bab;
        int resId = 0;

        File file = new File(getExternalFilesDir(null) + "/playstory/"+bab+".mp3");

        setDataSource(file, true);
        if (time > 0)
            seekMusicTo(time);

        Intent intent = new Intent(Constants.RESULT);
        intent.putExtra(Constants.MESSAGE, Constants.ACTION_CHANGE);
        intent.putExtra("bab", bab);
        broadcaster.sendBroadcast(intent);
        sendNotification(Constants.ACTION_PLAY);
    }

    protected void setBufferPosition(int progress) {
        mBufferPosition = progress;
    }

    /** Called when MediaPlayer is ready */
    @Override
    public void onPrepared(MediaPlayer player) {
        mState = State.StandBy;
        if(isAutoStart)
            mMediaPlayer.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e("playstory", "Error (" + what + "," + extra + ")");
        return false;
    }

    @Override
    public void onDestroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
        }

        super.onDestroy();
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public void pauseMusic() {
        mMediaPlayer.pause();
        sendNotification(Constants.ACTION_PAUSE);
        mState = State.Paused;
    }

    public void startMusic() {
        if (!mState.equals(State.Retrieving)) {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            //audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 80, 0);
            mMediaPlayer.start();
            sendNotification(Constants.ACTION_PLAY);
            mState = State.Playing;
        }else
            Log.e("player","Player not Ready. "+mState);
    }

    @Override
    public void sendBroadcast(Intent intent) {
        super.sendBroadcast(intent);
    }

    public boolean isPlaying() {

        boolean bn = false;
        try {
           bn = mMediaPlayer.isPlaying();




        }catch (IllegalStateException e){

        }

        return bn;



    }

    public int getMusicDuration() {
        if(getState()!=State.Retrieving)
            return mMediaPlayer.getDuration();

        return 0;
    }

    public int getCurrentPosition() {
        if(getState()!=State.Retrieving)
            return mMediaPlayer.getCurrentPosition();

        return 0;
    }

    public int getBufferPercentage() {
        return mBufferPosition;
    }

    public void seekMusicTo(int pos) {
        mMediaPlayer.seekTo(pos);
    }

    public static MusicService getInstance() {
        return mInstance;
    }

    public boolean isPaused(){
        return mState.equals(State.Paused);
    }

    public static void setSong(String url, String title, String songPicUrl) {
        mUrl = url;
        mSongTitle = title;
        mSongPicUrl = songPicUrl;
    }

    public String getSongTitle() {
        return mSongTitle;
    }

    public String getSongPicUrl() {
        return mSongPicUrl;
    }

    public void sendResult(String message) {
        Intent intent = new Intent(Constants.RESULT);
        if(message != null)
            intent.putExtra(Constants.MESSAGE, message);
        broadcaster.sendBroadcast(intent);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        setBufferPosition(percent * getMusicDuration() / 100);
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    private IBinder mBinder = new MusicBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void sendNotification(String action) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.notification);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.menutab_logo)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(pIntent)
                .setContent(remoteViews);

        ContentModel content = playlist.get(getBab()-1);

        String title = "Bab "+content.getBab();
        if(content.getChapter()>0)
            title += " Ch "+content.getChapter();

        remoteViews.setTextViewText(R.id.notif_title, title);
        remoteViews.setTextViewText(R.id.notif_desc, content.getTitle());

        remoteViews.setOnClickPendingIntent(R.id.notif_prev, action(Constants.ACTION_PREVIOUS));
        if (action.equals(Constants.ACTION_PAUSE)) {
            remoteViews.setImageViewResource(R.id.notif_play, R.drawable.player_play);
            remoteViews.setOnClickPendingIntent(R.id.notif_play, action(Constants.ACTION_PLAY));
        }else {
            remoteViews.setImageViewResource(R.id.notif_play, R.drawable.player_pause);
            remoteViews.setOnClickPendingIntent(R.id.notif_play, action(Constants.ACTION_PAUSE));
        }
        remoteViews.setOnClickPendingIntent(R.id.notif_next, action(Constants.ACTION_NEXT));
        remoteViews.setOnClickPendingIntent(R.id.notif_close, action(Constants.ACTION_STOP));

        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(NOTIFICATION_ID, builder.build());

    }

    private PendingIntent action(String action) {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(action);
        PendingIntent pi = PendingIntent.getService(this, 1, intent, 0);
        return pi;
    }
}
