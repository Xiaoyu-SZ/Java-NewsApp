package com.example.myapplication.ui.collection;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
public class CollectionViewModel extends ViewModel{
    private MutableLiveData<String> mText;

    public CollectionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("这是收藏夹");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
