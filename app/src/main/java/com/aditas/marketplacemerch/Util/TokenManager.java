package com.aditas.marketplacemerch.Util;

import android.content.SharedPreferences;

import com.aditas.marketplacemerch.Entity.AccessToken;

public class TokenManager {
    private static SharedPreferences sp;
    private static SharedPreferences.Editor edtr;

    private static TokenManager INSTANCE;

    public TokenManager(SharedPreferences pref){
        this.sp = pref;
        this.edtr = sp.edit();
    }

    public static synchronized TokenManager getInstance(SharedPreferences pref){
        if(INSTANCE == null){
            INSTANCE = new TokenManager(pref);
        }

        return INSTANCE;
    }

    public void saveToken(AccessToken at){
        edtr.putString("TOKEN_TYPE",at.getTokenType()).commit();
        edtr.putString("ACCESS_TOKEN",at.getAccessToken()).commit();
        edtr.putString("REFRESH_TOKEN",at.getRefreshToken()).commit();

    }

    public void deleteToken(){
        edtr.remove("TOKEN_TYPE").commit();
        edtr.remove("ACCESS_TOKEN").commit();
        edtr.remove("REFRESH_TOKEN").commit();

    }

    public AccessToken getToken(AccessToken at){
//        AccessToken at = new AccessToken();
        at.setTokenType(sp.getString("TOKEN_TYPE",null));
        at.setAccessToken(sp.getString("ACCESS_TOKEN",null));
        at.setRefreshToken(sp.getString("REFRESH_TOKEN",null));
        return at;
    }
}
