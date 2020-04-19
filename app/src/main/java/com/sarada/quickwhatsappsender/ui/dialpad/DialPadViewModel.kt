package com.sarada.quickwhatsappsender.ui.dialpad

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DialPadViewModel : ViewModel() {

    private val _mobileNumber = MutableLiveData<Long>()
    private val _ctryCode = MutableLiveData<Int>().apply { value = 91 }

    val mobileNumber: LiveData<Long> = _mobileNumber
    val ctryCode: LiveData<Int> = _ctryCode

    fun setCtryCode(ctryCode: Int) {
        this._ctryCode.postValue(ctryCode)
    }

    fun setMobileNumber(mobileNumber: String) {
        _mobileNumber.postValue(mobileNumber.toLong())
    }

    fun getUri() = "https://wa.me/" + ctryCode.value + mobileNumber.value

}