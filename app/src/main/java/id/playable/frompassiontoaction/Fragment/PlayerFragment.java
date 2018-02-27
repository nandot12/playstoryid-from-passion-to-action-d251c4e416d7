package id.playable.frompassiontoaction.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.playable.frompassiontoaction.KodePremiumActivity;
import id.playable.frompassiontoaction.LoginActivity;
import id.playable.frompassiontoaction.MainActivity;
import id.playable.frompassiontoaction.Model.ContentModel;
import id.playable.frompassiontoaction.R;
import id.playable.frompassiontoaction.components.Constants;
import id.playable.frompassiontoaction.components.DatabaseHandler;
import id.playable.frompassiontoaction.components.Helper;
import id.playable.frompassiontoaction.components.MusicService;
import id.playable.frompassiontoaction.components.SessionManager;

/**
 * Created by Zuhri Utama on 7/23/2015.
 */
public class PlayerFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.backgroundsound)
    ImageView backgroundsound;
    Unbinder unbinder;
    @BindView(R.id.media_subname)
    TextView mediaSubname;
    @BindView(R.id.layout1)
    RelativeLayout layout1;
    @BindView(R.id.songCurrentDurationLabel)
    TextView songCurrentDurationLabel;

    private View v;

    Context context;

    private SessionManager session;

    //private MediaPlayer mediaPlayer;
    private TextView songName, subName, duration2;
    private ImageButton media_play, media_previous, media_next, media_backward, media_forward;
    public static double timeElapsed = 0;
    private double finalTime = 0;
    private Handler durationHandler = new Handler();
   private SeekBar seekbar;
    private int forwardTime = 5000, backwardTime = 5000; // in milisecond
    private ArrayList<ContentModel> playlist = new ArrayList<>();

    public static SlidingUpPanelLayout mLayout;

    public int bab = 1;

    // Progress Dialog
    private ProgressDialog pDialog;

    private File dirPath;

    public static DatabaseHandler db;
    private MusicService musicService;
    private boolean isBound;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        context = getActivity();
        dirPath = getActivity().getExternalFilesDir(null);


        session = new SessionManager(context);

        int status = getActivity().getIntent().getIntExtra("status",0);

        if (status == 1){
          int posisi =   getActivity().getIntent().getIntExtra("position",0);

          //  Toast.makeText(getActivity(),String.valueOf(posisi),Toast.LENGTH_LONG).show();
            bab  = posisi + 1 ;
            checkFile(bab);
        }
        db = new DatabaseHandler(context);

        playlist = db.getContents(0);

        v = inflater.inflate(R.layout.fragment_player, container, false);

        initializeViews();


        File file = new File(dirPath + "/playstory/1.mp3");
        if (!file.exists()) {
            new File(dirPath + "/playstory/.nomedia");
            downloadFile(1);
        }


        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    /**
     * Mapping layout view
     */
    public void initializeViews() {

        // Media Info
        duration2 = v.findViewById(R.id.songDuration2);
        songName = v.findViewById(R.id.media_name);
        subName = v.findViewById(R.id.media_subname);

        // Media Button
        media_play = v.findViewById(R.id.media_play);
        media_backward = v.findViewById(R.id.media_backward);
        media_forward = v.findViewById(R.id.media_forward);
        media_previous = v.findViewById(R.id.media_previous);
        media_next = v.findViewById(R.id.media_next);
        media_play.setOnClickListener(this);
        media_backward.setOnClickListener(this);
        media_forward.setOnClickListener(this);
        media_previous.setOnClickListener(this);
        media_next.setOnClickListener(this);

        seekbar = v.findViewById(R.id.seekbar);

       changeBab(bab);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Mengunduh Audio. Mohon Tunggu...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(false);

        setupSeekBar();
    }

    public void checkFile(int t_bab) {
        if (session.checkCode() == false && t_bab != 1) {
            buyPremium();
        }else{
            File file = new File(dirPath + "/playstory/" + t_bab + ".mp3");
            if (!file.exists()) {
                downloadFile(t_bab);
            } else {
                musicService.changeBab(t_bab, 0);
            }
        }
    }

    private void buyPremium() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setNegativeButton("Tidak", null);

        String message = "Untuk menikmati keseluruhan Audiobook, Anda harus membeli kode premium, Beli Sekarang?";
        alertDialog.setMessage(message);

        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(session.isLoggedIn())
                    startActivityForResult(new Intent(getActivity(), KodePremiumActivity.class), MainActivity.LOGIN_REQUEST);
                else
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), MainActivity.LOGIN_REQUEST);
            }
        });

        alertDialog.show();

    }

    private void downloadFile(int t_bab) {

        ContentModel content = playlist.get(t_bab-1);

        songName.setText("Mohon Tunggu");
        subName.setText(content.toString());

        new DownloadFileFromURL().execute(String.valueOf(t_bab));
    }

    public void onStopThread() {
        isBound = false;
        durationHandler.removeCallbacks(updateSeekBarTime);
    }

    public void onStartThread(long delayMillis) {
        durationHandler.postDelayed(updateSeekBarTime, delayMillis);
    }

    private boolean runnable = true;
    //handler to change seekBarTime
    private Runnable updateSeekBarTime = new Runnable() {

        public void run() {
            if (isBound) {
                if (musicService.isPlaying()) {

                    // setDots();
                    finalTime = musicService.getMusicDuration();
                    seekbar.setMax((int) finalTime);

                    // seekbar.setTickMarkTintList();
                    //get current position
                    timeElapsed = musicService.getCurrentPosition();
                    //set seekbar progress
                    seekbar.setProgress((int) timeElapsed);
                    double startTime = 100;
                    double timeRemaining = startTime + timeElapsed;
                    double timefinal = finalTime - timeRemaining;
                    duration2.setText(String.format("%02d.%02d", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
                    songCurrentDurationLabel.setText(String.format("%02d.%02d",
                            TimeUnit.MILLISECONDS.toMinutes((long) timefinal),
                            TimeUnit.MILLISECONDS.toSeconds((long) timefinal) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            timefinal))));
                }
                //repeat yourself that again in 100 miliseconds
                if (runnable)
                    onStartThread(100);
            }
        }
    };

    public void startThread() {
        runnable = true;
        onStartThread(100);
    }

    public void stopThread() {
        runnable = false;
    }

    public void openBab(int t_bab, int time) {
        if (session.checkCode() == false && t_bab != 1) {
            buyPremium();
        }else{
            File file = new File(dirPath + "/playstory/" + t_bab + ".mp3");
            if (!file.exists()) {
                downloadFile(t_bab);
            } else {
                musicService.changeBab(t_bab, time);
                changeBab(t_bab);
                media_play.setBackgroundResource(R.drawable.player_pause);
                setupSeekBar();
            }
        }
    }

    // play mp3 song
    public void play() {
        if (isBound) {
            if (musicService.isPlaying()) {
                media_play.setBackgroundResource(R.drawable.iconplay);
                musicService.pauseMusic();
            } else {
                AudioManager am = (AudioManager) getActivity().getSystemService(getActivity().AUDIO_SERVICE);
                int volume_level = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (volume_level <= 1)
                    Helper.popup(getActivity(), "Naikkan Volume Handphone Anda....");

                media_play.setBackgroundResource(R.drawable.player_pause);
                musicService.startMusic();
                if (!runnable) {
                    runnable = true;
                    onStartThread(100);
                }
            }
            setupSeekBar();
        } else
            Log.e(Constants.LOG_TAG, "No Service Available");
    }

    private void setupSeekBar() {
        if (isBound) {
            finalTime = musicService.getMusicDuration();
            if (seekbar != null) {
                seekbar.setMax((int) finalTime);

                seekbar.setClickable(true);
                seekbar.setEnabled(true);
                timeElapsed = musicService.getCurrentPosition();
                seekbar.setProgress((int) timeElapsed);

                // seekbar.setProgressTintList(ColorStateList.valueOf(Color.BLUE));

                seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        timeElapsed = seekBar.getProgress();
                        updateDuration();
                        musicService.seekMusicTo((int) timeElapsed);
                    }
                });
            }
        }

        onStartThread(100);
    }

    // go forward at backwardTime seconds
    public void backward() {
        //check if we can go forward at forwardTime seconds before song endes
        if ((timeElapsed + forwardTime) <= finalTime) {
            timeElapsed = timeElapsed - backwardTime;
            updateDuration();
            //seek to the exact second of the track
            musicService.seekMusicTo((int) timeElapsed);
        }
    }

    // go backward at forwardTime seconds
    public void forward() {
        //check if we can go forward at bakwardTime seconds before song endes
        if ((timeElapsed - forwardTime) <= finalTime) {
            timeElapsed = timeElapsed + backwardTime;
            updateDuration();
            //seek to the exact second of the track
            musicService.seekMusicTo((int) timeElapsed);
        }
    }

    private void updateDuration() {
        double timeRemaining = 100 + timeElapsed;

        Log.d("durasi :", String.valueOf(timeRemaining));
        duration2.setText(String.format("%02d.%02d", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
        songCurrentDurationLabel.setText(String.format("%02d.%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime))));

    }


    private void confirmDownlad(final int t_bab) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());


        alertDialog.setNegativeButton("Tidak", null);

        ContentModel content = playlist.get(t_bab-1);

        String message = "Bab "+content.getBab();
        if(content.getChapter()>0)
            message += " Chapter "+content.getChapter();
        message += " : "+content.getTitle()+" Belum diunduh. Unduh sekarang?";
        alertDialog.setMessage(message);

        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                downloadFile(t_bab);
            }
        });

        alertDialog.show();
    }

    /**
     * added by @ZuhriUtama
     * save current bookmark
     */

    public void changeBab(int t_bab) {
        this.bab = t_bab;

        ContentModel content = playlist.get(t_bab-1);

        if (isBound && musicService.isPlaying())
            media_play.setBackgroundResource(R.drawable.player_pause);

        String title = "Bab "+content.getBab();
        if(content.getChapter()>0)
            title += " Chapter "+content.getChapter();
        songName.setText(title);

        subName.setText(content.getTitle());
    }


    public void onResume() {
        super.onResume();

        onStartThread(100);
    }

    public void onStop() {
        super.onStop();
        //slider.stopAutoCycle();
        onStopThread();
    }

    @Override
    public void onClick(View v) {
        if (v == media_play) {
            play();
        } else if (v == media_backward)
            backward();
        else if (v == media_forward)
            forward();
        else if (v == media_previous && bab > 1)
            checkFile(bab - 1);
        else if (v == media_next && bab < playlist.size())
            checkFile(bab + 1);
    }

    public void stopPlayer() {
        musicService.stopMusic();
        isBound = false;
        onStopThread();
    }

    public void preparePlayer(MusicService service) {
        musicService = service;
        bab = musicService.getBab();
        isBound = true;
        setupSeekBar();
    }

    public void updateUI(String s, Intent i) {
        if (s.equals(Constants.ACTION_PAUSE)) {
            musicService.pauseMusic();
            media_play.setBackgroundResource(R.drawable.iconplay);
        } else if (s.equals(Constants.ACTION_PLAY)) {
            musicService.startMusic();
            media_play.setBackgroundResource(R.drawable.player_pause);
            setupSeekBar();
        } else if (s.equals(Constants.ACTION_PREVIOUS)) {
            musicService.previous();
        } else if (s.equals(Constants.ACTION_NEXT)) {
            musicService.next();
        } else if (s.equals(Constants.ACTION_CHANGE)) {
            changeBab(i.getIntExtra("bab", 1));
            setupSeekBar();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * Background Async Task to download file
     */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */

        private String p_bab = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                p_bab = f_url[0];
                String file = p_bab + ".mp3";
                URL url = new URL(Constants.URL + file);

                // if(exists(Constants.URL+file)) {
                URLConnection conection = url.openConnection();
                conection.connect();

                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                File folder = new File(dirPath + "/playstory");
                boolean success = true;
                if (!folder.exists()) {
                    success = folder.mkdir();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder.getPath() + "/" + p_bab + ".part");
                Log.i("playstory", "download " + url.toString());

                byte data[] = new byte[1024];

                while ((count = input.read(data)) != -1) {
                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                //}else
                //   return "error, no file exists";

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                return "error : " + e.getMessage();
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {

        }

        @Override
        protected void onPostExecute(String msg) {
            if (msg == null) {
                File file = new File(dirPath + "/playstory/" + p_bab + ".part");
                boolean success = true;
                if (file.exists()) {
                    File newfile = new File(dirPath + "/playstory/" + p_bab + ".mp3");
                    file.renameTo(newfile);
                    musicService.changeBab(Integer.valueOf(p_bab),0);
                }
            }
        }

    }
}
