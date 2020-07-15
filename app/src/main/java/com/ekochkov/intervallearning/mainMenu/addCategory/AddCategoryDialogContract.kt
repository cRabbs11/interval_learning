package com.ekochkov.intervallearning.mainMenu.addCategory

import android.os.Bundle
import com.ekochkov.intervallearning.mvp.MVPView
import com.ekochkov.intervallearning.mvp.MvpPresenter
import com.ekochkov.intervallearning.room.Word

public interface AddCategoryDialogContract {
 
    interface View: MVPView {
        fun getBundle(): Bundle?
        fun fillWordData(wordBundle: Bundle?)
        fun getEditTextData(): Bundle
        fun returnToCategory(wordBundle: Bundle)
    }
 
    interface Presenter: MvpPresenter<View> {
        fun onAddClicked()
    }
}