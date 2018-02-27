package id.playable.frompassiontoaction.Model;

import java.io.Serializable;

/**
 * Created by Zuhri Utama on 7/6/2015.
 */
public class MenuModel implements Serializable{

    private int no;
    private String bab_name;
    private String bab_time;

    public MenuModel(int id, String name, String time){
        this.no = id;
        this.bab_name = name;
        this.bab_time = time;
    }

    public int getNo(){
        return no;
    }

    public String getName(){
        return bab_name;
    }

    public String getTime(){
        return bab_time;
    }
}
