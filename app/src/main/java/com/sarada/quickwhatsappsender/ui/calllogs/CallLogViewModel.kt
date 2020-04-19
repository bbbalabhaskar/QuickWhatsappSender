package com.sarada.quickwhatsappsender.ui.calllogs

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
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
            val contactsCursor = context.contentResolver.query(contentUri, null, null, null, null)
            contactsCursor.use { cursor ->
                val nameUri =
                    cursor?.getColumnIndex(android.provider.CallLog.Calls.CACHED_LOOKUP_URI)
                val number = cursor?.getColumnIndex(android.provider.CallLog.Calls.NUMBER)
                val duration = cursor?.getColumnIndex(android.provider.CallLog.Calls.DURATION)
                val date = cursor?.getColumnIndex(android.provider.CallLog.Calls.DATE)
                val type = cursor?.getColumnIndex(android.provider.CallLog.Calls.TYPE)

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
                        val callerNameUri = cursor.getString(nameUri!!)
                        val callDate = cursor.getString(date!!)
                        val callDateTime = Date(callDate.toLong()).toString()
                        val callDuration = cursor.getString(duration!!)

                        callLogs.value?.add(
                            CallLog(
                                getCallerName(context, callerNameUri, phoneNumber),
                                phoneNumber,
                                callDuration,
                                callType,
                                callDateTime
                            )
                        )
                    } while (cursor.moveToNext())
                }
            }

        }
    }

    private fun getCallerName(context: Context, callerNameUri: String?, phoneNumber: String): String {

        return if (callerNameUri != null) {
            var name = ""
            val contactsCursor =
                context.contentResolver.query(Uri.parse(callerNameUri), null, null, null, null)

            contactsCursor?.use {
                if (contactsCursor.count > 0) {
                    while (contactsCursor.moveToNext()) {
                        name =
                            contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    }
                }
            }
            return name
        } else {
            phoneNumber
        }
    }

}