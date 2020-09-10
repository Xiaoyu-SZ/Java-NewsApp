package com.java.tangningjing;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.easytagdragview.EasyTipDragView;
import com.easytagdragview.bean.Tip;
import com.easytagdragview.widget.TipItemView;
import com.java.tangningjing.ui.newspage.NewspageFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
public class HomenewActivity extends MainActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    Button btn;
    EasyTipDragView tipview;
    List<Fragment> fragments = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    ImageView select_img;
    FragmentPagerAdapter madapter;
    int CURRENT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.test_viewpager_layout, null, false);
        drawer.addView(contentView, 0);
//        btn=contentView.findViewById(R.id.btn);
//        btn.setOnClickListener(new View.OnClickListener() {
//                                   @Override
//                                   public void onClick(View v) {
//                                       if (window != null) {
//                                           if (window.isShowing()) {
//                                               window.dismiss();
//                                           } else {
//                                               showPopwindow(btn);
//                                           }
//                                       } else {
//                                           showPopwindow(btn);
//                                       }
//                                       Toast.makeText(HomenewActivity.this, "点击", Toast.LENGTH_LONG).show();
//                                   }
//                               });
        tabLayout = (TabLayout) contentView.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) contentView.findViewById(R.id.vp_viewpager);
        fragments.add(new NewspageFragment("news"));
        fragments.add(new NewspageFragment("paper"));
        titles.add("新闻");
        titles.add("论文");
        CURRENT=2;
        madapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NotNull
            @Override
            public Fragment getItem(int position) {
                 return fragments.get(position);
            }

            @Override
            public int getCount() {
                switch (CURRENT){
                    case 2:
                        return 2;
                    default:
                        return 1;
                }
            }

            @Override
            public void destroyItem(@NotNull ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (CURRENT){
                    case 2:
                        return titles.get(position);
                    case 1:
                        return titles.get(0);
                    case 0:
                        return titles.get(1);
                }
                return titles.get(position);
            }

        };
        viewPager.setAdapter(madapter);
        tabLayout.setupWithViewPager(viewPager);
        select_img=contentView.findViewById(R.id.icon_category);
        select_img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(getApplicationContext(),TActivity.class); //可能会有错，要注意
                intent.putExtra("currentchannel",CURRENT);
                startActivityForResult(intent,1);  //result会回到fragment
            }

        });

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==1){
            if(resultCode==RESULT_OK) {
                CURRENT=data.getIntExtra("currentchannel",2);
                switch (CURRENT){
                    case 2:
                        ((NewspageFragment)fragments.get(0)).setManager("news");
                        ((NewspageFragment)fragments.get(1)).setManager("paper");
                        break;
                    case 1:
                        ((NewspageFragment)fragments.get(0)).setManager("news");
                        break;
                    case 0:
                        ((NewspageFragment)fragments.get(0)).setManager("paper");
                        break;
                    default:
                        break;
                }
                madapter.notifyDataSetChanged();
                ((NewspageFragment)fragments.get(0)).Refresh();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
