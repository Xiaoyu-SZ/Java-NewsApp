package com.java.tangningjing;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.java.tangningjing.MainActivity;
import com.java.tangningjing.R;
import com.java.tangningjing.bean.downloadmanager;
import com.java.tangningjing.bean.entity;
import com.java.tangningjing.bean.pair;
import com.java.tangningjing.bean.relations;
import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.List;

public class EntityresultActivity extends MainActivity {
    private ExpandingList entityExpandingList;
    ArrayList<entity> entities;
    private final Handler messageHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //更新
                    //entities.clear();
                    createItems();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + msg.what);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SugarContext.init(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_entityresult, null, false);
        entityExpandingList=contentView.findViewById(R.id.expanding_list_main);
        drawer.addView(contentView, 0);
        entities=new ArrayList<>();
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
                    entities=downloadmanager.entitydownload(query);
                    //更新UI操作；
                    messageHandler.sendEmptyMessage(1);
                }
            }.start();
        }
    }
//    @Override
//    protected void onNewIntent (Intent intent){
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            final String query = intent.getStringExtra(SearchManager.QUERY);
//            Toast.makeText(getApplicationContext(), query,
//                    Toast.LENGTH_LONG).show();
//            //doMySearch(query);
//            new Thread() {
//                @RequiresApi(api = Build.VERSION_CODES.N)
//                @Override
//                public void run() {//在run()方法实现业务逻辑；
//                    //...
//                    entities=downloadmanager.entitydownload(query);
//                    //更新UI操作；
//                    messageHandler.sendEmptyMessage(1);
//                }
//            }.start();
//        }
//       super.onNewIntent(intent);
//    }
    private void createItems() {
//        addItem("John", new String[]{"House", "Boat", "Candy", "Collection"}, R.color.pink, R.drawable.ic_menu_camera);
//        addItem("Mary", new String[]{"Dog", "Horse", "Boat"}, R.color.orange, R.drawable.ic_menu_camera);
//        addItem("Ana", new String[]{"Cat"}, R.color.purple, R.drawable.ic_menu_camera);
//        addItem("Peter", new String[]{"Parrot", "Elephant", "Coffee"}, R.color.saffron, R.drawable.ic_menu_camera);
//        addItem("Joseph", new String[]{}, R.color.orange, R.drawable.ic_menu_camera);
//        addItem("Paul", new String[]{"Golf", "Football"}, R.color.green, R.drawable.ic_menu_camera);
//        addItem("Larry", new String[]{"Ferrari", "Mazda", "Honda", "Toyota", "Fiat"}, R.color.sienna, R.drawable.ic_menu_camera);
//        addItem("Moe", new String[]{"Beans", "Rice", "Meat"}, R.color.green, R.drawable.ic_menu_camera);
//        addItem("Bart", new String[]{"Hamburger", "Ice cream", "Candy"}, R.color.purple, R.drawable.ic_menu_camera);
        for(int i=0;i<entities.size();i++){
            addItem(entities.get(i),R.color.green,R.drawable.ic_menu_camera);
        }
    }

    private void addItem(final entity entityitem, int colorRes, int iconRes) {
        //Let's create an item with R.layout.expanding_layout
        final ExpandingItem item = entityExpandingList.createNewItem(R.layout.entity_expanding_layout);

        //If item creation is successful, let's configure it
        if (item != null) {
            item.setIndicatorColorRes(colorRes);
            item.setIndicatorIconRes(iconRes);
            //It is possible to get any view inside the inflated layout. Let's set the text in the item
            ((TextView) item.findViewById(R.id.entity_name)).setText(entityitem.label);

            //We can create items in batch.
            //item.createSubItems(subItems.length);
            if(entityitem.info!="") {
                item.createSubItems(1);
                for (int i = 0; i < item.getSubItemsCount(); i++) {
                    //Let's get the created sub item by its index
                    final View view = item.getSubItemView(i);
                    //Let's set some values in
                    configureSubItem(item, view, entityitem);
                }
            }
                item.findViewById(R.id.add_more_sub_items).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // showInsertDialog(entityitem);
                        Intent intent=new Intent(getApplicationContext(),EntitydetailActivity.class);
                        intent.putExtra("relations",entityitem.rel );
                        intent.putExtra("properties",entityitem.properties);
                        intent.putExtra("name",entityitem.label);
                        startActivity(intent);
                    }
                });

            item.findViewById(R.id.remove_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // entityExpandingList.removeItem(item);
//                    Intent intent=new Intent(getApplicationContext(),EntitydetailActivity.class);
//                    intent.putExtra("name", entityitem.label);
//                    intent.putExtra("relations",entityitem.rel);
//                    intent.putExtra("properties",entityitem.properties);
//                    startActivity(intent);
                }
            });
        }
    }

    private void configureSubItem(final ExpandingItem item, final View view, entity entityitem) {
        TextView myview=(TextView)view.findViewById(R.id.sub_title);
        myview.setText(entityitem.info);
        Log.v("info",entityitem.info);
        //myview.setText("腺病毒(adenovirus)是一种没有包膜的直径为70～90 nm的颗粒，由252个壳粒呈廿面体排列构成。每个壳粒的直径为7～9 nm。衣壳里是线状双链DNA分子，约含4.7kb，两端各有长约100 bp的反向重复序列。由于每条DNA链的5'端同相对分子质量为55X103Da的蛋白质分子共价结合，可以出现双链DNA的环状结构。");
        myview.requestLayout();
        ImageView img=(ImageView)view.findViewById(R.id.entityimage);
        Glide.with(EntityresultActivity.this)
                .load(entityitem.img)
                .into(img);
       /* view.findViewById(R.id.remove_sub_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.removeSubItem(view);
            }
        });
        */
    }
    private static class ViewHolder {
        TextView type;
        TextView relenti;
    }
    private class RelationAdapter extends BaseAdapter {
        public ArrayList<relations> Relations;
        public String label;
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
        public ArrayList<pair> Pros;
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
    private void showInsertDialog(entity entityitem) {
        //final EditText text = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.entitydetail_layout, null, false);
        ListView relationsview=contentView.findViewById(R.id.entityrelations);
        ListView propertyviews=contentView.findViewById(R.id.entityproperties);
        RelationAdapter readp=new RelationAdapter();
        readp.Relations=entityitem.rel;
        readp.label=entityitem.label;
        PropertyAdapter ptadp=new PropertyAdapter();
        ptadp.Pros=entityitem.properties;
        relationsview.setAdapter(readp);
        propertyviews.setAdapter(ptadp);
        builder.setView(contentView);
        builder.setTitle("哈哈哈哈哈哈");
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();
    }

    interface OnItemCreated {
        void itemCreated(String title);
    }
}
