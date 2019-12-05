package com.o1.timemanager.ui.backpack;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BackpackViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BackpackViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}