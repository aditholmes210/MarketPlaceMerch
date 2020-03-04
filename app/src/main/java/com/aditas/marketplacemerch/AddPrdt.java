package com.aditas.marketplacemerch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.aditas.marketplacemerch.Activity.Adapt;
import com.aditas.marketplacemerch.Entity.Category;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class AddPrdt extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    RequestQueue queue;
    Spinner dropDown;
    ArrayList<Category> cat;
    Adapt adpt;
    EditText etNm, etQty, etDesc, etPrice;
    Button btnAdd, btnChoose;
    ImageView imgV;

    //set default request code for intent result
    private int PICK_IMAGE_REQUEST = 1;
    private String prodImg = null; //image string yang akan dikirim  ke server (bukan dalam bentuk gambar tapi dalam bentuk string base64.
    private String prodName, prodDesc, prodQty, prodPrice, catId, merchId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_prdt);

        //Volley queue instance
        queue = Volley.newRequestQueue(getApplicationContext());

        init();
        //Array List for hold categories from server
        cat = new ArrayList<>();
        //set categories adapter to spinner
        adpt = new Adapt();
        dropDown.setAdapter(adpt);
        dropDown.setOnItemSelectedListener(this);
        //get all categories from server
        getAllCat();
        //button choose image
        btnChoose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showFileChooser();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //product to be send
                prodName = etNm.getText().toString();
                prodQty = etQty.getText().toString();
                prodPrice = etPrice.getText().toString();
                prodDesc = etDesc.getText().toString();
                merchId = "1"; //sementara set ke 1

                //check string prodImg (sdh pilih gambar dari gallery atau belum)
                if(prodImg == null){ //jika kosong
                    prodImg = null; // isi dengan null
                }

                postProd();
            }
        });
    }

    private void getAllCat(){
        String url = "http://210.210.154.65:4444/api/categories"; //method GET

        JsonObjectRequest catReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //handle response
                        try {
                            JSONArray arr = response.getJSONArray("data");
                            for (int i = 0; i < arr.length(); i++) {
                                Gson gson = new Gson();
                                Category catg = gson.fromJson(arr.getJSONObject(i).toString(), Category.class);
                                cat.add(catg);
                            }
                            adpt.addData(cat);
                            adpt.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), String.valueOf(adpt.getCount()), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(catReq);

    }

    //Override Spinner Method
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        //Toast.makeText(getApplicationContext(),String.valueOf(catadpt.getItemId(pos)), Toast.LENGTH_LONG).show();

        //set merchId yg akan dikirim dari item yg dipilih dari spinner category, id didapat dari obj category di
        this.catId = String.valueOf(adpt.getItemId(pos));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){
        //biarin blank, yg penting di override
    }
    //get image from implicit intent
    private void showFileChooser() {
        Intent pickImg = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImg.setType("image/*");
        pickImg.putExtra("aspectX", 1);
        pickImg.putExtra("aspectY", 1);
        pickImg.putExtra("scale", true);
        pickImg.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(pickImg, PICK_IMAGE_REQUEST);
    }

    //get result image from intent above
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData()!= null){
            Uri filePath = data.getData();
            try{
                Bitmap bit = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //encoding img to string
                prodImg = getStringImage(bit);  //call getStringImage()
                Log.d("image", prodImg);

                Glide.with(getApplicationContext())
                        .load(bit)
                        .override(imgV.getWidth())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imgV);
                System.out.println("image : "+prodImg);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    //convert image bitmap to string base64
    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imgByt = baos.toByteArray();
        String encodeImg = Base64.encodeToString(imgByt, Base64.DEFAULT);
        return encodeImg;
    }

    public void init(){
        //EditText
        etNm = findViewById(R.id.et_name);
        etQty = findViewById(R.id.et_qty);
        etDesc = findViewById(R.id.et_desc);
        etPrice = findViewById(R.id.et_price);

        btnAdd = findViewById(R.id.btn_add);
        btnChoose = findViewById(R.id.btn_choose);

        //ImageView for hold choosed image from intent
        imgV = findViewById(R.id.img_form);

        //Spinner Categories
        dropDown = findViewById(R.id.cat_down);
    }

    private void postProd(){
        String url = "http://210.210.154.65:4444/api/products";
        final StringRequest addProdReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response : ", response);
                        try{
                            JSONObject obj = new JSONObject(response);
                            int code = obj.getInt("code");
                            if(code == 200){
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            }
                            else{
                                //Toast.makeText(getApplicationContex(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                ProdErrorRes errorRes = new Gson().fromJson(obj.getString("message"), ProdErrorRes.class);
                                if(errorRes.getProdNameError().size() != 0){
                                    if(errorRes.getProdNameError().get(0) != null){
                                        etNm.setError(errorRes.getProdNameError().get(0));
                                        Toast.makeText(getApplicationContext(), errorRes.getProdNameError().get(0), Toast.LENGTH_LONG).show();
                                    }
                                }
                                if(errorRes.getProdQtyError().size() != 0){
                                    if(errorRes.getProdQtyError().get(0) != null){
                                        etQty.setError(errorRes.getProdQtyError().get(0));
                                        Toast.makeText(getApplicationContext(), errorRes.getProdQtyError().get(0), Toast.LENGTH_LONG).show();
                                    }
                                }
                                if(errorRes.getProdPriceError().size() != 0){
                                    if(errorRes.getProdPriceError().get(0) != null){
                                        etPrice.setError(errorRes.getProdPriceError().get(0));
                                        Toast.makeText(getApplicationContext(), errorRes.getProdPriceError().get(0), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                            Log.i("response : ", response);
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "Product Added", Toast.LENGTH_LONG).show();
                    }
                },
            new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),  String.valueOf(error.networkResponse), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<>();
                params.put("ProductName", prodName);
                params.put("ProductQty", prodQty);
                params.put("ProductDesc", prodDesc);
                params.put("ProductPrice", prodPrice);
                if(prodImg != null) {
                    params.put("ProductImage", prodImg);
                }
                params.put("categoryId", catId);
                params.put("merchantId", merchId);
                return params;
            }
        };
        {
            int socketTimeout = 30000;
            RetryPolicy police = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            addProdReq.setRetryPolicy(police);
            RequestQueue rq = Volley.newRequestQueue(this);
            rq.add(addProdReq);
        }
    }
}
