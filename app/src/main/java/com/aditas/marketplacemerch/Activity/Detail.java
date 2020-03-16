package com.aditas.marketplacemerch.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aditas.marketplacemerch.AddPrdt;
import com.aditas.marketplacemerch.Entity.Product;
import com.aditas.marketplacemerch.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class Detail extends AppCompatActivity {
    public static String EXTRA_DATA = "EXTRA PRODUCT DATA";
    public static final String EXTRA_ID = "Update Data";
    private ImageView img;
    private TextView name, qty, catg, merch,price,desc;
    private ProgressBar pb;
    private Button btnEdit, btnDel;
    private RequestQueue rq;
    private String url = "http://210.210.154.65:4444/api/categories";
    Product prod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        prod = getIntent().getParcelableExtra(EXTRA_DATA);
        init();
        rq = Volley.newRequestQueue(this);
        if(prod != null){
            Toast.makeText(this, "ada data", Toast.LENGTH_LONG).show();
            String _desc = prod.getDesc();
            String names = prod.getName();
            int Qty = prod.getQty();
            String _qty = Integer.toString(Qty);
            String cat = prod.getCatg().getName();
            String _merch = prod.getMerch().getName();
            int _price = prod.getPrice();
            String url = "http://210.210.154.65:4444/api/categories";

            Toast.makeText(Detail.this,prod.getImage(),Toast.LENGTH_SHORT).show();
            Glide.with(this)
                    .load(url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            pb.setVisibility(View.GONE);
                            Toast.makeText(Detail.this,"gagal memuat gambar",Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            pb.setVisibility(View.GONE);
                            Toast.makeText(Detail.this,"berhasil memuat gambar",Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    })
                    .into(img);

            name.setText(names);
            qty.setText(_qty);
            catg.setText(cat);
            merch.setText(_merch);
            price.setText("Rp. "+ _price);
            desc.setText(_desc);
            OnClickDelete();
            OnEditData();
        }
    }

    private void init() {
        img = findViewById(R.id.img_detail);
        name = findViewById(R.id.tv_name);
        qty = findViewById(R.id.tv_qty);
        catg = findViewById(R.id.tv_prod);
        merch = findViewById(R.id.tv_merch);
        price = findViewById(R.id.price_detail);
        desc = findViewById(R.id.tv_desc_detail);
        pb = findViewById(R.id.progressbar);
        btnEdit = findViewById(R.id.button_edit);
        btnDel = findViewById(R.id.button_delete);
    }

    private void OnClickDelete() {
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringReq = new StringRequest(Request.Method.DELETE, "http://210.210.154.65:4444//api/product/" + prod.getId() + "/delete",
                    response -> {
                        Toast.makeText(Detail.this,"Data has been deleted",Toast.LENGTH_SHORT).show();
                }, error -> {
                        //Toast.makeText(Detail.this,"Data isn't deleted yet",Toast.LENGTH_SHORT).show();
                });
                rq.add(stringReq);
            }
        });
    }

    private void OnEditData() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddPrdt.class);
                i.putExtra(EXTRA_ID, prod);
                startActivity(i);
            }
        });
    }
}
