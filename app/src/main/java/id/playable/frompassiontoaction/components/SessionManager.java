package id.playable.frompassiontoaction.components;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Zuhri Utama on 3/12/2016.
 */
public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;
    private static String PREF_NAME = "FPTA";
    private static String IS_LOGIN = "isLoggedIn";
    private static String IS_FIRST = "isFirstStart";
    private static String SKIP_COUNT = "skipCount";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_STATUS = "status";
    private static String KEY_IMAGE = "image";

    private static String KEY_STATUS_CODE = "premium";



    public SessionManager(Context ctx){
        context = ctx;
        pref = ctx.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    public void saveSetting(String key, Object save) {
        editor = pref.edit();
        if (save instanceof Boolean) {
            editor.putBoolean(key, (Boolean) save);
        } else if (save instanceof String) {
            editor.putString(key, (String) save);
        }
        editor.commit();
    }

    public void saveStatus(int status){
        editor = pref.edit();
        editor.putInt(KEY_STATUS,status);
        editor.commit();
    }

    public void saveCode (){
        editor = pref.edit();
        editor.putBoolean(KEY_STATUS_CODE,true);
        editor.commit();
    }

    public Boolean checkCode(){
        return pref.getBoolean(KEY_STATUS_CODE,false);
    }
    public Boolean getSettingBoolean(String key, Boolean defValueStr) {
        Boolean cekSetting = pref.getBoolean(key, defValueStr);
        return cekSetting;
    }

    public void createLoginSession(String name, String email,String image){
        editor = pref.edit();
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_IMAGE,image);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public boolean isFirstStart() {
        return pref.getBoolean(IS_FIRST, true);
    }

    public int getSkipCount(){
        return pref.getInt(SKIP_COUNT, 0);
    }

    public String getName(){
        return pref.getString(KEY_NAME, null);
    }

    public Integer getStatus(){
        return pref.getInt(KEY_STATUS,0);
    }

    public String getEmail(){
        return pref.getString(KEY_EMAIL, null);
    }

    public String getImage(){
        return pref.getString(KEY_IMAGE, null);
    }

    public void logout(){
        editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
