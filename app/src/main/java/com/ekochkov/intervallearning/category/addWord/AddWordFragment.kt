package com.ekochkov.intervallearning.category.addWord

import android.os.Bundle
import android.util.Log
import android.view.*
import com.ekochkov.intervallearning.room.Word
import android.widget.EditText

import androidx.fragment.app.DialogFragment
import com.ekochkov.intervallearning.utils.OnWordItemClickListener
import android.view.LayoutInflater
import android.widget.Button
import com.ekochkov.intervallearning.R
import com.ekochkov.intervallearning.room.RoomController
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup

import android.graphics.Color
import android.widget.TextView


class AddWordFragment(listener: AddWordDialogListener) : DialogFragment(), OnWordItemClickListener<Word>, AddWordContract.View {

    override fun returnToCategory(wordBundle: Bundle) {
        listener.onDialogPositiveClick(wordBundle)
        dismiss()
    }

    override fun getEditTextData(): Bundle {
        var bundle = Bundle()
        bundle.putString("categoryName", categoryName.text.toString())
        bundle.putString("wordOriginal", wordOriginal.text.toString())
        bundle.putString("wordTranslate", wordTranslate.text.toString())
        return  bundle
    }

    override fun fillWordData(bundle: Bundle?) {
        var category = bundle?.getString("categoryName")
        var original = bundle?.getString("wordOriginal")
        var translate = bundle?.getString("wordTranslate")
        categoryName.text = category
        wordOriginal.setText(original)
        wordTranslate.setText(translate)
    }

    interface AddWordDialogListener {
        fun onDialogPositiveClick(bundle: Bundle)
        fun onDialogNegativeClick()
    }

    // Use this instance of the interface to deliver action events
    lateinit var listener: AddWordDialogListener

    override fun onDeleteClick(item: Word) {

    }

    init {
        this.listener=listener
    }

    override fun onChangeClick(item: Word) {

    }

    val LOG_TAG = AddWordFragment::class.java.name + " BMTH "

	lateinit var categoryName: TextView
	lateinit var wordOriginal: EditText
	lateinit var wordTranslate: EditText
    lateinit var addButton: Button
    lateinit var cancelButton: Button
    lateinit var presenter: AddWordPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LOG_TAG, "onViewCreated")
        //getDialog()!!.setTitle("dialog title")
        // Show soft keyboard automatically and request focus to field
        getDialog()!!.getWindow()!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(LOG_TAG, "onCreateView")
        val rootView = inflater.inflate(R.layout.fragment_dialog_add_word, container, false)
        initView(rootView)
        return rootView
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            //цвет фона диалога
            //dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun initView(rootView: View) {
        Log.d(LOG_TAG, "initView")
        categoryName = rootView.findViewById(R.id.category_name)
        wordTranslate = rootView.findViewById(R.id.word_translate)
        wordOriginal = rootView.findViewById(R.id.word_original)
        addButton = rootView.findViewById(R.id.add_button)
        addButton.setOnClickListener {
            presenter.onAddWordClicked()
            //listener.onDialogPositiveClick(word)
            //dismiss()
        }
        cancelButton = rootView.findViewById(R.id.cancel_button)
        cancelButton.setOnClickListener {
            listener.onDialogNegativeClick()
            dismiss()
        }

        var roomController = RoomController(context!!)
        presenter = AddWordPresenter(roomController)
        presenter.attachView(this)
        presenter.viewIsReady()
    }
	
	override fun getBundle(): Bundle? {
        Log.d(LOG_TAG, "getBundle")
        val bundle = arguments       
        return bundle
    }
}