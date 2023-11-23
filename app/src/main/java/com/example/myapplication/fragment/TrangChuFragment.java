package com.example.myapplication.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.myapplication.DetailActicity;
import com.example.myapplication.GeneralFunction.FetchDataCallback;
import com.example.myapplication.GeneralFunction.GeneralFunction;
import com.example.myapplication.R;
import com.example.myapplication.adapter.StoryAdapter;
import com.example.myapplication.api.Api;
import com.example.myapplication.modal.Story;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class TrangChuFragment extends Fragment {
    private View view;
    StoryAdapter storyAdapter ;
    RecyclerView rcv_Story;
    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
    private EditText editTextTimKiem;
    private ImageView imageView;
    List<Story> stories = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_trang_chu, container, false);
        imageView = view.findViewById(R.id.btnsearch);
        editTextTimKiem =view.findViewById(R.id.etEditText);

        imageView.setOnClickListener(view->{
            String quey = editTextTimKiem.getText().toString();
            List<Story> tmp = new ArrayList<>();
            for (Story object: stories) {
                if(object.getName().trim().toLowerCase().contains(quey.trim().toLowerCase())){
                    tmp.add(object);
                }
            }
            storyAdapter.setData(tmp);
            rcv_Story.setAdapter(storyAdapter);
        });


        rcv_Story=view.findViewById(R.id.rcv_story);
        storyAdapter = new StoryAdapter(new StoryAdapter.IclickDetail() {
            @Override
            public void detailStory(Story story) {
                Intent intent=new Intent(getActivity(), DetailActicity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("story",story);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        return view;
    }

    private  void  fetchData(){
        GeneralFunction.fetchData(Story.class, Api.api+"/getAllStory", getContext(), new FetchDataCallback<Story>() {
            @Override
            public void onSuccess(ArrayList<Story> result) {
                stories.addAll(result);
                rcv_Story.setLayoutManager(gridLayoutManager);
                storyAdapter.setData(result);
                rcv_Story.setAdapter(storyAdapter);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchData();
    }
}