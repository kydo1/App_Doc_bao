package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.myapplication.GeneralFunction.FetchDataCallback;
import com.example.myapplication.GeneralFunction.GeneralFunction;
import com.example.myapplication.GeneralFunction.handleInterface;
import com.example.myapplication.R;
import com.example.myapplication.adapter.CommentAdapter;
import com.example.myapplication.adapter.ImageAdapter;
import com.example.myapplication.api.Api;
import com.example.myapplication.modal.Comment;
import com.example.myapplication.modal.Story;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class DetailActicity extends AppCompatActivity  {
    ImageView img_Food;
    TextView tv_Name,tv_Description,tv_xem_chi_tiet,tvTacgia,tvNgayTao;
    SharedPreferences sharedPreferences;
    Story story;
    RecyclerView rcv_comments;
    String  _idUser="";
    TextInputEditText edt_binh_luan;
    ImageView img_send;
    Boolean isLogin;
    CommentAdapter commentAdapter;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    Boolean isAdmin = false;
    RecyclerView rcv_replies;
    CommentAdapter cmAdapter;
    LinearLayout layout;
    ImageView img_Edit,imgDelete;
    Boolean isClicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        untitUi();


        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        story=(Story) bundle.getSerializable("story");

        tvTacgia.setText("Tác giả:" + story.getAuthor());
        tvNgayTao.setText("Ngày tạo :" + story.getTime());
        setData();
        img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleComment();
            }
        });
        tv_xem_chi_tiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DetailActicity.this,DetailImage.class);
                intent1.putStringArrayListExtra("listContent",story.getContent());
                startActivity(intent1);
            }
        });

        img_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(DetailActicity.this,Update.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("story",story);
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(DetailActicity.this);
                builder.setMessage("Bạn Có Muốn Chắc Chắn Xóa Sản Phẩm "+" "+story.getName()+"Này Không").setTitle("Xóa")
                        .setPositiveButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setNegativeButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                             handleDelete();
                            }
                        });
                builder.show();
            }
        });
    }
    private  void untitUi(){
        cmAdapter = new CommentAdapter(new CommentAdapter.iClickReplies() {
            @Override
            public void replyComment(Comment comment) {
                Toast.makeText(DetailActicity.this, "vao day", Toast.LENGTH_SHORT).show();
                String[] listField = new String[] {"text", "parent","userId"};
                String _id = comment.get_id();
                JSONObject jsonObject = GeneralFunction.putData(listField,"tra loi 2",_id,_idUser);
                GeneralFunction.postData(Api.api+"/comment/replies/:"+_id, jsonObject, getApplicationContext(), new handleInterface() {
                    @Override
                    public void onSuccess(JSONObject response) {

                    }

                    @Override
                    public void onError() {

                    }
                });

            }
        });

        tv_xem_chi_tiet = findViewById(R.id.tv_xem_chi_tiet);
        img_Food=findViewById(R.id.img_story);
        tv_Name=findViewById(R.id.tv_name);
        tv_Description=findViewById(R.id.tv_description);
        rcv_replies = findViewById(R.id.rcv_replies);
        layout = findViewById(R.id.linear_Admin);
        rcv_comments = findViewById(R.id.rcv_comments);
        edt_binh_luan = findViewById(R.id.edt_comment);
        img_send = findViewById(R.id.img_send);
        img_Edit = findViewById(R.id.img_edit);
        imgDelete = findViewById(R.id.img_delete);
        tvTacgia = findViewById(R.id.tvtacgia);
        tvNgayTao = findViewById(R.id.tvNgayTao);


        commentAdapter = new CommentAdapter(new CommentAdapter.IclickDetail() {
            @Override
            public void updateComment(Comment comment) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActicity.this);
                builder.setTitle("Sua");
                View view = LayoutInflater.from(DetailActicity.this).inflate(R.layout.dialog, null);
                EditText edt_text = view.findViewById(R.id.edt_new_text);
                builder.setView(view);
                edt_text.setHint(comment.getText());
                builder.setPositiveButton("OK", (dialog, which) -> {
                    String text = edt_text.getText().toString().trim();
                    handleUpdateComment(comment,text);


                });

                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

                AlertDialog dialog = builder.create();
                dialog.show();

            }

            @Override
            public void handleReportComment(Comment comment) {
                String _id = comment.get_id();
                GeneralFunction.handleReport(Api.api + "/report/comment/" +
                        ":" +_id, getApplicationContext(), new handleInterface() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        Toast.makeText(DetailActicity.this, "Báo cáo thành công", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {

                    }
                });

            }

            @Override
            public void handleMoreIconClick(Comment comment,LinearLayout layoutTong,LinearLayout layoutSua,LinearLayout layoutXoa,LinearLayout layoutBaoCao) {
                isClicked = !isClicked;
                if(isClicked){
                    layoutTong.setVisibility(View.VISIBLE);
                    if( _idUser .equals(comment.getUserId())){
                        layoutSua.setVisibility(View.VISIBLE);
                        layoutXoa.setVisibility(View.VISIBLE);
                        layoutBaoCao.setVisibility(View.GONE);
                    }else {
                        layoutBaoCao.setVisibility(View.VISIBLE);
                        layoutSua.setVisibility(View.GONE);
                        layoutXoa.setVisibility(View.GONE);

                    }
                }else {
                    layoutTong.setVisibility(View.GONE);


                }


            }

            @Override
            public void delete(Comment comment) {

                AlertDialog.Builder builder=new AlertDialog.Builder(DetailActicity.this);
                builder.setMessage("Bạn Có Muốn Chắc Chắn Xóa Bình Luận  Này Không").setTitle("Xóa")
                        .setPositiveButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setNegativeButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                handleDeleteComment(comment);
                            }
                        });

                builder.show();


            }

            @Override
            public void collapseComment(ArrayList<String> listCommentChild,String _id,RecyclerView rcv) {
                GeneralFunction.fetchData(Comment.class, Api.api + "/comments/replies/child/:" + _id, getApplicationContext(), new FetchDataCallback<Comment>() {
                    @Override
                    public void onSuccess(ArrayList<Comment> result) {
                        Toast.makeText(DetailActicity.this, "vao day", Toast.LENGTH_SHORT).show();
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        cmAdapter.setData(result);
                        rcv.setLayoutManager(layoutManager);
                        Log.d("LIST CHILD COMMENT",result.toString());
                        rcv.setAdapter(cmAdapter);
                        cmAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            }

        }
        );

    }
    private  void setData(){
        Picasso.get().load(story.getImage()).into(img_Food);
        tv_Description.setText(story.getDescription());
        tv_Name.setText(story.getName());

    }
    public  void  handleDeleteComment(Comment comment){
        String _id = comment.get_id();
        GeneralFunction.deleteData(Api.api + "/comment/delete/:" +_id, getApplicationContext(), new handleInterface() {
            @Override
            public void onSuccess(JSONObject res) {
                Toast.makeText(DetailActicity.this,"Xóa Comment Thành Công",Toast.LENGTH_LONG).show();
                fetchComment();
            }

            @Override
            public void onError() {
                Toast.makeText(DetailActicity.this,"loi",Toast.LENGTH_LONG).show();

            }
        },comment.getStoryId());


    }
    public  void  handleUpdateComment(Comment comment,String text){
            String _id = comment.get_id();
            String[] listField = new String[] {"text"};
           JSONObject jsonObject = GeneralFunction.putData(listField,text);
           GeneralFunction.postData(Api.api + "/comment/update/:" + _id, jsonObject, getApplicationContext(), new handleInterface() {
               @Override
               public void onSuccess(JSONObject res) {
                   fetchComment();

               }

               @Override
               public void onError() {

               }
           });

    }
    public  void  handleDelete(){
        String _id = story.get_id();
       GeneralFunction.deleteData(Api.api+"/delete/story/:"+_id, getApplicationContext(), new handleInterface() {
           @Override
           public void onSuccess(JSONObject res) {
               Intent intent =new Intent(DetailActicity.this,MainActivity.class);
               startActivity(intent);

           }

           @Override
           public void onError() {

           }
       });

    }
    private  void  handleComment(){
        String[] listField = new String[] {"text", "storyId","userId"};
        String text = edt_binh_luan.getText().toString();
        JSONObject jsonObject =  GeneralFunction.putData(listField,text,story.get_id(),_idUser);
        GeneralFunction.postData(Api.api + "/comment", jsonObject, getApplicationContext(), new handleInterface() {
            @Override
            public void onSuccess(JSONObject res) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        edt_binh_luan.setText("");
                        fetchComment();
                    }
                });


            }

            @Override
            public void onError() {

            }
        });
    }

    private  void fetchComment(){
        String _id = story.get_id();
        GeneralFunction.fetchData(Comment.class, Api.api + "/comments/:" + _id, getApplicationContext(), new FetchDataCallback<Comment>() {
            @Override
            public void onSuccess(ArrayList<Comment> result) {
                rcv_comments.setLayoutManager(linearLayoutManager);
                commentAdapter.setData(result);
                commentAdapter.notifyDataSetChanged();
                rcv_comments.setAdapter(commentAdapter);
                Log.d("COMMENT",result.toString());
            }

            @Override
            public void onError(String message) {

            }
        });

    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences=getSharedPreferences("infoUser",MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean("isLogin",false);
        _idUser = sharedPreferences.getString("_id","");
        isAdmin = sharedPreferences.getBoolean("isAdmin",false);
        if(!isAdmin){
            layout.setVisibility(View.GONE);
        }else{
            layout.setVisibility(View.VISIBLE);

        }
        fetchComment();

    }

}