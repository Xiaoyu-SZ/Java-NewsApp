package com.example.myapplication.NewsAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private ArrayList<String> mDataset;
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
        public NewsViewHolder(View itemView) {
            super(itemView);
            Log.v("info","holderconstruct");
            title = itemView.findViewById(R.id.news_title);
            cv =(CardView)itemView;
            Log.v("leaving","leaveholderconstruct");
        }
        public TextView getTitle(){
            return title;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewsAdapter(ArrayList<String> myDataset) {
        mDataset = myDataset;
        Log.v("created","adapter");
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        Log.v("creating","view");
        NewsViewHolder holder = new NewsViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.news_result_list_item, parent,
                false));
        Log.v("created","view");
        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(NewsViewHolder holder,final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title.setText(mDataset.get(position));
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

}

