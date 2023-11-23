package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.GeneralFunction.GeneralFunction;
import com.example.myapplication.GeneralFunction.handleInterface;
import com.example.myapplication.api.Api;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextInputEditText edt_Email,edt_PassWord;
    String email,passWord;
    TextView tv_Login;
    TextView tv_Singup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unitUi();
        tv_Singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,SingUp.class);
                startActivity(intent);

            }
        });
        tv_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                fetchData();


            }
        });
    }
    private  void unitUi(){
        edt_Email=findViewById(R.id.edt_email);
        edt_PassWord=findViewById(R.id.edt_pass_word);
        tv_Login=findViewById(R.id.tv_login);
        tv_Singup = findViewById(R.id.tv_sing_up);
    }

    private  void getData(){
        email=edt_Email.getText().toString().trim();
        passWord=edt_PassWord.getText().toString().trim();
    }

    private void fetchData(){
        String[] listField = new String[] {"email","password"};

        JSONObject jsonObject = GeneralFunction.putData(listField,email,passWord);

        GeneralFunction.postData(Api.api + "/login", jsonObject, getApplicationContext(), new handleInterface() {
            @Override
            public void onSuccess(JSONObject response) {

                try {
                    int status = response.getInt("status");
                    JSONObject userInfor = response.getJSONObject("userFilter");
                    String _id = userInfor.getString("_id");
                    String fullname = userInfor.getString("fullName");
                    String avatar = userInfor.getString("avatar");
                    String email = userInfor.getString("email");
                    Boolean isAdmin = userInfor.getBoolean("isAdmin");
                    if(status == 200){
                        Intent intent = new Intent(Login.this,MainActivity.class);
                        editor.putString("_id",_id);
                        editor.putString("fullName",fullname);
                        editor.putString("email",email);
                        editor.putString("avatar",avatar);
                        editor.putBoolean("isLogin",true);
                        editor.putBoolean("isAdmin",isAdmin);

                        editor.apply();
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }



            }

            @Override
            public void onError() {

            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences=getSharedPreferences("infoUser",MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    }
