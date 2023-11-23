package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.GeneralFunction.FetchDataCallback;
import com.example.myapplication.GeneralFunction.GeneralFunction;
import com.example.myapplication.adapter.StoryAdapter;
import com.example.myapplication.api.Api;
import com.example.myapplication.modal.Story;

import java.util.ArrayList;
import java.util.List;

public class ListStoryFilter extends AppCompatActivity {
    StoryAdapter storyAdapter ;
    RecyclerView rcv_Story;
    GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
    String theloai = "";
    TextView tvName;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_story_filter);
        theloai = getIntent().getStringExtra("Category");
        tvName= findViewById(R.id.tv_name);
        imageView = findViewById(R.id.back);

        imageView.setOnClickListener(view->{
            finish();
        });
        tvName.setText(theloai);

        rcv_Story=findViewById(R.id.rcv_story);
        storyAdapter = new StoryAdapter(new StoryAdapter.IclickDetail() {
            @Override
            public void detailStory(Story story) {
                Intent intent=new Intent(ListStoryFilter.this, DetailActicity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("story",story);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }


    private  void  fetchData(){
        GeneralFunction.fetchData(Story.class, Api.api+"/getAllStory", getApplicationContext(), new FetchDataCallback<Story>() {
            @Override
            public void onSuccess(ArrayList<Story> result) {
                List<Story> tmp = new ArrayList<>();
                for(Story ob : result) {
                    if(ob.getTheloai().equals(theloai)) {
                        tmp.add(ob);
                    }
                }
                rcv_Story.setLayoutManager(gridLayoutManager);
                storyAdapter.setData(tmp);
                rcv_Story.setAdapter(storyAdapter);
            }

            @Override
            public void onError(String message) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        fetchData();
    }
}