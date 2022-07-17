package com.myweather.android.ui.addcity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddCityViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "this is addCityActivity"
    }
    val text:LiveData<String> = _text
}