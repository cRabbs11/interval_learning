package com.ekochkov.intervallearning.category

import android.os.Bundle
import com.ekochkov.intervallearning.mvp.MVPView
import com.ekochkov.intervallearning.mvp.MvpPresenter
import com.ekochkov.intervallearning.room.Word

public interface CategoryContract {
 
    interface View: MVPView {
		fun showCategoryName(category: String?)
		fun showWordList(words: ArrayList<Word> )
		fun updateWordList(words: ArrayList<Word> )
        fun addNewCategory()
		fun addNewWord(word: Word)
        fun getBundle(): Bundle?
        fun getWordData(): Bundle?
        fun clearEditText()
        fun showToast(meaasage: String)
        fun showWordData(word: Word)
    }
 
    interface Presenter: MvpPresenter<View> {
		fun onListItemClicked(word: Word)
        fun onFloatingBtnClicked()
        fun onSaveWordClicked()
        fun onDeleteWordClicked()
    }
}