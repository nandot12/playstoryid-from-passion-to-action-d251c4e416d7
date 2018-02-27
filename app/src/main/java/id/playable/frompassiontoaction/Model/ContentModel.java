package id.playable.frompassiontoaction.Model;

import android.database.Cursor;

/**
 * Created by zuhriutama on 12/4/17.
 */

public class ContentModel {

    public static final String tablename = "content";

    private int id;
    private int bab;
    private int chapter;
    private String title;
    private boolean downloaded;

    public ContentModel(int bab, int chapter, String title){
        this.bab = bab;
        this.chapter = chapter;
        this.title = title;
        downloaded=false;
    }

    public ContentModel(Cursor cursor) {
        id = cursor.getInt(0);
        bab = cursor.getInt(1);
        chapter = cursor.getInt(2);
        title = cursor.getString(3);
        downloaded = cursor.getInt(4)==1;
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

    public void setChapter(int chapter){
        this.chapter = chapter;
    }

    public int getChapter(){
        return chapter;
    }

    public void setTitle(String title) { this.title = title; }

    public String getTitle() { return title; }

    public void setDownloaded(boolean downloaded){ this.downloaded = downloaded; }

    public boolean isDownloaded(){ return downloaded; }

    public String toString(){
        String val = "Bab "+getBab();
        if(getChapter()>0)
            val += " Chapter "+getChapter();

        val += " : "+getTitle();

        return val;
    }
}
