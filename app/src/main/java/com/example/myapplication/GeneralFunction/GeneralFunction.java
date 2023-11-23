package com.example.myapplication.GeneralFunction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.AddStory;
import com.example.myapplication.DetailActicity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.api.Api;
import com.example.myapplication.modal.Story;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GeneralFunction {
    public static <T> void fetchData(Class<T> clazz, String api, Context context, final FetchDataCallback<T> callback,String... strings) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, api, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<T>>(){}.getType();
                        ArrayList<T> list = gson.fromJson(response.toString(), listType);
                        ArrayList<T> resultList = new ArrayList<>();
                        for (T item : list) {
                            T result = gson.fromJson(gson.toJson(item), clazz);
                            resultList.add(result);
                        }
                        callback.onSuccess(resultList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.getMessage());
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonArrayRequest);
    }
    public  static JSONObject putData( String[]listField,String... strings){
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < strings.length; i++) {
            String str = strings[i];
            try {
                jsonObject.put(listField[i], str);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return jsonObject;

    }

    public  static  void postData(String api,JSONObject jsonObject,Context context,handleInterface handleInterface,String... strings){
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                api,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            if (status == 200) {
                                handleInterface.onSuccess(response);
                            } else {
                                Toast.makeText(context, " Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý lỗi
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
        );
        queue.add(request);

    }
    public  static  void handleReport(String api,Context context,handleInterface handleInterface){
        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    api,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            handleInterface.onSuccess(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Xử lý lỗi
                            handleInterface.onError();

                        }
                    }
            );
            queue.add(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public  static  void deleteData(String api,Context context,handleInterface handleInterface,String... strings){
       JSONObject jsonObject = null;
        if(strings.length>0){
             jsonObject = new JSONObject();
            try {
                jsonObject.put("storyId",strings[0]);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    api,
                    jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            handleInterface.onSuccess(response);


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Xử lý lỗi
                            handleInterface.onError();

                        }
                    }
            );
            queue.add(request);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public  static void   handleTimesDiff(String createdAtString, TextView tvTime){

        Instant currentTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentTime = Instant.now();
        }
        try {
            Instant createdAt = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss 'GMT'XXX yyyy", Locale.ENGLISH);
                 createdAt = formatter.parse(createdAtString, Instant::from);
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Duration duration = Duration.between(createdAt, currentTime);
                long minutes = duration.toMinutes();
                long hours = duration.toHours();
                long days = duration.toDays();
                if(days>0){
                    tvTime.setText(days+ " ngày trước");

                } else if (hours>0) {
                    tvTime.setText(hours + " giờ trước");

                }else {
                    tvTime.setText(minutes +" phút trước");
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public  static  void checkRequestCode(Intent data, UploadImageInterface uploadImageInterface){
        Uri imageUri = data.getData();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("listStory").child(imageUri.getLastPathSegment());
        UploadTask uploadTask = storageRef.putFile(imageUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uriImage) {
                        uploadImageInterface.onSuccess(uriImage);

                    }
                });
            }
        });
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                uploadImageInterface.onError(e.getMessage());
            }
        });

    }




}
