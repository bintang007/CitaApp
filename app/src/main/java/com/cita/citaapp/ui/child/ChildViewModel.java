package com.cita.citaapp.ui.child;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChildViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ChildViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Ini Fragment Anak");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
