package com.ekochkov.intervallearning.mainMenu.addCategory

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
import android.view.ViewGroup


class AddCategoryDialogFragment(listener: AddWordDialogListener) : DialogFragment(), OnWordItemClickListener<Word>, AddCategoryDialogContract.View {

    override fun returnToCategory(wordBundle: Bundle) {
        listener.onDialogPositiveClick(wordBundle)
        dismiss()
    }

    override fun getEditTextData(): Bundle {
        var bundle = Bundle()
        bundle.putString("categoryName", categoryName.text.toString())
        return  bundle
    }

    override fun fillWordData(bundle: Bundle?) {
        var category = bundle?.getString("categoryName")
        categoryName.setText(category)
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

    val LOG_TAG = AddCategoryDialogFragment::class.java.name + " BMTH "

	lateinit var categoryName: EditText
    lateinit var addButton: Button
    lateinit var cancelButton: Button
    lateinit var dialogPresenter: AddCategoryDialogPresenter

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
        val rootView = inflater.inflate(R.layout.fragment_dialog_add_category, container, false)
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
        addButton = rootView.findViewById(R.id.add_button)
        addButton.setOnClickListener {
            dialogPresenter.onAddClicked()
            //listener.onDialogPositiveClick(word)
            //dismiss()
        }
        cancelButton = rootView.findViewById(R.id.cancel_button)
        cancelButton.setOnClickListener {
            listener.onDialogNegativeClick()
            dismiss()
        }

        var roomController = RoomController(context!!)
        dialogPresenter = AddCategoryDialogPresenter(roomController)
        dialogPresenter.attachView(this)
        dialogPresenter.viewIsReady()
    }
	
	override fun getBundle(): Bundle? {
        Log.d(LOG_TAG, "getBundle")
        val bundle = arguments       
        return bundle
    }
}