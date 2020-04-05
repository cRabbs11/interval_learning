package com.ekochkov.intervallearning.repeat

import com.ekochkov.intervallearning.mvp.MVPView
import com.ekochkov.intervallearning.mvp.MvpPresenter
import com.ekochkov.intervallearning.room.Word

public interface RepeatContract {

    interface View: MVPView {
        fun showWord(word: String?)
        fun showToast(toast: String)
        fun showButtons()
        fun hideButtons()
        fun showFinish()
    }

    interface Presenter: MvpPresenter<View> {
        fun onPositiveButtonClicked()
        fun onNegativeButtonClicked()
        fun onWordClicked()
    }
}