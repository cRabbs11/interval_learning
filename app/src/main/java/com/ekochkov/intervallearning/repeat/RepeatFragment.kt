package com.ekochkov.intervallearning.repeat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import com.ekochkov.intervallearning.R
import com.ekochkov.intervallearning.room.RoomController

class RepeatFragment : Fragment(), RepeatContract.View {
    override fun closeFragment() {
        Log.d(LOG_TAG, "closeFragment")
        activity?.onBackPressed()
    }

    override fun showButtons() {
        positiveButton.visibility=VISIBLE
        negativeButton.visibility=VISIBLE
    }
    override fun hideButtons() {
        positiveButton.visibility=GONE
        negativeButton.visibility=GONE
    }

    override fun showWord(word: String?) {
        wordText.setText(word)
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, LENGTH_SHORT).show()
    }

    override fun showFinish(message: String) {
        Toast.makeText(context, message, LENGTH_SHORT).show()
       // wordLayout.visibility= GONE
    }


    val LOG_TAG = RepeatFragment::class.java.name + " BMTH "

    lateinit var positiveButton: ImageButton
    lateinit var negativeButton: ImageButton
    lateinit var wordLayout: RelativeLayout
    lateinit var wordText: TextView

    lateinit var presenter: RepeatPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_repeat, container, false)
        initView(rootView)
        return rootView
    }

    fun initView(rootView: View ) {
        positiveButton = rootView.findViewById(R.id.positive_button)
        negativeButton = rootView.findViewById(R.id.negative_button)

        wordLayout = rootView.findViewById(R.id.word_layout)
        wordLayout.setOnClickListener {
            presenter.onWordClicked()
        }
        wordText = rootView.findViewById(R.id.word_text)

        positiveButton.setOnClickListener {
            presenter.onPositiveButtonClicked()
        }

        negativeButton.setOnClickListener {
            presenter.onNegativeButtonClicked()
        }

        var roomController = RoomController(context!!)
        presenter = RepeatPresenter(roomController)
        presenter.attachView(this)
        presenter.viewIsReady()
    }

}