package com.java.tangningjing;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.easytagdragview.EasyTipDragView;
import com.google.android.material.tabs.TabLayout;
import com.java.tangningjing.NewsAdapter.NewsAdapter;
import com.java.tangningjing.bean.news;
import com.java.tangningjing.bean.newsmanager;
import com.java.tangningjing.ui.history.HistoryViewModel;
import com.java.tangningjing.ui.home.HomeViewModel;
import com.java.tangningjing.ui.newspage.NewspageFragment;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.orm.SugarContext;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class HistoryActivity extends MainActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    List<Fragment> fragments = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    FragmentPagerAdapter madapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_page_layout, null, false);
        drawer.addView(contentView, 0);
        tabLayout = (TabLayout) contentView.findViewById(R.id.tabs);
        viewPager = (ViewPager) contentView.findViewById(R.id.vpcontent);
        fragments.add(new NewspageFragment("news",true));
        fragments.add(new NewspageFragment("paper",true));
        titles.add("新闻");
        titles.add("论文");
        madapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NotNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public void destroyItem(@NotNull ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }

        };
        viewPager.setAdapter(madapter);
        tabLayout.setupWithViewPager(viewPager);
    }


}

