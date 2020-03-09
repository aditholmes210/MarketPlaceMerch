package com.aditas.marketplacemerch.Util;

import android.content.SharedPreferences;

import com.aditas.marketplacemerch.Entity.AccessToken;

public class TokenManager {
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    private static TokenManager INSTANCE;

    public TokenManager(SharedPreferences pref){
        this.sp = pref;
        this.editor = sp.edit();
    }

    public static synchronized TokenManager getInstance(SharedPreferences pref){
        if(INSTANCE == null){
            INSTANCE = new TokenManager(pref);
        }

        return INSTANCE;
    }

    public void saveToken(AccessToken accessToken){
        editor.putString("TOKEN_TYPE",accessToken.getTokenType()).commit();
        editor.putString("ACCESS_TOKEN",accessToken.getAccessToken()).commit();
        editor.putString("REFRESH_TOKEN",accessToken.getRefreshToken()).commit();

    }

    public void deleteToken(){
        editor.remove("TOKEN_TYPE").commit();
        editor.remove("ACCESS_TOKEN").commit();
        editor.remove("REFRESH_TOKEN").commit();

    }

    public AccessToken getToken(){
        AccessToken accessToken = new AccessToken();
        accessToken.setTokenType(sp.getString("TOKEN_TYPE",null));
        accessToken.setAccessToken(sp.getString("ACCESS_TOKEN",null));
        accessToken.setRefreshToken(sp.getString("REFRESH_TOKEN",null));
        return accessToken;
    }
}
