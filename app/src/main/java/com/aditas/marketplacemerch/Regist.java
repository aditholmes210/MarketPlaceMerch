package com.aditas.marketplacemerch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aditas.marketplacemerch.Entity.AccessToken;
import com.aditas.marketplacemerch.Entity.RegistErrorRes;
import com.aditas.marketplacemerch.Util.TokenManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Map;

import butterknife.OnClick;

public class Regist extends AppCompatActivity {
    private EditText etFirst, etLast, etMail, etPass, etConf, etMerch;
    private Button btnReg;
    private TextView tvAlready;
    AccessToken at;
    RequestQueue rq;
    final String FIRST_NAME = "first_name";
    final String LAST_NAME = "last_name";
    final String EMAIL = "email";
    final String PASSWORD = "password";
    final String CPASSWORD = "confirm_password";
    final String MERCHANT_NAME = "merchant_name";
    String firstName, lastName, email, password, confirm, merchName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        rq = Volley.newRequestQueue(this);
        init();
    }

    @OnClick(R.id.tv_login)
    public void goToLogin(){
        Intent intent = new Intent(Regist.this, Login.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_regist)
    public void regist(){
        if(isValidInput() == true){
            postDataRegist();
        }
    }

    private void init(){
        etFirst = findViewById(R.id.et_first);
        etLast = findViewById(R.id.et_last);
        etMail = findViewById(R.id.et_mail);
        etPass = findViewById(R.id.et_pass);
        etConf = findViewById(R.id.et_confirm);
        etMerch = findViewById(R.id.et_merch);
        btnReg = findViewById(R.id.btn_regist);
        tvAlready = findViewById(R.id.tv_login);
    }

    private void postDataRegist() {
        firstName = etFirst.getText().toString();
        lastName = etLast.getText().toString();
        email = etMail.getText().toString();
        password = etPass.getText().toString();
        confirm = etConf.getText().toString();
        //isMerch = 1;
        merchName = etMerch.getText().toString();

        String url = "http://210.210.154.65:4444/api/auth/signup";
        StringRequest registerReq = new StringRequest(Request.Method.POST, url,
                response -> {
                        // do whatever u want with response
                        at = new Gson().fromJson(response,AccessToken.class);
                        TokenManager.getInstance(getSharedPreferences("pref",MODE_PRIVATE)).saveToken(at);
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                        },
                error -> {
                        String statusCode = String.valueOf( error.networkResponse.statusCode );
                        String body = "";
                        try {
                            body = new String(error.networkResponse.data, "UTF-8");
                            JSONObject res = new JSONObject(body);

                            RegistErrorRes errorRes = new Gson()
                                    .fromJson(res.getJSONObject("error")
                                            .toString(),RegistErrorRes.class);

                            if(errorRes.getEmailError().size() > 0){
                                if(errorRes.getEmailError().get(0) != null){
                                    etMail.setError(errorRes.getEmailError().get(0));
                                }
                            }
                        }
                        catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
            }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> head = new Hashtable<>();
                head.put("Accept","application/json");
                head.put("Content-Type","application/x-www-form-urlencoded");
                return head;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new Hashtable<>();

                param.put(FIRST_NAME, firstName);
                param.put(LAST_NAME, lastName);
                param.put(EMAIL ,email);
                param.put(PASSWORD, password);
                param.put(CPASSWORD, confirm);
                //param.put(IS_MERCHANT,String.valueOf(isMerch));
                param.put(MERCHANT_NAME, merchName);

                return param;
            }
        };

        //VolleyService.getInstance(getApplicationContext()).addToReqQueue(registerReq);
        rq.add(registerReq);
    }

    private boolean isValidInput() {
        boolean isValid = true;

        if(etFirst.getText().toString().isEmpty()){
            etFirst.setError("First name cannot be empty");
            isValid = false;
        }

        if(etLast.getText().toString().isEmpty()){
            etLast.setError("Last name cannot be empty");
            isValid = false;
        }

        if(etMail.getText().toString().isEmpty()){
            etMail.setError("email name cannot be empty");
            isValid = false;
        }else if(!etMail.getText().toString().contains("@")){
            etMail.setError("must be a valid email");
            isValid = false;
        }

        if(etPass.getText().toString().isEmpty()){
            etPass.setError("Password cannot be empty");
            isValid = false;
        }
        else if(etPass.getText().toString().length() < 8){
            etPass.setError("Password must be 8 or more character");
            isValid = false;
        }

        if(etConf.getText().toString().isEmpty()){
            etConf.setError("Confirm password cannot be empty");
            isValid = false;
        }
        else if(!(etConf.getText().toString().equals(etPass.getText().toString()))){
            etConf.setError("Password did not match");
            isValid = false;
        }

//        if(etMerch.getText().toString().isEmpty()){
//            etMerch.setError("Merchant Name cannot be empty");
//            isValid = false;
//        }

        return isValid;
    }

}
