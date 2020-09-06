package com.example.myapplication;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.NewsAdapter.NewsAdapter;
import com.example.myapplication.bean.news;
import com.example.myapplication.bean.newsmanager;
import com.example.myapplication.ui.home.HomeViewModel;
import com.orm.SugarContext;
import com.orm.util.SugarConfig;

public class NewspageActivity extends MainActivity {
    TextView newscontent;
    news current;
    @SuppressLint("HandlerLeak")
    private final Handler messageHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SugarContext.init(this);
        Intent intent = getIntent();
        final String newsid = intent.getStringExtra("uid");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.fragment_newspage, null, false);
        drawer.addView(contentView, 0);
        newscontent=contentView.findViewById(R.id.newscontent);
        new Thread() {
            @Override
            public void run() {//在run()方法实现业务逻辑；
                //...
                current=newsmanager.loadorstore(newsid);
                //更新UI操作；
                messageHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        newscontent.setText(current.context);
                    }
                });
            }
        }.start();
    }
    public void onTerminate(){
        SugarContext.terminate();
    }
}
