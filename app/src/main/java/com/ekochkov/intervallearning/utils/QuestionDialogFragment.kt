package com.ekochkov.intervallearning.utils

import android.content.DialogInterface
import android.R
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class QuestionDialogFragment(question: String, positive: String, negative: String, listener: QuestionDialogListener): DialogFragment() {

    interface QuestionDialogListener {
        fun onPositiveClick()
        fun onNegativeClick()
    }

    var question: String
    var positive: String
    var negative: String
    var listener: QuestionDialogListener

    init {
        this.question=question
        this.positive=positive
        this.negative=negative
        this.listener=listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(getContext()!!)
        builder.setMessage(question)
            .setPositiveButton(positive, DialogInterface.OnClickListener { dialog, id ->
                listener.onPositiveClick()
            })
            .setNegativeButton(negative, DialogInterface.OnClickListener { dialog, id ->
                listener.onNegativeClick()
            })
        // Create the AlertDialog object and return it
        return builder.create()
    }
}
