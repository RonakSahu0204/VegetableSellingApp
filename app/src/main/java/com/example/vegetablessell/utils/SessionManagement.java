package com.example.vegetablessell.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.vegetablessell.model.UserModel;

public class SessionManagement {

    private SharedPreferences prefs;

    private static String jwtToken;

    public SessionManagement(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setSession(String userDetail) {
        prefs.edit().putString("userDetail", userDetail).commit();
    }

    public String getSession() {
        return prefs.getString("userDetail", "");
    }

    public void closeSession() {
        prefs.edit().putString("userDetail", "").commit();
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }

    public void setPrefs(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    public static String getJwtToken() {
        return SessionManagement.jwtToken;
    }

    public static void setJwtToken(String jwtToken) {
        SessionManagement.jwtToken = jwtToken;
    }
}
