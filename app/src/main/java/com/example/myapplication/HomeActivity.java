package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.aware.PublishConfig;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.NewsAdapter.NewsAdapter;
import com.example.myapplication.bean.news;
import com.example.myapplication.bean.newsmanager;
import com.example.myapplication.ui.home.HomeViewModel;
import com.example.myapplication.util.SPUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends MainActivity {
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TwinklingRefreshLayout refreshLayout;
    private newsmanager newsManager;
    private HomeViewModel model;
    private Timer timer;
    @SuppressLint("HandlerLeak")
    private final Handler messageHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    refreshLayout.finishRefreshing();
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
    private ProgressDialog progressDialog;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("正在下载中，请稍后......");
//    设置setCancelable(false); 表示我们不能取消这个弹出框，等下载完成之后再让弹出框消失
        progressDialog.setCancelable(false);
//    设置ProgressDialog样式为圆圈的形式
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        initNewsManager();
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_home, null, false);
        drawer.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.nav_home);
        model = new ViewModelProvider(this).get(HomeViewModel.class);
        model.setList(newsManager.newslist);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyAsyncTask().execute("");
            }
        });
        recyclerView=contentView.findViewById(R.id.news_rv);
        refreshLayout = (TwinklingRefreshLayout) contentView.findViewById(R.id.refreshLayout);
        initRecyclerview();
        initRefreshLayout();
        Log.v("created","homeactivity");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initNewsManager(){
                newsManager = new newsmanager("news");
    }
    private void initRecyclerview(){
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        newsAdapter = new NewsAdapter(model.getNewsdata());
        newsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(HomeActivity.this,"这是条目"+model.getNewsdata().get(position),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(),NewspageActivity.class);
                intent.putExtra("uid",newsAdapter.getNewsbyPos(position).uid);
                startActivity(intent);
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
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Thread() {
                    @Override
                    public void run() {//在run()方法实现业务逻辑；
                        //...
                        newsManager.refresh();
                        newsAdapter.SetData(newsManager.newslist);
                        //更新UI操作；
                        messageHandler.sendEmptyMessage(1);
                    }
                }.start();
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout){
                new Thread() {
                    @Override
                    public void run() {//在run()方法实现业务逻辑；
                        //...
                        newsManager.getmore();
                        ArrayList<news> test;
                        for(int i=0;i<20;i++){
                            newsAdapter.Add(newsManager.newslist.get(i));
                        }

                       // newsAdapter.AddData(newsManager.newslist);
                        //更新UI操作；
                        messageHandler.sendEmptyMessage(2);
                    }
                }.start();
            }
        });
        refreshLayout.startRefresh();
    }


    /*
    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        new Thread() {
            @Override
            public void run() {//在run()方法实现业务逻辑；
                //...
                newsManager.refresh();
                newsAdapter.SetData(newsManager.newslist);
                //更新UI操作；
                messageHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        newsAdapter.notifyDataSetChanged();
                    }
                });
                refreshLayout.setRefreshing(false);
            }
        }.start();
    }

     */

                        public class MyAsyncTask extends AsyncTask<String, Integer, ArrayList<news>> {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            protected ArrayList<news> doInBackground(String... strings) {
                                newsManager.refresh();
                                return newsManager.newslist;
                            }

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                progressDialog.show();
                                //    在onPreExecute()中我们让ProgressDialog显示出来
                                //progressDialog.show();
                            }

                            @RequiresApi(api = Build.VERSION_CODES.N)


                            @Override
                            protected void onProgressUpdate(Integer... values) {
                                super.onProgressUpdate(values);
                            }

                            @Override
                            protected void onPostExecute(ArrayList<news> result) {
                                super.onPostExecute(result);
                                //    将doInBackground方法返回的byte[]解码成要给Bitmap
                                newsAdapter.SetData(newsManager.newslist);
                                newsAdapter.notifyDataSetChanged();
                                //Log.v("listsize",String.valueOf(model.getNewsSize()));
                                progressDialog.dismiss();

                            }
                        }
}
