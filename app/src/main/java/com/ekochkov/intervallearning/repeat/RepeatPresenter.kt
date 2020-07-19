package com.ekochkov.intervallearning.repeat

import android.util.Log
import com.ekochkov.intervallearning.interval.IntervalLearning
import com.ekochkov.intervallearning.mvp.PresenterBase
import com.ekochkov.intervallearning.room.RoomController
import com.ekochkov.intervallearning.room.Word
import com.ekochkov.intervallearning.utils.DateHelper
import com.ekochkov.intervallearning.utils.SimpleCallback
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

public class RepeatPresenter(roomController: RoomController):
    PresenterBase<RepeatContract.View>(), RepeatContract.Presenter {

    override fun onWordClicked(text: String) {
        Log.d(LOG_TAG, "onWordClicked")
        if (text.equals(currentWord.original)) {
            getView()?.showWord(currentWord.translate)
        } else {
            getView()?.showWord(currentWord.original)
        }
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
                    finish("слова закончились")
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
                    finish("слова закончились")
                }
                getView()?.hideButtons()
            }
        })
    }

    private fun finish(message: String) {
        getView()?.showFinish(message)
        closeFragmentTimer()
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
        getRepeatWordList(object: SimpleCallback<ArrayList<Word>> {
            override fun onResult(item: ArrayList<Word>) {
                if (item.size!=0) {
                    wordList = item
                    currentWord = wordList.get(0)
                    getView()?.showFirstWord(currentWord.original)
                } else {
                    //getView()?.showToast("слов для проверки не найдено")
                    finish("слов для проверки не найдено")
                }
            }
        })
    }

    private fun closeFragmentTimer() {
        Log.d(LOG_TAG, "closeFragmentTimer()")
        Thread(Runnable {
                try {
                    Log.d(LOG_TAG, "TimeUnit.SECONDS.sleep(3)")
                    TimeUnit.SECONDS.sleep(3)
                    getView()?.closeFragment()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
        }).start()
    }

    val LOG_TAG = RepeatPresenter::class.java.name + " BMTH "
    private val roomController: RoomController
    private var wordList = arrayListOf<Word>()
    private lateinit var currentWord: Word

    init {
        this.roomController=roomController
    }

    private fun getRepeatWordList(callback: SimpleCallback<ArrayList<Word>>) {
        roomController.getRepeatWords(object: RoomController.RoomAsyncCallback<ArrayList<Word>> {
            override fun onSuccess(item: ArrayList<Word>) {
                Log.d(LOG_TAG, "RepeatWordList.size: ${item.size}")
                //item.forEach {
                //    Log.d(LOG_TAG, "word: ${it.original}, time: ${DateHelper.convertFromLongToStringFormat(DateHelper.FORMAT_dd_MM_YYYY, it.repeat_time!!.toLong())}")
                //}
                callback.onResult(item)
            }

            override fun onComplete() {

            }
        })
    }

}