package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.GeneralFunction.FetchDataCallback;
import com.example.myapplication.GeneralFunction.GeneralFunction;
import com.example.myapplication.adapter.StoryAdapter;
import com.example.myapplication.api.Api;
import com.example.myapplication.fragment.DanhMucFragment;
import com.example.myapplication.fragment.DichTuDienFragment;
import com.example.myapplication.fragment.TrangChuFragment;
import com.example.myapplication.modal.Story;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TrangChuFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_trangchu);
            toolbar.setTitle("Trang chủ");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_trangchu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TrangChuFragment()).commit();
                toolbar.setTitle("Trang chủ");
                break;

            case R.id.nav_danhmuc:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DanhMucFragment()).commit();
                toolbar.setTitle("Danh mục");
                break;

            case R.id.nav_dangtin:

                Intent intent1 = new Intent(MainActivity.this,AddStory.class);
                startActivity(intent1);
                break;
            case R.id.nav_acount:
                Intent intent2 = new Intent(MainActivity.this,Account.class);
                startActivity(intent2);

            case R.id.nav_translate:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DichTuDienFragment()).commit();
                toolbar.setTitle("Dịch Từ điển");
                break;

            case R.id.nav_logout:
                finishAndRemoveTask();

                break;


        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}