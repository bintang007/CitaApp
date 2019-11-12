package com.cita.citaapp.ui.nutrient;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NutrientViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NutrientViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Ini Fragment Kandungan Gizi");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
