package com.java.tangningjing.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.java.tangningjing.EntityresultActivity;
import com.java.tangningjing.R;
import com.java.tangningjing.TestActivity;
import com.java.tangningjing.Utils;
import com.java.tangningjing.bean.downloadmanager;
import com.java.tangningjing.bean.expert;
import com.java.tangningjing.ui.models.Friend;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yalantis.flipviewpager.adapter.BaseFlipAdapter;
import com.yalantis.flipviewpager.utils.FlipSettings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ExpertpageFragment extends Fragment {
    boolean ispassedaway;
    ArrayList<expert> Experts;
    ExpertAdapter adapter;
    private final Handler messageHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
    public ExpertpageFragment(boolean _ispassedaway){
        this.ispassedaway=_ispassedaway;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.test, container, false);
        //news.deleteAll(news.class);
        super.onCreate(savedInstanceState);
        //inflate your activity layout here!
     //   drawer.addView(contentView, 0);
        //setContentView(R.layout.test);
        Experts=new ArrayList<>();
        new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {//在run()方法实现业务逻辑；
                //...
                ArrayList<expert> temp= downloadmanager.expertdownload(ispassedaway);
                Experts.addAll(temp);
                messageHandler.sendEmptyMessage(1);
                //Log.v("test", "refreshing");
            }
        }.start();
        final ListView experts = (ListView)root.findViewById(R.id.friends);
        FlipSettings settings = new FlipSettings.Builder().defaultPage(1).build();
        adapter=new ExpertAdapter(getActivity(),Experts, settings);
        experts.setAdapter(adapter);
        experts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Friend f = (Friend) friends.getAdapter().getItem(position);
               // Toast.makeText(TestActivity.this, f.getNickname(), Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
    class ExpertAdapter extends BaseFlipAdapter {

        private final int PAGES = 3;
        private int[] IDS_INTEREST = {R.id.interest_1, R.id.interest_2, R.id.interest_3, R.id.interest_4, R.id.interest_5};

        public ExpertAdapter(Context context, List<expert> items, FlipSettings settings) {
            super(context, items, settings);
        }

        @Override
        public View getPage(int position, View convertView, ViewGroup parent, Object expert1, Object expert2) {
            final ExpertHolder holder;

            if (convertView == null) {
                holder = new ExpertHolder();
                convertView = getLayoutInflater().inflate(R.layout.test_merge_page, parent, false);
                holder.leftAvatar = (ImageView) convertView.findViewById(R.id.first);
                holder.rightAvatar = (ImageView) convertView.findViewById(R.id.second);
                holder.infoPage = getLayoutInflater().inflate(R.layout.test_info, parent, false);
                holder.nickName = (TextView) holder.infoPage.findViewById(R.id.nickname);
                for (int id : IDS_INTEREST)
                    holder.interests.add((TextView) holder.infoPage.findViewById(id));

                convertView.setTag(holder);
            } else {
                holder = (ExpertHolder) convertView.getTag();
            }

            switch (position) {
                // Merged page with 2 friends
                case 1:
                    Glide.with(requireActivity())
                            .load(((expert)expert1).avatar)
                            .into(holder.leftAvatar);
                   // holder.leftAvatar.setImageResource(((expert)expert1).avatar);
                    if (expert2 != null)
                        Glide.with(requireActivity())
                                .load(((expert)expert2).avatar)
                                .into(holder.rightAvatar);
                    break;
                default:
                    fillHolder(holder, position == 0 ? (expert) expert1 : (expert) expert2);
                    holder.infoPage.setTag(holder);
                    return holder.infoPage;
            }
            return convertView;
        }

        @Override
        public int getPagesCount() {
            return PAGES;
        }

        private void fillHolder(ExpertHolder holder, expert Expert) {
            if (Expert == null)
                return;

            holder.nickName.setText(Expert.name_zh);
//            Iterator<TextView> iViews = holder.interests.iterator();
//            Iterator<String> iInterests = friend.getInterests().iterator();
//            while (iViews.hasNext() && iInterests.hasNext())
//                iViews.next().setText(iInterests.next());
//            holder.infoPage.setBackgroundColor(getResources().getColor(friend.getBackground()));
//            holder.nickName.setText(friend.getNickname());
        }

        class ExpertHolder {
            ImageView leftAvatar;
            ImageView rightAvatar;
            View infoPage;
            List<TextView> interests = new ArrayList<>();
            TextView nickName;
        }
    }
}
