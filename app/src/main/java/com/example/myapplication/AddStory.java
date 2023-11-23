package com.example.myapplication;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
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
import com.google.android.gms.tasks.OnFailureListener;
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


public class AddStory extends AppCompatActivity {
    TextView tv_chon_anh_bia,tv_chon_noi_dung_anh,tv_them;
    private RecyclerView rcv_content;
    private ImageView mImageView;
    ImageAdapter imageAdapter;
    private List<String> mImages = new ArrayList<>();
    private static final int REQUEST_CODE_IMAGE = 100;
    private static final  int PICK_IMAGE_REQUEST = 999;
    String name, author, description, time;
    TextInputEditText edt_ten,edt_mo_ta,edt_tac_gia,edt_thoi_gian;
    private Spinner spinnerSp;
    String image ;
    String theloai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);



        spinnerSp = findViewById(R.id.spinerSanPham);
        unitUi();

        tv_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edt_ten.getText().toString();
                description = edt_mo_ta.getText().toString();
                author = edt_tac_gia.getText().toString();
                time = edt_thoi_gian.getText().toString();
                String content = mImages.toString();
                String[] listField = new String[] {"name", "description","image", "author","time","content","theloai"};
                JSONObject jsonObject = GeneralFunction.putData(listField,name,description,image,author,time,content,theloai);
                GeneralFunction.postData(Api.api + "/addStory", jsonObject, getApplicationContext(), new handleInterface() {
                    @Override
                    public void onSuccess(JSONObject res) {
                        Intent intent = new Intent(AddStory.this,MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        });

        tv_chon_anh_bia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(PICK_IMAGE_REQUEST);


            }
        });
        tv_chon_noi_dung_anh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(REQUEST_CODE_IMAGE);

            }
        });
    }

    private  void unitUi(){
        tv_chon_anh_bia = findViewById(R.id.tv_chon_anh_bia);
        tv_chon_noi_dung_anh = findViewById(R.id.tv_chon_noi_dung_truyen);
        mImageView = findViewById(R.id.img_anh_bia);
        rcv_content = findViewById(R.id.rcv_content);
        tv_them = findViewById(R.id.tv_them);
        edt_ten = findViewById(R.id.edt_name_story);
        edt_mo_ta = findViewById(R.id.edt_description_story);
        edt_thoi_gian = findViewById(R.id.edt_date_story);
        edt_tac_gia = findViewById(R.id.edt_author_story);
        rcv_content.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
        imageAdapter = new ImageAdapter(this, mImages, new ImageAdapter.IClick() {
            @Override
            public void handleClick(String url,int index) {
                return;
            }
        });
        rcv_content.setAdapter(imageAdapter);

    }
    private void pickImage(int RequestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, RequestCode);

    }

    private  void  fetchDataCategory(){
        GeneralFunction.fetchData(Category.class, Api.api+"/getCategories", this, new FetchDataCallback<Category>() {
            @Override
            public void onSuccess(ArrayList<Category> result) {
                List<Category> tmp  = new ArrayList<>();
                tmp.addAll(result);
                ArrayAdapter sanPhamAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner, tmp);
                spinnerSp.setAdapter(sanPhamAdapter);
                spinnerSp.setSelection(-1);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            GeneralFunction.checkRequestCode(data, new UploadImageInterface() {
                @Override
                public void onSuccess(Uri imgUri) {
                    image = imgUri.toString();
                    Picasso.get().load(imgUri).into(mImageView);

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
                    mImages.add(uri.toString());
                    imageAdapter.notifyDataSetChanged();


                }

                @Override
                public void onError(String err) {

                }
            });



        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchDataCategory();
    }
}

