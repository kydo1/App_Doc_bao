package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.myapplication.adapter.ContentAdapter;
import com.example.myapplication.adapter.ImageAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailImage extends AppCompatActivity {
    ContentAdapter contentAdapter;
    RecyclerView rcvContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image);
        Intent intent = getIntent();
        ArrayList<String> list = intent.getStringArrayListExtra("listContent");
        if(list!=null){
            rcvContent = findViewById(R.id.rcv_content);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rcvContent.setLayoutManager(layoutManager);
            contentAdapter = new ContentAdapter(getApplicationContext(), list);
            rcvContent.setAdapter(contentAdapter);
            contentAdapter.notifyDataSetChanged();

        }


    }
}