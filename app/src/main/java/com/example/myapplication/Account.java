package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.myapplication.GeneralFunction.UploadImageInterface;
import com.example.myapplication.GeneralFunction.handleInterface;
import com.example.myapplication.api.Api;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class Account extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String email,fullName,avatar;
    TextInputEditText tv_email,tv_fullName;
    TextView tv_dang_xuat;
    ImageView img_avatar;
    private static final  int PICK_IMAGE_REQUEST = 999;

    String _id ="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        tv_email = findViewById(R.id.edt_email);
        tv_fullName = findViewById(R.id.edt_fullname);
        tv_dang_xuat = findViewById(R.id.tv_dang_xuat);
        img_avatar = findViewById(R.id.profile_image);

        img_avatar.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
               intent.setType("image/*");
               startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        tv_dang_xuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("_id",null);
                editor.putString("fullName",null);
                editor.putString("email",null);
                editor.putString("avatar",null);
                editor.putBoolean("isLogin",false);
                editor.putBoolean("isAdmin",false);
                editor.apply();
                Intent intent = new Intent(Account.this,Login.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences=getSharedPreferences("infoUser",MODE_PRIVATE);
        editor= sharedPreferences.edit();
        email = sharedPreferences.getString("email","");
        fullName = sharedPreferences.getString("fullName","");
        avatar = sharedPreferences.getString("avatar","");
        _id = sharedPreferences.getString("_id","");
        tv_email.setText(email);
        tv_fullName.setText(fullName);
        Picasso.get().load(avatar).into(img_avatar);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            GeneralFunction.checkRequestCode(data, new UploadImageInterface() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(img_avatar);
                    editor.putString("avatar",uri.toString());
                    editor.apply();
                    String[] listField = new String[] {"avatar"};

                    JSONObject jsonObject = GeneralFunction.putData(listField,uri.toString());
                    GeneralFunction.postData(Api.api + "/user/update/avatar/:" + _id, jsonObject, getApplicationContext(), new handleInterface() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Toast.makeText(Account.this, "Cập nhật avatar thành công", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError() {

                        }
                    });
                }

                @Override
                public void onError(String err) {

                }
            });



        }
    }
}