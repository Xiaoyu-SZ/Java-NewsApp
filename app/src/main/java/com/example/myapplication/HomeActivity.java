package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.NewsAdapter.NewsAdapter;
import com.example.myapplication.ui.home.HomeViewModel;

import java.util.List;

public class HomeActivity extends MainActivity {
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_home, null, false);
        drawer.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.nav_home);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "主页来了",
                        Toast.LENGTH_SHORT).show();
            }
        });
        final HomeViewModel model = new ViewModelProvider(this).get(HomeViewModel.class);
        recyclerView=contentView.findViewById(R.id.news_rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        newsAdapter = new NewsAdapter(model.getNewsdata());
        newsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(HomeActivity.this,"这是条目"+model.getNewsdata().get(position),Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),NewspageActivity.class));
            }
        });
        recyclerView.setAdapter(newsAdapter);
        Log.v("created","homeactivity");
    }
}
