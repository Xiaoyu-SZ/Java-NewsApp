package com.java.tangningjing;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.tangningjing.NewsAdapter.NewsAdapter;
import com.java.tangningjing.bean.news;
import com.java.tangningjing.bean.newsmanager;
import com.java.tangningjing.bean.searchmanager;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.orm.SugarContext;

import java.util.ArrayList;

public class SearchResultActivity extends MainActivity{
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TwinklingRefreshLayout refreshLayout;
    private searchmanager searchManager;
    @SuppressLint("HandlerLeak")
    private final Handler messageHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    newsAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    newsAdapter.notifyDataSetChanged();
                    refreshLayout.finishLoadmore();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + msg.what);
            }
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SugarContext.init(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_home, null, false);
        drawer.addView(contentView, 0);
        // Get the intent, verify the action and get the query
        recyclerView=contentView.findViewById(R.id.news_rv);
        refreshLayout = (TwinklingRefreshLayout) contentView.findViewById(R.id.refreshLayout);
        initSearchManager();
        initRecyclerview();
        initRefreshLayout();
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(getApplicationContext(), query,
                    Toast.LENGTH_LONG).show();
            //doMySearch(query);
            new Thread() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {//在run()方法实现业务逻辑；
                    //...
                    searchManager.search(query);
                    newsAdapter.SetData(searchManager.newslist);
                    //更新UI操作；
                    messageHandler.sendEmptyMessage(1);
                }
            }.start();
        }
    }
    @Override
    protected void onNewIntent (Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(getApplicationContext(), query,
                    Toast.LENGTH_LONG).show();
            //doMySearch(query);
            new Thread() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {//在run()方法实现业务逻辑；
                    //...
                    searchManager.search(query);
                    newsAdapter.SetData(searchManager.newslist);
                    //更新UI操作；
                    messageHandler.sendEmptyMessage(1);
                }
            }.start();
        }
        super.onNewIntent(intent);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initSearchManager(){
        searchManager = new searchmanager();
    }
    private void initRecyclerview(){
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        newsAdapter = new NewsAdapter();
        newsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(HomeActivity.this,"这是条目"+model.getNewsdata().get(position),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(),NewspageActivity.class);
                intent.putExtra("uid",newsAdapter.getNewsbyPos(position).uid);
                intent.putExtra("position",position);
                startActivityForResult(intent,0);
            }
        });
        recyclerView.setAdapter(newsAdapter);
    }
    private void initRefreshLayout() {
        //refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
        //  android.R.color.holo_orange_light, android.R.color.holo_green_light);
        // refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout){
                new Thread() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {//在run()方法实现业务逻辑；
                        //...
                        searchManager.getmore();
                        newsAdapter.AddData(searchManager.newslist);
                        //更新UI操作；
                        messageHandler.sendEmptyMessage(2);
                    }
                }.start();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.back_menu, menu);
        return true;
    }
}
