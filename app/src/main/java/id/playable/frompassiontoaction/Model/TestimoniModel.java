package id.playable.frompassiontoaction.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Zuhri Utama on 7/6/2015.
 */
public class TestimoniModel implements Serializable{

    private int no;
    private String name, title, message, photo;
    private int rate;

    public TestimoniModel(int id, String name, String title, String message, String photo, int rate){
        this.no = id;
        this.name = name;
        this.title = title;
        this.message = message;
        this.photo = photo;
        this.rate = rate;
    }

    public TestimoniModel(JSONObject testimoni) {
        try {
            this.no = testimoni.getInt("id");
            this.name = testimoni.getString("name");
            this.title = testimoni.getString("title");
            this.message = testimoni.getString("message");
            this.photo = testimoni.getString("photo");
            this.rate = testimoni.getInt("rate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getNo(){
        return no;
    }

    public String getName(){
        return name;
    }

    public String getTitle(){
        return title;
    }

    public String getMessage(){
        return message;
    }

    public String getPhoto(){
        return photo;
    }

    public int getRate(){
        return rate;
    }
}
