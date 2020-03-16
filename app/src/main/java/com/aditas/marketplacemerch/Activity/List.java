package com.aditas.marketplacemerch.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aditas.marketplacemerch.Adapter.Adapt;
import com.aditas.marketplacemerch.AddPrdt;
import com.aditas.marketplacemerch.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

public class List extends AppCompatActivity {
    private RecyclerView rvMerch;
    private Adapt adpt;
    private RequestQueue rq;
    private SwipeRefreshLayout srl;
    private FloatingActionButton fab;
    String url = "http://210.210.154.65:4444/api/merchant/products";
    String urlCat = "http://210.210.154.65:4444/api/categories";
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        init();
        Volley();
    }

    private void init() {
        rvMerch = findViewById(R.id.rv_merch);
        srl = findViewById(R.id.swipe_fresh);
        fab = findViewById(R.id.fab);
        adpt = new Adapt();
        rvMerch.setAdapter(adpt);
        rvMerch.setLayoutManager(new GridLayoutManager(this, 2));
        rq = Volley.newRequestQueue(getApplicationContext());
    }

    private void Volley(){
        StringRequest listProdReq = new StringRequest(Request.Method.GET, url,
                response -> {
                        Gson data = new Gson();
                        ListProd lp = data.fromJson(response, ListProd.class);
                        adpt.setProd(lp.getProd());
                }, error -> {
                    Log.e("Volley error", error.getMessage());
        });
        rq.add(listProdReq);
        srl.setRefreshing(false);
    }
    private void setSrl(){
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Volley();
            }
        });
    }
    private void setFab(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDataVolley();
            }
        });
    }
    private void addDataVolley(){
        Intent i = new Intent(getApplicationContext(), AddPrdt.class);
        startActivity(i);
    }
}
