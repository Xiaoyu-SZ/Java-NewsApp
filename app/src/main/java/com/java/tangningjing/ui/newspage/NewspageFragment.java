package com.java.tangningjing.ui.newspage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.tangningjing.HomeActivity;
import com.java.tangningjing.NewsAdapter.NewsAdapter;
import com.java.tangningjing.NewspageActivity;
import com.java.tangningjing.R;
import com.java.tangningjing.bean.news;
import com.java.tangningjing.bean.newsmanager;
import com.java.tangningjing.ui.gallery.GalleryViewModel;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
//实现fragment 需要写ondestroy

public class NewspageFragment extends Fragment {
    private newsmanager newsManager;
    private NewsAdapter newsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TwinklingRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private String type;
    public NewspageFragment(String type){
        this.type=type;
    }
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        refreshLayout.startRefresh();
        super.onActivityCreated(savedInstanceState);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news_list, container, false);
        refreshLayout = (TwinklingRefreshLayout) root.findViewById(R.id.refreshLayout);
        recyclerView=(RecyclerView) root.findViewById(R.id.news_rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        newsAdapter = new NewsAdapter(new ArrayList<news>());
        newsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(HomeActivity.this,"这是条目"+model.getNewsdata().get(position),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getContext().getApplicationContext(),NewspageActivity.class); //可能会有错，要注意
                intent.putExtra("uid",newsAdapter.getNewsbyPos(position).uid);
                intent.putExtra("position",position);
                startActivityForResult(intent,0);  //result会回到fragment
            }
        });
        recyclerView.setAdapter(newsAdapter);
        newsManager=new newsmanager(type);
        //init refresh
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Thread() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {//在run()方法实现业务逻辑；
                        //...
                        newsManager.refresh(getActivity());
                        newsAdapter.SetData(newsManager.newslist) ;
                        messageHandler.sendEmptyMessage(1);
                        Log.v("test","refreshing");
                    }
                }.start();
            }
            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout){
                new Thread() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {//在run()方法实现业务逻辑；
                        Log.v("test","Loading");
                        if(newsManager.getmore(getActivity())) {
                            ArrayList<news> test;
                            for (int i = 0; i < 20; i++) {
                                newsAdapter.Add(newsManager.newslist.get(i));
                            }
                            // newsAdapter.AddData(newsManager.newslist);
                            //更新UI操作；
                            messageHandler.sendEmptyMessage(2);
                        }
                    }
                }.start();
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==0){
            if(resultCode==RESULT_OK) {
                int pos=data.getIntExtra("position",0);
                newsAdapter.getNewsbyPos(pos).cached=true;
                newsAdapter.notifyDataSetChanged();
            }
        }
      //  super.onActivityResult(requestCode, resultCode, data);
    }

}
