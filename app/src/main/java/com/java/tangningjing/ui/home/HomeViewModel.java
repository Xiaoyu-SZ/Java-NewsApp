package com.java.tangningjing.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.java.tangningjing.bean.news;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private ArrayList<news> newsdata;
    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("主页在这");
        newsdata=new ArrayList<>();
        /*
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
        */
    }
    public void setList(ArrayList<news> newsset){
        newsdata=newsset;
    }
    public ArrayList<news> getNewsdata(){return newsdata;}
    public int getNewsSize() {
        return newsdata.size();
    }
}