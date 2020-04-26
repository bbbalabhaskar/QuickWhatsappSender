package com.sarada.quickwhatsappsender.ui.calllogs

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sarada.quickwhatsappsender.models.CallLog
import java.util.*
import kotlin.collections.ArrayList

class CallLogViewModel : ViewModel() {


    val callLogs: MutableLiveData<MutableList<CallLog>> = MutableLiveData()

    init {
        callLogs.value = ArrayList()
    }

    fun updateCallData(context: Context) {

        val contentUri = android.provider.CallLog.Calls.CONTENT_URI;

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val contactsCursor =
                context.contentResolver.query(contentUri, null, null, null, "date DESC")
            contactsCursor.use { cursor ->

                val nameUri =
                    cursor?.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME)
                val number = cursor?.getColumnIndex(android.provider.CallLog.Calls.NUMBER)
                val duration = cursor?.getColumnIndex(android.provider.CallLog.Calls.DURATION)
                val date = cursor?.getColumnIndex(android.provider.CallLog.Calls.DATE)
                val type = cursor?.getColumnIndex(android.provider.CallLog.Calls.TYPE)
                var index = 0;
                if (cursor?.moveToFirst()!!) {
                    do {
                        val callType = when (type?.let { cursor.getInt(it) }) {
                            android.provider.CallLog.Calls.INCOMING_TYPE -> "Incoming"
                            android.provider.CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                            android.provider.CallLog.Calls.MISSED_TYPE -> "Missed"
                            android.provider.CallLog.Calls.REJECTED_TYPE -> "Rejected"
                            else -> "Not Defined"
                        }
                        val phoneNumber = cursor.getString(number!!)
                        val callerName = nameUri?.let { cursor.getString(it) }
                        val callDate = cursor.getString(date!!)
                        val callDateTime = Date(callDate.toLong()).toString()
                        val callDuration = cursor.getString(duration!!)

                        if (!callLogs.value?.isEmpty()!!) {
                            val log = callLogs.value?.last()
                            if (log != null && log.number == phoneNumber) {
                                log.count++
                                index--
                            } else {
                                updateCallLogList(
                                    callerName,
                                    phoneNumber,
                                    callDuration,
                                    callType,
                                    callDateTime
                                )
                            }
                        } else {
                            updateCallLogList(
                                callerName,
                                phoneNumber,
                                callDuration,
                                callType,
                                callDateTime
                            )
                        }

                    } while (cursor.moveToNext() && index++ <= 100) //limiting total contacts to 100
                    //TODO implement lazyloading.
                }
            }

        }
    }

    private fun updateCallLogList(
        callerName: String?,
        phoneNumber: String,
        callDuration: String,
        callType: String,
        callDateTime: String
    ) {
        callLogs.value?.add(
            CallLog(
                callerName ?: phoneNumber,
                phoneNumber,
                callDuration,
                callType,
                callDateTime,
                1
            )
        )
    }


}