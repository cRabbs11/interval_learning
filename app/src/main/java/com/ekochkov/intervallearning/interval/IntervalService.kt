package com.ekochkov.intervallearning.interval

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.ekochkov.intervallearning.room.Word
import com.ekochkov.intervallearning.utils.Notification
import com.ekochkov.intervallearning.utils.SimpleCallback
import java.util.concurrent.TimeUnit
import android.os.Binder
import com.ekochkov.intervallearning.room.RoomController

class IntervalService: Service() {

    val LOG_TAG = IntervalService::class.java.name + " BMTH "

    var mBinder: IntervalBinder? = IntervalBinder()

    lateinit var activityCallback: IntervalCallback<Int>

    inner class IntervalBinder : Binder() {
        fun getService(): IntervalService {
            return this@IntervalService
        }
    }

    fun getActivityCallback(callback: IntervalCallback<Int>) {
        Log.d(LOG_TAG, "getActivityCallback()")
        activityCallback=callback
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "onCreate()")
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(LOG_TAG, "onBind()")
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        someTask()
        return super.onStartCommand(intent, flags, startId)
    }

    fun someTask() {
        Thread(Runnable {
            while (true) {
                try {
                    checkWords(applicationContext)
                    //повтор каждые 2 часа
                    TimeUnit.HOURS.sleep(6)
                    //TimeUnit.MINUTES.sleep(2)
                    //TimeUnit.SECONDS.sleep(5)
                    Log.d(LOG_TAG, "TimeUnit.HOURS.sleep(6)")
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }).start()
    }

    private fun checkWords(context: Context) {
        var roomController = RoomController(context)
        roomController.getRepeatWords(object: RoomController.RoomAsyncCallback<ArrayList<Word>> {
            override fun onSuccess(item: ArrayList<Word>) {
                if (item.size!=0) {
                    if (activityCallback!=null && activityCallback.getBound()) {
                        Log.d(LOG_TAG, "activityIsRunning true")
                        activityCallback.onResult(item.size)
                    } else {
                        Log.d(LOG_TAG, "activityIsRunning false")
                        var notification = Notification()
                        notification.showNotification(context, item.size) //передавать количество слов, которые надо повторить
                    }
                }
            }

        })
    }

}