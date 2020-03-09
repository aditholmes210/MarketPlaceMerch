package com.aditas.marketplacemerch.Network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyService {
    private static VolleyService INSTANCE;
    private static RequestQueue rq;
    private static Context ctx;

    public VolleyService(Context context){
        this.ctx = context;
        this.rq = getRequestQueue();
    }

    public static synchronized VolleyService getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = new VolleyService(context);
        }
        return INSTANCE;
    }


    public RequestQueue getRequestQueue(){
        if(rq == null){
            rq = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return rq;
    }

    public <T> void addToReqQueue(Request<T> req){
        getRequestQueue().add(req);
    }
}
