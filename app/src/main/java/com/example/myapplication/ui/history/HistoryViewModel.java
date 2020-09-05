package com.example.myapplication.ui.history;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class HistoryViewModel extends ViewModel{
    private ArrayList<String> newsdata;

    public HistoryViewModel() {
        newsdata=new ArrayList<>();
        for(int i=0;i<10;i++){
            newsdata.add("新闻");
        }
    }

    public ArrayList<String> getNewsdata() {
        return newsdata;
    }
}
