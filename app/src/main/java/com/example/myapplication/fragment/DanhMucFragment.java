package com.example.myapplication.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.AddStory;
import com.example.myapplication.GeneralFunction.EventItem;
import com.example.myapplication.GeneralFunction.FetchDataCallback;
import com.example.myapplication.GeneralFunction.GeneralFunction;
import com.example.myapplication.GeneralFunction.handleInterface;
import com.example.myapplication.ListStoryFilter;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Update;
import com.example.myapplication.adapter.AdapterLoai;
import com.example.myapplication.api.Api;
import com.example.myapplication.modal.Category;
import com.example.myapplication.modal.Story;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;


public class DanhMucFragment extends Fragment implements EventItem<Category> {
    private View view;

    private FloatingActionButton btnadd;
    private RecyclerView recyclerView;
    private AdapterLoai adapterLoai;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_danh_muc, container, false);

        btnadd = view.findViewById(R.id.btnADD);

        recyclerView = view.findViewById(R.id.rvLoaiSP);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        adapterLoai = new AdapterLoai(this,getContext());


        btnadd.setOnClickListener(view->{
            showCustomDialog();
        });
        return view;
    }


    //Function to display the custom dialog.
    void showCustomDialog() {
        final Dialog dialog = new Dialog(getContext());
        //We have added a title in the custom layout. So let's disable the default title.

        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.dialog_category);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);

        //Initializing the views of the dialog.
        final EditText nameEt = dialog.findViewById(R.id.name_et);
        Button submitButton = dialog.findViewById(R.id.submit_button);
        Button cancleButton = dialog.findViewById(R.id.cancle_button);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEt.getText().toString();
                if(name.isEmpty()) {
                    Toast.makeText(getContext(),"Vui lòng không được bỏ trống thông tin.",Toast.LENGTH_SHORT).show();
                    return;
                }
                Category object = new Category();
                object.setName(name);
                String[] listField = new String[] {"name"};
                JSONObject jsonObject = GeneralFunction.putData(listField,name);
                GeneralFunction.postData(Api.api + "/category", jsonObject, getContext(), new handleInterface() {
                    @Override
                    public void onSuccess(JSONObject res) {
                        fetchData();
                    }

                    @Override
                    public void onError() {

                    }
                });

                dialog.dismiss();
            }
        });


        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private  void  fetchData(){
        GeneralFunction.fetchData(Category.class, Api.api+"/getCategories", getContext(), new FetchDataCallback<Category>() {
            @Override
            public void onSuccess(ArrayList<Category> result) {
                adapterLoai.setDataRecyclerview(result);
                recyclerView.setAdapter(adapterLoai);
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

    @Override
    public void onDeleteItem(Category object) {
        showConfirmDelte(object);
    }

    @Override
    public void onClickItem(Category object) {
        Intent intent = new Intent(getActivity(), ListStoryFilter.class);
        intent.putExtra("Category",object.getName());
        startActivity(intent);
    }


    void showConfirmDelte(Category object) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Thông báo");
        builder.setMessage("Bạn chắc chắn muốn xoá danh mục này?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                String _id = object.get_id();

                GeneralFunction.postData(Api.api + "/category/:" + _id, null, getContext(), new handleInterface() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        fetchData();
                    }

                    @Override
                    public void onError() {

                    }
                });

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}