package com.java.tangningjing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.tangningjing.NewsAdapter.NewsAdapter;
import com.java.tangningjing.ui.history.HistoryViewModel;
import com.java.tangningjing.ui.home.HomeViewModel;

public class HistoryActivity extends MainActivity{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter newsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_history, null, false);
        drawer.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.nav_history);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "历史记录来了",
                        Toast.LENGTH_SHORT).show();
            }
        });
        HistoryViewModel model = new ViewModelProvider(this).get(HistoryViewModel.class);
        /*
        recyclerView=contentView.findViewById(R.id.news_rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        newsAdapter = new NewsAdapter(model.getNewsdata());
        recyclerView.setAdapter(newsAdapter);
         */
        Log.v("created","historyactivity");
    }
}
