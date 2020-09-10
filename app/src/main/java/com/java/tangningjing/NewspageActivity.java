package com.java.tangningjing;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.java.tangningjing.NewsAdapter.NewsAdapter;
import com.java.tangningjing.bean.news;
import com.java.tangningjing.bean.newsmanager;
import com.java.tangningjing.ui.home.HomeViewModel;
import com.mob.MobSDK;
import com.orm.SugarContext;
import com.orm.util.SugarConfig;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewspageActivity extends MainActivity {
    TextView newscontent;
    news current;
    TextView newstitle;
    int pos=0;
    @SuppressLint("HandlerLeak")
    private final Handler messageHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SugarContext.init(this);
        MobSDK.submitPolicyGrantResult(true, null);
        Intent intent = getIntent();
        final String newsid = intent.getStringExtra("uid");
        pos=intent.getIntExtra("position",0);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.fragment_newspage, null, false);
        drawer.addView(contentView, 0);
        newscontent=contentView.findViewById(R.id.newscontent);
        newstitle=contentView.findViewById(R.id.newstitle);
        ((Button)(contentView.findViewById(R.id.shareButton))).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
//                OnekeyShare oks = new OnekeyShare();
//                oks.setTitle("分享");
//                oks.setText("我是分享文本");
//               // oks.setTitleUrl("http://sharesdk.cn");
//                oks.show(MobSDK.getContext());
                Intent intent;
                intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, newstitle.getText().toString()+"  内容:"+newscontent.getText().toString().substring(0,10)+"...");
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "分享到..."));
            }
        });
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("position",pos);
                setResult(RESULT_OK, intent);
                finish();//返回
            }
        });
        //getActionBar().setDisplayHomeAsUpEnabled(true);
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
                        newstitle.setText(current.title);
                    }
                });
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.back_menu, menu);
        return true;
    }
    public void onTerminate(){
        SugarContext.terminate();
    }
}
