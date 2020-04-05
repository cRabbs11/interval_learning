package com.ekochkov.intervallearning.utils

import android.content.Context.NOTIFICATION_SERVICE
import android.app.NotificationManager
import android.content.Context

import androidx.core.app.NotificationCompat
import com.ekochkov.intervallearning.R
import android.app.PendingIntent
import com.ekochkov.intervallearning.MainActivity
import android.content.Intent
import android.os.Bundle


class Notification() {

    companion object {
        const val OPEN_REPEAT_FRAGMENT_CODE = 21
    }
    fun showNotification(context: Context, value: Int) {
        // Create PendingIntent
        val resultIntent = Intent(context, MainActivity::class.java)

        resultIntent.putExtra(context.resources.getResourceName(R.string.start_activity_mode), OPEN_REPEAT_FRAGMENT_CODE)

        val resultPendingIntent = PendingIntent.getActivity(
            context, OPEN_REPEAT_FRAGMENT_CODE, resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_note_black_24dp)
            .setContentTitle("Проверка:")
            .setContentText("${value} доступно для повторения")
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)

        val notification = builder.build()

        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        notificationManager!!.notify(1, notification)
    }

}