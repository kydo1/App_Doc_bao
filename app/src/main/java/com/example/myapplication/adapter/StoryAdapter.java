package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.modal.Story;
import com.squareup.picasso.Picasso;
import java.util.List;
public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {
    List<Story>list;
    View view;
    public IclickDetail clickItem;
    public   interface  IclickDetail{
        void detailStory(Story story);
    }
    public StoryAdapter(IclickDetail clickItem) {
        this.clickItem = clickItem;
    }
    public  void setData(List<Story>storyList){
        this.list=storyList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_story,parent,false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
       final   Story story=list.get(position);
        if(story==null){
            return;

        }
        holder.tv_Name.setText(story.getName());
        Picasso.get().load(story.getImage()).into(holder.img_Story);
        holder.img_Story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItem.detailStory(story);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(list!=null)
            return list.size();
        return 0;
    }
    public  class StoryViewHolder extends RecyclerView.ViewHolder{
        TextView tv_Name;
        ImageView img_Story;
        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Name=itemView.findViewById(R.id.tv_name);
            img_Story=itemView.findViewById(R.id.img_story);
        }
    }
}
