package com.java.tangningjing;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.easytagdragview.EasyTipDragView;
import com.google.android.material.tabs.TabLayout;
import com.java.tangningjing.ui.ExpertpageFragment;
import com.java.tangningjing.ui.newspage.NewspageFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ExpertActivity extends MainActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    List<Fragment> fragments = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    FragmentPagerAdapter madapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_expert, null, false);
        drawer.addView(contentView, 0);
        tabLayout = (TabLayout) contentView.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) contentView.findViewById(R.id.vp_viewpager);
//        fragments.add(new NewspageFragment("news"));
//        fragments.add(new NewspageFragment("paper"));
//        titles.add("新闻");
//        titles.add("论文");
        fragments.add(new ExpertpageFragment(false));
      //  fragments.add(new ExpertpageFragment(true));

        titles.add("高关注学者");
        titles.add("追忆学者");
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
