package com.ekochkov.intervallearning.mainMenu

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.ekochkov.intervallearning.interval.IntervalController
import com.ekochkov.intervallearning.mvp.PresenterBase
import com.ekochkov.intervallearning.room.RoomController
import com.ekochkov.intervallearning.room.Word
import com.ekochkov.intervallearning.utils.SimpleCallback
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.ekochkov.intervallearning.room.Category


public class MainMenuPresenter(lifecycleOwner: LifecycleOwner, roomController: RoomController, intervalController: IntervalController):
        PresenterBase<MainMenuContract.View>(), MainMenuContract.Presenter {

    override fun onCategoryStatusClicked(item: Category) {
        if (item.status==1) {
            item.status=0
        } else {
            item.status=1
        }

        roomController.setCategoryStatus(item, object: RoomController.RoomAsyncCallback<Int> {
            override fun onSuccess(item: Int) {
                Log.d(LOG_TAG, "setCategoryStatus: ${item}")
                getCategoryList()
            }

            override fun onComplete() {

            }
        })
    }

    override fun onNotificationClicked() {
        Log.d(LOG_TAG, "onNotificationClicked()")
        getView()?.startRepeat()
    }

    val LOG_TAG = MainMenuPresenter::class.java.name + " BMTH "

    override fun onFloatingBtnClicked() {
        getView()?.addNewCategory()
    }

    override fun onListItemClicked() {

    }

    override fun viewIsReady() {
        getCategoryList()
        startIntervalService()
    }

    fun startIntervalService() {
        var liveData = intervalController.getAppStatus() as LiveData<Boolean>
        liveData.observe(lifecycleOwner, object: Observer<Boolean> {
            override fun onChanged(value: Boolean?) {
                Log.d(LOG_TAG, "appStatus onChanged: ${value}")
                roomController.getRepeatWords(object : RoomController.RoomAsyncCallback<ArrayList<Word>> {
                    override fun onSuccess(item: ArrayList<Word>) {
                        Log.d(LOG_TAG, "repeatWordList.size: ${item.size}")
                        if (item.size!=0) {
                            getView()?.showRepeatLayout("повторить слова")
                        } else {
                            getView()?.hideRepeatLayout()
                        }
                    }

                    override fun onComplete() {

                    }
                })


            }
        })
    }

    private val roomController: RoomController
    private val intervalController: IntervalController
    private val lifecycleOwner: LifecycleOwner

    init {
        this.roomController=roomController
        this.intervalController=intervalController
        this.lifecycleOwner=lifecycleOwner
    }

    internal fun getCategoryList() {
        Log.d(LOG_TAG, "getCategoryList")
        roomController.getCategories(object: RoomController.RoomAsyncCallback<ArrayList<Category>> {
            override fun onSuccess(categorylist: ArrayList<Category>) {
                getView()?.showCategoryList(categorylist)
            }

            override fun onComplete() {

            }
        })
        //roomController.getAllCategories(object: RoomController.RoomAsyncCallback<ArrayList<Word>> {
        //    override fun onSuccess(words: ArrayList<Word>) {
		//		getView()?.showCategoryList(words)
        //    }
        //})
    }

}