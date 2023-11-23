package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class SingUp extends AppCompatActivity {
    TextInputEditText edt_Email,edt_Pass_Word,edt_Full_Name;
    TextView tv_Sing_Up,tv_dang_nhap;
    String email,passWord,fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        edt_Email=findViewById(R.id.edt_email);
        edt_Pass_Word=findViewById(R.id.edt_pass_word);
        tv_Sing_Up=findViewById(R.id.tv_sing_up);
        tv_dang_nhap = findViewById(R.id.tv_login);

        edt_Full_Name = findViewById(R.id.edt_full_name);
        tv_dang_nhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingUp.this,Login.class);
                startActivity(intent);
            }
        });
        tv_Sing_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                if(passWord.length()==0||email.length()==0){
                    Toast.makeText(SingUp.this,"Vui Lòng Nhập Đầy Đủ Thông Tin",Toast.LENGTH_LONG).show();
                    return;
                }
                String[] listField = new String[] {"email","password","fullName"};

                JSONObject jsonObject = GeneralFunction.putData(listField,email,passWord,fullName);
                GeneralFunction.postData(Api.api + "/singup", jsonObject, getApplicationContext(), new handleInterface() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                edt_Full_Name.setText("");
                                edt_Email.setText("");
                                edt_Pass_Word.setText("");
                                Intent intent = new Intent(SingUp.this,Login.class);
                                startActivity(intent);
                            }
                        });

                    }

                    @Override
                    public void onError() {

                    }
                });





            }
        });
    }
    private  void getData(){
        email=edt_Email.getText().toString().trim();
        passWord=edt_Pass_Word.getText().toString().trim();
        fullName = edt_Full_Name.getText().toString().trim();
    }

}