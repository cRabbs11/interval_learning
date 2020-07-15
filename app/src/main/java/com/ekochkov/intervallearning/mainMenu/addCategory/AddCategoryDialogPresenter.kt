package com.ekochkov.intervallearning.mainMenu.addCategory

import android.os.Bundle
import android.util.Log
import com.ekochkov.intervallearning.mvp.PresenterBase
import com.ekochkov.intervallearning.room.RoomController
import com.ekochkov.intervallearning.room.Word


public class AddCategoryDialogPresenter(roomController: RoomController):
        PresenterBase<AddCategoryDialogContract.View>(), AddCategoryDialogContract.Presenter {

	override fun onAddClicked() {
		var bundle = getView()?.getEditTextData()
		var categoryName = bundle?.getString("categoryName")

		var wordBundle = Bundle()
		wordBundle.putString("categoryName", categoryName)
		getView()?.returnToCategory(wordBundle)
	}

	val LOG_TAG = AddCategoryDialogPresenter::class.java.name + " BMTH "

    override fun viewIsReady() {
		getWordData(getView()?.getBundle())
    }

	private fun getWordData(bundle: Bundle?) {
		getView()?.fillWordData(bundle)

	}

    private val roomController: RoomController

    init {
        this.roomController=roomController
    }

}