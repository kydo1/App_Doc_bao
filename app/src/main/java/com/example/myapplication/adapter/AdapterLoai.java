package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.GeneralFunction.EventItem;
import com.example.myapplication.R;
import com.example.myapplication.modal.Category;

import java.util.ArrayList;
import java.util.List;

public class AdapterLoai extends RecyclerView.Adapter<AdapterLoai.ViewHolder>{
    private ArrayList<Category> categories;
    private final EventItem<Category> eventItem;
    private final Context mContext;

    public AdapterLoai(EventItem eventItem, Context mContext) {
        this.eventItem = eventItem;
        this.mContext = mContext;
    }

    public void setDataRecyclerview(ArrayList<Category> categories){
        this.categories = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_loai, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.tvTenLoai.setText(category.getName());
        holder.setOnClick(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenLoai;
        ImageView imgDelte;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenLoai = itemView.findViewById(R.id.tvLoai);
            imgDelte = itemView.findViewById(R.id.btnDelete);

        }
        public void setOnClick(Category category) {

            imgDelte.setOnClickListener(view ->{
                eventItem.onDeleteItem(category);
            });

            tvTenLoai.setOnClickListener(view ->{
                eventItem.onClickItem(category);
            });
        }
    }

}
