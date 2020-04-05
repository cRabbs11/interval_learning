package com.ekochkov.intervallearning

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment


class StartFragment : Fragment() {

    val LOG_TAG = StartFragment::class.java.name + " BMTH "

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_word, container, false)
        return rootView
    }
}