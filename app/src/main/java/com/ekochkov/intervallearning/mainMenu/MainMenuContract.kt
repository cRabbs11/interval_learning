package com.ekochkov.intervallearning.mainMenu

import com.ekochkov.intervallearning.mvp.MVPView
import com.ekochkov.intervallearning.mvp.MvpPresenter
import com.ekochkov.intervallearning.room.Category
import com.ekochkov.intervallearning.room.Word

public interface MainMenuContract {
 
    interface View: MVPView {
		fun showCategoryList(words: ArrayList<Category> )
		fun showEmptyCategoryMessage()
        fun addNewCategory()
        fun startRepeat()
        fun showRepeatLayout(text: String)
        fun hideRepeatLayout()
    }
 
    interface Presenter: MvpPresenter<View> {
		fun onListItemClicked()
        fun onCategoryStatusClicked(item: Category)
        fun onFloatingBtnClicked()
        fun onNotificationClicked()
    }
}