package com.sarada.quickwhatsappsender.utils

import android.os.Build
import android.text.format.DateFormat
import androidx.annotation.RequiresApi
import java.time.format.DateTimeFormatter
import java.util.*

class TimeAgo {

    companion object {
        private const val SECOND_MILLIS = 1000
        private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
        private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
        private const val DAY_MILLIS = 60 * HOUR_MILLIS

        @RequiresApi(Build.VERSION_CODES.N)
        fun getTimeAgo(inputMillis: Long): String? {

            var time = inputMillis

            if (time < 1000000000000L) {
                time *= 1000
            }

            val now = System.currentTimeMillis()

            if (time > now || time <= 0) {
                return null
            }

            val diff = now - time

            return when {
                diff < MINUTE_MILLIS -> "just now"
                diff < 2 * MINUTE_MILLIS -> "a minute ago"
                diff < 50 * MINUTE_MILLIS -> String.format("%d %s", (diff / MINUTE_MILLIS), "minutes ago")
                diff < 90 * MINUTE_MILLIS -> "an hour ago"
                diff < 24 * HOUR_MILLIS -> String.format("%d %s", (diff / HOUR_MILLIS), "hours ago")
                diff < 48 * HOUR_MILLIS -> "yesterday"
                else -> {
                    val date = Date(inputMillis)
                    return android.icu.text.DateFormat.getDateInstance().format(date)
                }
            }
        }
    }

}