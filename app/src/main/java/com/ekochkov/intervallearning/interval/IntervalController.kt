package com.ekochkov.intervallearning.interval

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LiveData
import com.ekochkov.intervallearning.utils.SimpleCallback
import com.ekochkov.intervallearning.utils.SingletonHolder
import androidx.lifecycle.MutableLiveData



class IntervalController private constructor(activity: Activity) : IntervalCallback<Int> {

    companion object : SingletonHolder<IntervalController, Activity>(::IntervalController)
    lateinit var activity: Activity

    override fun getBound(): Boolean {
        return appStatus.value?: false
    }

    override fun onResult(item: Int) {
        callback.onResult("пришло время повторить слова: ${item}")
    }

    val LOG_TAG = IntervalController::class.java.getName() + " BMTH "

    lateinit private var service: IntervalService
    lateinit var callback: SimpleCallback<String>
    private var appStatus = MutableLiveData<Boolean>()

    var intervalServiceConn = object: ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(LOG_TAG, "onServiceDisconnected()")
            setAppStatus(false)
        }

        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            Log.d(LOG_TAG, "onServiceConnected()")
            var intervalBinder = binder as IntervalService.IntervalBinder
            service = intervalBinder.getService()
            setAppStatus(true)
            service.getActivityCallback(this@IntervalController)
        }
    }

    init {
        this.activity=activity
    }

    fun startService(callback: SimpleCallback<String>) {
        this.callback=callback
        var intent = Intent(activity, IntervalService::class.java)
        activity.startService(intent)
        activity.bindService(intent, intervalServiceConn, Context.BIND_AUTO_CREATE)
    }

    fun getAppStatus(): LiveData<Boolean> {
        return appStatus
    }

    fun setAppStatus(value: Boolean) {
        this.appStatus.value=value

    }
}