package com.java.tangningjing.ui.newspage;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewspageViewModel extends ViewModel {
    private MutableLiveData<String> newstitle;
    private MutableLiveData<String> newscontent;
    public NewspageViewModel(){
        newstitle=new MutableLiveData<>();
        newscontent=new MutableLiveData<>();
    }
}
