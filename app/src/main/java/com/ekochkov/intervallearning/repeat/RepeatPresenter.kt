package com.ekochkov.intervallearning.repeat

import android.util.Log
import com.ekochkov.intervallearning.interval.IntervalLearning
import com.ekochkov.intervallearning.mvp.PresenterBase
import com.ekochkov.intervallearning.room.RoomController
import com.ekochkov.intervallearning.room.Word
import com.ekochkov.intervallearning.utils.DateHelper
import com.ekochkov.intervallearning.utils.SimpleCallback
import java.util.*
import kotlin.collections.ArrayList

public class RepeatPresenter(roomController: RoomController):
    PresenterBase<RepeatContract.View>(), RepeatContract.Presenter {

    override fun onWordClicked() {
        Log.d(LOG_TAG, "onWordClicked")
        getView()?.showWord(currentWord.translate)
        getView()?.showButtons()
    }

    override fun onPositiveButtonClicked() {
        Log.d(LOG_TAG, "onPositiveButtonClicked()")
        processingWord(true, object: SimpleCallback<Int> {
            override fun onResult(item: Int) {
                if (wordList.size!=0) {
                    currentWord = wordList.get(0)
                    getView()?.showWord(currentWord.original)
                } else {
                    getView()?.showFinish()
                }
                getView()?.hideButtons()
            }
        })
    }

    override fun onNegativeButtonClicked() {
        Log.d(LOG_TAG, "onNegativeButtonClicked()")
        processingWord(false, object: SimpleCallback<Int> {
            override fun onResult(item: Int) {
                if (wordList.size!=0) {
                    currentWord = wordList.get(0)
                    getView()?.showWord(currentWord.original)
                } else {
                    getView()?.showFinish()
                }
                getView()?.hideButtons()
            }
        })
    }

    private fun processingWord(answer: Boolean, callback: SimpleCallback<Int>) {
        Log.d(LOG_TAG, "processingWord()")
        run loop@{
            wordList.forEach {
                if (it.id==currentWord.id) {
                    wordList.remove(it)
                    return@loop
                }
            }
        }
        var intervalLearning = IntervalLearning()
        intervalLearning.rebuildWord(currentWord, answer)
        roomController.updateWord(currentWord, object: RoomController.RoomAsyncCallback<Int> {
            override fun onSuccess(item: Int) {
                Log.d(LOG_TAG, "updateWord onSuccess: ${item}")
                callback.onResult(item)
            }

            override fun onComplete() {

            }
        })
    }

    override fun viewIsReady() {
        Log.d(LOG_TAG, "viewIsReady()")
        getView()?.hideButtons()
        getRepeatWordList(object: SimpleCallback<Int> {
            override fun onResult(item: Int) {
                if (item!=0) {
                    getView()?.showWord(currentWord.original)
                } else {
                    getView()?.showToast("слов для проверки не найдено")
                }
            }
        })
    }

    val LOG_TAG = RepeatPresenter::class.java.name + " BMTH "
    private val roomController: RoomController
    private var wordList = arrayListOf<Word>()
    private lateinit var currentWord: Word

    init {
        this.roomController=roomController
    }

    private fun getRepeatWordList(callback: SimpleCallback<Int>) {
        roomController.getRepeatWords(object: RoomController.RoomAsyncCallback<ArrayList<Word>> {
            override fun onSuccess(item: ArrayList<Word>) {
                Log.d(LOG_TAG, "RepeatWordList.size: ${item.size}")
                item.forEach {
                    Log.d(LOG_TAG, "word: ${it.original}, time: ${DateHelper.convertFromLongToStringFormat(DateHelper.FORMAT_dd_MM_YYYY, it.repeat_time!!.toLong())}")
                }
                wordList = item
                currentWord = wordList.get(0)
                callback.onResult(wordList.size)
            }

            override fun onComplete() {

            }
        })
    }

}