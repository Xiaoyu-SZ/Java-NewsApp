package com.java.tangningjing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.java.tangningjing.bean.entity;
import com.java.tangningjing.bean.news;
import com.java.tangningjing.bean.newsmanager;
import com.java.tangningjing.bean.pair;
import com.java.tangningjing.bean.relations;
import com.mob.MobSDK;
import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EntitydetailActivity extends MainActivity{
    ListView relationsview;
    ListView propertyviews;
    String label;
    entity current;
    ArrayList<relations> Relations;
    ArrayList<pair> Pros;
    private static class ViewHolder {
        TextView type;
        TextView relenti;
    }
    private class RelationAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return Relations.size();
        }
        @Override
        public relations getItem(int position) {
            return Relations.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(getApplicationContext(), R.layout.entityresult_relation_item, null);
                holder.type = (TextView) convertView.findViewById(R.id.relationtype);
                holder.relenti = (TextView) convertView.findViewById(R.id.relatedentity);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            relations item = getItem(position);
            if(item.forward){
                holder.type.setText(label+item.relation);
                holder.relenti.setText(item.label);
            }
            else{
                holder.type.setText(item.label+item.relation);
                holder.relenti.setText(label);
            }
            return convertView;
        }
    }
    private class PropertyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return Pros.size();
        }
        @Override
        public pair getItem(int position) {
            return Pros.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(getApplicationContext(), R.layout.entityresult_relation_item, null);
                holder.type = (TextView) convertView.findViewById(R.id.relationtype);
                holder.relenti = (TextView) convertView.findViewById(R.id.relatedentity);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            pair item = getItem(position);
            holder.type.setText(item.first+":");
            holder.type.setText(item.second);
            return convertView;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       SugarContext.init(this);
       // MobSDK.submitPolicyGrantResult(true, null);
        Intent intent = getIntent();
        label = intent.getStringExtra("name");
        ArrayList<entity> arr=new ArrayList<>();
        try{
            List<entity> Ent=entity.listAll(entity.class);
            arr = (ArrayList<entity>) entity.find(entity.class, "label = ? ", label);
        }
        catch(Exception e){
            e.printStackTrace();
            return;
        }
        if(arr.size()==0){
           Log.v("error","couldn't find entity");
           return;
        }
        current=arr.get(0);
        Relations=  (ArrayList<relations>) intent.getSerializableExtra(
                "relations");
        Pros=(ArrayList<pair>)intent.getSerializableExtra("properties");
       // Relations=intent.get("relation");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.entitydetail_layout, null, false);
        drawer.addView(contentView, 0);
//        newscontent=contentView.findViewById(R.id.newscontent);
//        newstitle=contentView.findViewById(R.id.newstitle);
        relationsview=contentView.findViewById(R.id.entityrelations);
        propertyviews=contentView.findViewById(R.id.entityproperties);
        relationsview.setAdapter(new RelationAdapter());
        propertyviews.setAdapter(new PropertyAdapter());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });
        //getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.back_menu, menu);
        return true;
    }
}
