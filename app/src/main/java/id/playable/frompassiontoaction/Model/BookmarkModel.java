package id.playable.frompassiontoaction.Model;

import android.database.Cursor;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zuhri Utama on 7/6/2015.
 */
public class BookmarkModel implements Serializable{

    public static final String tablename = "bookmark";

    private int id;
    private int bab;
    private int time;
    private String message;
    private boolean checked;

    public BookmarkModel(){
        bab = 0;
        time = 0;
        message = "";
    }

    public BookmarkModel(Cursor cursor) {
        id = cursor.getInt(0);
        bab = cursor.getInt(1);
        time = cursor.getInt(2);
        message = cursor.getString(3);
    }

    public int getId(){
        return id;
    }

    public void setBab(int bab){
        this.bab = bab;
    }

    public int getBab(){
        return bab;
    }

    public void setTime(int time){
        this.time = time;
    }

    public int getTime(){
        return time;
    }

    public String getFormatedTime(){
        return String.format("%02d.%02d", TimeUnit.MILLISECONDS.toMinutes((long) time), TimeUnit.MILLISECONDS.toSeconds((long) time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) time)));
    }

    public void setMessage(String message) { this.message = message; }

    public String getMessage() { return message; }

    public void setChecked(boolean checked){ this.checked = checked; }

    public boolean isChecked(){ return checked; }
}
