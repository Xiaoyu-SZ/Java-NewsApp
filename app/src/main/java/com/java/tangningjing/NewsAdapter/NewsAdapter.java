package com.java.tangningjing.NewsAdapter;

import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.java.tangningjing.R;
import com.java.tangningjing.bean.news;

import java.util.ArrayList;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private ArrayList<news> mDataset;
    private OnItemClickListener   mOnItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView title;
        private CardView cv;
        private TextView time;
        private TextView res;
        public NewsViewHolder(View itemView) {
            super(itemView);
            Log.v("info","holderconstruct");
            title = itemView.findViewById(R.id.title);
            time=itemView.findViewById(R.id.release_time);
            res=itemView.findViewById(R.id.news_res);
            cv =(CardView)itemView;
            Log.v("leaving","leaveholderconstruct");
        }
        public TextView getTitle(){
            return title;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewsAdapter(ArrayList<news> myDataset) {
        mDataset=new ArrayList<>();
        myDataset.addAll(myDataset);
        Log.v("created","adapter");
    }
    public NewsAdapter() {
        mDataset=new ArrayList<>();
    }
    public void SetData(ArrayList<news> newsdata){
        mDataset.clear();
        mDataset.addAll(newsdata);
    }
    public void Add(news News){mDataset.add(News);}
    public void AddData(ArrayList<news> newsdata) {
        mDataset.addAll(newsdata);
    }
    // Create new views (invoked by the layout manager)
    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        // create a new view
        Log.v("creating","view");
        NewsViewHolder holder = new NewsViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.news_result_recycler_item, parent,
                false));
        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(NewsViewHolder holder,final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(mDataset.get(position).cached){
            holder.title.setTextColor(BLUE);
        }
        else{
            holder.title.setTextColor(BLACK);
        }
        holder.title.setText(mDataset.get(position).title);
        holder.time.setText(mDataset.get(position).time);
        holder.res.setText(mDataset.get(position).source);
        if (mOnItemClickListener != null) {
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view, position);
                }
            });
        }
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    public news getNewsbyPos(int position){return mDataset.get(position);}
}

