package com.ekochkov.intervallearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.Navigation

import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

import com.ekochkov.intervallearning.utils.SimpleCallback

import com.ekochkov.intervallearning.interval.IntervalController



class MainActivity : AppCompatActivity(), SimpleCallback<Int> {

    override fun onResult(item: Int) {
        Log.d(LOG_TAG, "onResult")
        //notificationLayout.visibility=VISIBLE
        //notificationInfo.text = "пришло время повторить слова: ${item}"
    }

    val LOG_TAG = MainActivity::class.java.getName() + " BMTH "
	lateinit var navController: NavController
    lateinit var intervalController: IntervalController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
		navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun onStart() {
        Log.d(LOG_TAG, "onStart()")
        super.onStart()
        intervalController = IntervalController.Companion.getInstance(this)
        intervalController.startService(object: SimpleCallback<String> {
            override fun onResult(item: String) {
                Log.d(LOG_TAG, "intervalController onResult: ${item}")
                intervalController.setAppStatus(true)
            }
        })
        intervalController.setAppStatus(true)

    }

    override fun onStop() {
        Log.d(LOG_TAG, "onStop()")
        intervalController.setAppStatus(false)
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "onDestroy()")
        super.onDestroy()
    }

    override fun onPause() {
        Log.d(LOG_TAG, "onPause()")
        super.onPause()
    }

}
