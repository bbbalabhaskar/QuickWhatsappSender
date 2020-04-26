package com.sarada.quickwhatsappsender.models

data class CallLog(
    val name: String,
    val number: String,
    val duration: String,
    val type: String,
    val dayTime: String,
    var count: Int
)