package com.projnibbas.ratingapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class SessionManager {

    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String SESSION_COOKIE = "Cookie";


    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context){
        this.context = context;
        preferences = context.getSharedPreferences("RatingsApp_SessionManager", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setCookie(String cookie){
        editor.putString(SESSION_COOKIE, cookie);
        editor.commit();
    }

    public String getCookie(){
        return preferences.getString(SESSION_COOKIE, null);
    }

    public void checkAndSetCookie(Map<String,String> headers) {
        if (headers.containsKey(SET_COOKIE_KEY)) {
            String cookie = headers.get(SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                setCookie(cookie);
            }
        }
    }

    public void logout() {
        editor.putString(SESSION_COOKIE, null);
        editor.commit();
    }
}
