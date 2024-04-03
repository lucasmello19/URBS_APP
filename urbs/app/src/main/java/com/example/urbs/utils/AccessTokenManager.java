package com.example.urbs.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AccessTokenManager {
    private static AccessTokenManager instance;
    private SharedPreferences sharedPreferences;

    private static final String ACCESS_TOKEN_KEY = "access_token";

    private AccessTokenManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static synchronized AccessTokenManager getInstance(Context context) {
        if (instance == null) {
            instance = new AccessTokenManager(context.getApplicationContext());
        }
        return instance;
    }

    public void setAccessToken(String accessToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ACCESS_TOKEN_KEY, accessToken);
        editor.apply();
    }

    public String getAccessToken() {
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null);
    }
}
