package com.yummybaryani.admin.Database;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lincoln on 05/05/16.
 */
public class PrefManager {

    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;
    public static Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences constants
    private static final String PREF_NAME = "MyPreference";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public static boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
    public  static  void setMessageCount(int count) {
        editor.putInt("messageCount", count);
        editor.commit();
    }
    public  static  int getMessageCount() {
        return  pref.getInt("messageCount", 0);
    }
    public  static  void setToken(String token) {
        editor.putString("fcm_token", token);
        editor.commit();
    }
    public  static  String getToken() {
        return  pref.getString("fcm_token","");
    }



}