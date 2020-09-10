package com.java.tangningjing;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class TActivity extends MainActivity implements ChannelAdapter.onItemRangeChangeListener {

    private RecyclerView mRecyclerView;
    private List<ChannelBean> mList;
    private ChannelAdapter mAdapter;
    private String select[]={};
    private String recommend[]={};
    private String city[] = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int currentchannel=intent.getIntExtra("currentchannel",2);
        switch(currentchannel){
            case 2:
                select=new String[]{"标签：","新闻","论文"};
                break;
            case 1:
                select=new String[]{"标签：","新闻"};
                recommend=new String[]{"论文"};
                break;
            case 0:
                select=new String[]{"标签：","论文"};
                recommend=new String[]{"新闻"};
                break;
            default:
                break;
        }
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_t, null, false);
        drawer.addView(contentView,0);
        mRecyclerView = contentView.findViewById(R.id.recyclerView);
        mList = new ArrayList<>();
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mList.get(position).getSpanSize();
            }
        });
        mRecyclerView.setLayoutManager(manager);
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setMoveDuration(300);     //设置动画时间
        animator.setRemoveDuration(0);
        mRecyclerView.setItemAnimator(animator);
        ChannelBean title = new ChannelBean();
        title.setLayoutId(R.layout.adapter_title);
        title.setSpanSize(4);
        mList.add(title);
        for (String bean : select) {
            mList.add(new ChannelBean(bean, 1, R.layout.adapter_channel, true));
        }
        ChannelBean tabBean = new ChannelBean();
        tabBean.setLayoutId(R.layout.adapter_tab);
        tabBean.setSpanSize(4);
        mList.add(tabBean);
        List<ChannelBean> recommendList = new ArrayList<>();
        for (String bean : recommend) {
            recommendList.add(new ChannelBean(bean, 1, R.layout.adapter_channel, true));
        }
        List<ChannelBean> cityList = new ArrayList<>();
        for (String bean : city) {
            cityList.add(new ChannelBean(bean, 1, R.layout.adapter_channel, false));
        }
        ChannelBean moreBean = new ChannelBean();
        moreBean.setLayoutId(R.layout.adapter_more_channel);
        moreBean.setSpanSize(4);
        cityList.add(moreBean);
        mList.addAll(recommendList);
        mAdapter = new ChannelAdapter(this, mList, recommendList,cityList);
        mAdapter.setFixSize(1);
        mAdapter.setSelectedSize(select.length);
        mAdapter.setRecommend(true);
        mAdapter.setOnItemRangeChangeListener(this);
        mRecyclerView.setAdapter(mAdapter);
        WindowManager m = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int spacing = (m.getDefaultDisplay().getWidth() - dip2px(this, 70) * 4) / 5;
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4,spacing,true));
        ItemDragCallback callback=new ItemDragCallback(mAdapter,2);
        ItemTouchHelper helper=new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                if(mAdapter.hasPaper&&mAdapter.hasNews) {
                    intent.putExtra("currentchannel",2);
                }
                else if(mAdapter.hasNews){
                    intent.putExtra("currentchannel",1);
                }
                else if(mAdapter.hasPaper){
                    intent.putExtra("currentchannel",0);
                }
                setResult(RESULT_OK, intent);
                finish();//返回
            }
        });
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void refreshItemDecoration() {
        mRecyclerView.invalidateItemDecorations();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.back_menu, menu);
        return true;
    }
}
