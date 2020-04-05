package com.ekochkov.intervallearning.utils

import java.text.SimpleDateFormat
import java.util.*

class DateHelper {

    companion object {

        const val FORMAT_yyyyMMdd = "yyyyMMdd"
        const val FORMAT_dd_MM_YYYY = "dd.MM.yyyy"
        const val FORMAT_dd_MM_YYYY_HH_mm_z = "dd:MM:yyyy, HH:mm (z)"
        const val FORMAT_dd_MM_YYYY_HH_mm_ss_z = "dd:MM:yyyy, HH:mm:ss (z)"

        fun getCurrentLongTime(): Long {
            var time = Calendar.getInstance().time.time
            return time
        }

        fun convertFromLongToStringFormat(format: String, time: Long): String {
            var formatString = SimpleDateFormat(format).format(time).toString()
            return formatString
        }
    }
}