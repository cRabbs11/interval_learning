package com.ekochkov.intervallearning.category.addWord

import android.os.Bundle
import android.util.Log
import com.ekochkov.intervallearning.interval.IntervalLearning
import com.ekochkov.intervallearning.mvp.PresenterBase
import com.ekochkov.intervallearning.room.RoomController
import com.ekochkov.intervallearning.room.Word
import com.ekochkov.intervallearning.utils.DateHelper
import com.ekochkov.intervallearning.utils.SimpleCallback


public class AddWordPresenter(roomController: RoomController):
        PresenterBase<AddWordContract.View>(), AddWordContract.Presenter {

	override fun onAddWordClicked() {
		var bundle = getView()?.getEditTextData()
		var categoryName = bundle?.getString("categoryName")
		var wordOriginal = bundle?.getString("wordOriginal")
		var wordTranslate = bundle?.getString("wordTranslate")

		var wordBundle = Bundle()
		wordBundle.putString("categoryName", categoryName)
		wordBundle.putString("wordOriginal", wordOriginal)
		wordBundle.putString("wordTranslate",wordTranslate)
		getView()?.returnToCategory(wordBundle)
	}

	val LOG_TAG = AddWordPresenter::class.java.name + " BMTH "

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