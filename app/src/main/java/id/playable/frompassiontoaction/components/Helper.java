package id.playable.frompassiontoaction.components;

import android.content.Context;
import android.widget.Toast;

import java.net.InetAddress;

/**
 * Created by Zuhri Utama on 3/15/2016.
 */
public class Helper {

    public static void popup(Context ctx, String msg){
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName(Constants.URL); //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }
}
