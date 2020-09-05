package com.example.myapplication.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private ArrayList<String> newsdata;
    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("主页在这");
        newsdata=new ArrayList<>();
        newsdata.add("新闻1");
        newsdata.add("新闻2");
        newsdata.add("新闻3");
        newsdata.add("新闻4");
        newsdata.add("新闻5");
        newsdata.add("新闻6");
        newsdata.add("新闻7");
        newsdata.add("新闻8");
        newsdata.add("新闻9");
        newsdata.add("新闻10");
    }
    public ArrayList<String> getNewsdata(){return newsdata;}
    public LiveData<String> getText() {
        return mText;
    }
}