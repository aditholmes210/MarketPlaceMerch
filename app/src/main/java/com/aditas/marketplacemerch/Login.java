package com.aditas.marketplacemerch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.aditas.marketplacemerch.Entity.AccessToken;
import com.aditas.marketplacemerch.Entity.RegistErrorRes;
import com.aditas.marketplacemerch.Network.VolleyService;
import com.aditas.marketplacemerch.Util.TokenManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends AppCompatActivity {
    @BindView(R.id.et_mail) EditText etMail;
    @BindView(R.id.et_pass) EditText etPass;
    @BindView(R.id.btn_login)
    Button btnLogin;

    final String EMAIL = "email";
    final String PASSWORD = "password";

    AccessToken at;

    String email, pass;
    //int isMerch = 1; //set 1 for true, set 1 in merch and 0 in cust

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_login)
    public void goToRegist(){
        Intent intent = new Intent (Login.this, Regist.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_login)
    public void regist(){
        if(isValidInput() == true){
            postDataRegist();
        }
    }

    private void postDataRegist() {
        email = etMail.getText().toString();
        pass = etPass.getText().toString();

        String url = "http://210.210.154.65:4444/api/auth/login";
        StringRequest registReq = new StringRequest(Request.Method.POST, url,
                response -> {
                        //do whatever u want with response
                        at = new Gson().fromJson(response, AccessToken.class);
                        TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE)).saveToken(at);
                },
                error -> {
                        String statusCode = String.valueOf(error.networkResponse.statusCode);
                        String body = "";
                        try{
                            body = new String(error.networkResponse.data, "UTF-8");
                            System.out.println(body);
                            JSONObject res = new JSONObject(body);

                            RegistErrorRes errorRes = new Gson().fromJson(res.getJSONObject("error").toString(),RegistErrorRes.class);

                            if(errorRes.getEmailError().size() > 0){
                                if(errorRes.getEmailError().get(0) != null){
                                    etMail.setError(errorRes.getEmailError().get(0));
                                }
                            }
                        }
                        catch(UnsupportedEncodingException e){
                            e.printStackTrace();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String,String> head = new Hashtable<>();
//
//                head.put("Accept","application/json");
//                head.put("Content-Type","application/x-www-form-urlencoded");
//
//                return head;
//            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new Hashtable<>();

                param.put(EMAIL, email);
                param.put(PASSWORD, pass);

                return param;
            }
        };
        VolleyService.getInstance(getApplicationContext()).addToReqQueue(registReq);
    }

    private boolean isValidInput(){
        boolean isValid = true;

        if(etMail.getText().toString().isEmpty()){
            etMail.setError("ISI EMAILNYAAA");
            isValid = false;
        } else if(!etMail.getText().toString().contains("@")){
            etMail.setError("ISI YANG BENEERRR");
            isValid = false;
        }

        if(etPass.getText().toString().isEmpty()){
            etPass.setError("ISI PASSWORDNYAAA");
            isValid = false;
        } else if(etPass.getText().toString().length() < 8){
            etPass.setError("HARUS LEBIH DARI 8 HURUF");
            isValid = false;
        }

        return isValid;
    }
}
