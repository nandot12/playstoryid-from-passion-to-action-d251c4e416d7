package id.playable.frompassiontoaction.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Zuhri Utama on 7/6/2015.
 */
public class BannerModel implements Serializable{

    private int no;
    private String image, link;

    public BannerModel(int id, String image, String link){
        this.no = id;
        this.image = image;
        this.link = link;
    }

    public BannerModel(JSONObject testimoni) {
        try {
            this.no = testimoni.getInt("id");
            this.image = testimoni.getString("image");
            this.link = testimoni.getString("link");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getNo(){
        return no;
    }

    public String getImage(){
        return image;
    }

    public String getLink(){
        return link;
    }
}
