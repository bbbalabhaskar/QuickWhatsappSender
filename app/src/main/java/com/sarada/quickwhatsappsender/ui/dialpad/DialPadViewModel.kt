package com.sarada.quickwhatsappsender.ui.dialpad

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DialPadViewModel : ViewModel() {

    private val _mobileNumber = MutableLiveData<String>()

    val mobileNumber: LiveData<String> = _mobileNumber

    fun setMobileNumber(mobileNumber: String) {
        _mobileNumber.postValue(mobileNumber)
    }

    fun getUri() = "https://wa.me/" + mobileNumber.value

}