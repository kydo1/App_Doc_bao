package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.GeneralFunction.FetchDataCallback;
import com.example.myapplication.GeneralFunction.GeneralFunction;
import com.example.myapplication.GeneralFunction.UploadImageInterface;
import com.example.myapplication.GeneralFunction.handleInterface;
import com.example.myapplication.adapter.ImageAdapter;
import com.example.myapplication.api.Api;
import com.example.myapplication.modal.Category;
import com.example.myapplication.modal.Story;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Update extends AppCompatActivity {
    Story story;
    TextInputEditText edt_ten_moi,edt_tac_gia_moi,edt_mo_ta_moi,edt_thoi_gian_moi;
    ImageView imageView_moi;
    RecyclerView recyclerView;
    private static final int REQUEST_CODE_IMAGE = 100;
    private static final  int PICK_IMAGE_REQUEST = 999;
    private static final  int PICK_IMAGE_UPDATE = 888;

    TextView btn_chon_anh_bia_moi,btn_chon_noi_dung_moi,btn_sua;
    String image ;
    ImageAdapter imageAdapter;
    public List<String>listImageStore = new ArrayList<>();
    private Spinner spinnerSp;
    String theloai;

    int index = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        story=(Story) bundle.getSerializable("story");



        unitUi();
        setData();
        spinnerSp = findViewById(R.id.spinerSanPham);
        btn_sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUpdate();
            }
        });
        btn_chon_anh_bia_moi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(PICK_IMAGE_REQUEST);

            }
        });
        btn_chon_noi_dung_moi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(REQUEST_CODE_IMAGE);
            }
        });
    }
    private  void  setData(){
        edt_ten_moi.setHint(story.getName());
        edt_thoi_gian_moi.setHint(story.getTime());
        edt_mo_ta_moi.setHint(story.getDescription());
        edt_tac_gia_moi.setHint(story.getAuthor());
        Picasso.get().load(Uri.parse(story.getImage())).into(imageView_moi);
    }
    public  void  handleUpdate(){
        String[] listField = new String[] {"name", "description","image", "author","time","content"};
        String name = edt_ten_moi.getText().toString();
        String description = edt_mo_ta_moi.getText().toString();
        String author = edt_tac_gia_moi.getText().toString();
        String time = edt_thoi_gian_moi.getText().toString();

        JSONObject jsonObject = GeneralFunction.putData(listField,name,description,image,author,time,listImageStore.toString());
        String _id = story.get_id();

        GeneralFunction.postData(Api.api + "/update/story/:" + _id, jsonObject, getApplicationContext(), new handleInterface() {
            @Override
            public void onSuccess(JSONObject response) {
                Intent intent = new Intent(Update.this,MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError() {

            }
        });



    }


    private  void  fetchDataCategory(){
        GeneralFunction.fetchData(Category.class, Api.api+"/getCategories", this, new FetchDataCallback<Category>() {
            @Override
            public void onSuccess(ArrayList<Category> result) {
                int index = -1;
                for (int i = 0 ;i< result.size();i++) {
                    if(result.get(i).getName().equals(story.getTheloai())) {
                        index = i;
                    }
                }



                List<Category> tmp  = new ArrayList<>();
                tmp.addAll(result);
                ArrayAdapter sanPhamAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner, tmp);
                spinnerSp.setAdapter(sanPhamAdapter);
                spinnerSp.setSelection(index);
                spinnerSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Category category = (Category) parent.getSelectedItem();
                        theloai = category.getName();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchDataCategory();
    }

    private  void  unitUi(){
        edt_mo_ta_moi = findViewById(R.id.edt_mo_ta_moi);
        edt_tac_gia_moi = findViewById(R.id.edt_tac_gia_moi);
        edt_ten_moi = findViewById(R.id.edt_truyen_moi);
        edt_thoi_gian_moi = findViewById(R.id.edt_nam_moi);
        imageView_moi = findViewById(R.id.img_anh_bia_moi);
        recyclerView = findViewById(R.id.rcv_content_moi);
        btn_chon_anh_bia_moi = findViewById(R.id.tv_chon_anh_bia_moi);
        btn_chon_noi_dung_moi = findViewById(R.id.tv_chon_noi_dung_truyen_moi);
        btn_sua = findViewById(R.id.tv_sua);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
        listImageStore = story.getContent();
        imageAdapter = new ImageAdapter(this, listImageStore, new ImageAdapter.IClick() {
            @Override
            public void handleClick(String url,int indexPostion) {
                index = indexPostion;
                pickImage(PICK_IMAGE_UPDATE);
            }
        });
        recyclerView.setAdapter(imageAdapter);
    }
    public  void pickImage(int requestCode){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            GeneralFunction.checkRequestCode(data, new UploadImageInterface() {
                @Override
                public void onSuccess(Uri imgUri) {
                    image = imgUri.toString();
                    Picasso.get().load(imgUri).into(imageView_moi);

                }

                @Override
                public void onError(String err) {


                }
            });

        }
        if (requestCode == PICK_IMAGE_UPDATE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            GeneralFunction.checkRequestCode(data, new UploadImageInterface() {
                @Override
                public void onSuccess(Uri imgUri) {
                    image = imgUri.toString();
                    listImageStore.set(index,image);
                    imageAdapter.notifyDataSetChanged();

                }

                @Override
                public void onError(String err) {


                }
            });

        }
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            GeneralFunction.checkRequestCode(data, new UploadImageInterface() {
                @Override
                public void onSuccess(Uri uri) {
                    listImageStore.add(uri.toString());
                    imageAdapter.notifyDataSetChanged();

                }

                @Override
                public void onError(String err) {

                }
            });
        }
    }

}