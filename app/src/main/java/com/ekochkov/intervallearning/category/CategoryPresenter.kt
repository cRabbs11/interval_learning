package com.ekochkov.intervallearning.category

import android.os.Bundle
import android.util.Log
import com.ekochkov.intervallearning.interval.IntervalLearning
import com.ekochkov.intervallearning.mvp.PresenterBase
import com.ekochkov.intervallearning.room.RoomController
import com.ekochkov.intervallearning.room.Word
import com.ekochkov.intervallearning.utils.DateHelper
import com.ekochkov.intervallearning.utils.SimpleCallback


public class CategoryPresenter(roomController: RoomController):
        PresenterBase<CategoryContract.View>(), CategoryContract.Presenter {

	override fun onDeleteWordClicked() {
		var bundle = getView()?.getWordData()

		var wordOriginal = bundle?.getString("wordOriginal")
		var categoryName = bundle?.getString("categoryName")

		isWordExist(wordOriginal, categoryName, object: SimpleCallback<Int?> {
			override fun onResult(id: Int?) {
				if (id==-5) {
					getView()?.showToast("слово не найдено")
				} else {
					deleteWordFromBD(id, object: SimpleCallback<Int?> {
						override fun onResult(item: Int?) {
							if (id==-5) {
								getView()?.showToast("слово не найдено")
							} else {
								getWordList(categoryName)
								getView()?.clearEditText()
								getView()?.showToast("слово удалено")
							}
						}
					})
				}
			}

		})
	}

	override fun onSaveWordClicked() {
		Log.d(LOG_TAG, "onSaveImageButtonClicked")
		var bundle = getView()?.getWordData()

		var wordOriginal = bundle?.getString("wordOriginal")
		var wordTranslate = bundle?.getString("wordTranslate")
		var categoryName = bundle?.getString("categoryName")

		isWordExist(wordOriginal, categoryName, object: SimpleCallback<Int?> {
			override fun onResult(id: Int?) {
				if (id==-5) {
					addNewWordInBD(wordOriginal, wordTranslate, categoryName, object: SimpleCallback<Long?> {
						override fun onResult(item: Long?) {
							getWordList(categoryName)
							getView()?.clearEditText()
							getView()?.showToast("слово добавлено")
						}
					})
				} else {
					updateWordInBD(id, wordOriginal, wordTranslate, categoryName, object: SimpleCallback<Int?> {
						override fun onResult(item: Int?) {
							getWordList(categoryName)
							getView()?.clearEditText()
							getView()?.showToast("слово обновлено")
						}
					})
				}
 			}
		})
	}

	/**
	 * Удаляет слово из БД по Id
	 * @param callback если слова нет вернет -5, в остальных случаях вернет кол-во удаленных слов
	 */
	private fun deleteWordFromBD(id: Int?, callback: SimpleCallback<Int?>) {
		Log.d(LOG_TAG, "deleteWordFromBD... id: ${id}")
		roomController.deleteWord(id, object: RoomController.RoomAsyncCallback<Int> {
			override fun onSuccess(id: Int) {
				Log.d(LOG_TAG, "onSuccess id: ${id}")
				callback.onResult(id)
			}

			override fun onComplete() {
				Log.d(LOG_TAG, "onComplete()")
				callback.onResult(-5)
			}
		})
	}

	/**
	 * обновляет слово
	 */
	private fun updateWordInBD(id: Int?, wordOriginal: String?, wordTranslate: String?, categoryName: String?, callback: SimpleCallback<Int?>) {
		Log.d(LOG_TAG, "updateWordInBD")
		var repeatTime = getNewWordRepeatTime()
		var intervalLevel = "1"
		var status = getNewWordStatus(getView()?.getBundle())
		var word = Word(id, wordOriginal, wordTranslate, categoryName, repeatTime, intervalLevel, status)
		roomController.updateWord(word, object: RoomController.RoomAsyncCallback<Int> {
			override fun onSuccess(id: Int) {
				Log.d(LOG_TAG, "insertWord onSuccess id=${id}")
				callback.onResult(id)
			}

			override fun onComplete() {
			}
		})
	}

	/**
	 * добавляет новое слово
	 */
	private fun addNewWordInBD(wordOriginal: String?, wordTranslate: String?, categoryName: String?, callback: SimpleCallback<Long?>) {
		Log.d(LOG_TAG, "addNewWordInBD")
		var repeatTime = getNewWordRepeatTime()
		var intervalLevel = "1"
		var status = getNewWordStatus(getView()?.getBundle())
		roomController.insertWord(wordOriginal, wordTranslate, categoryName, repeatTime, intervalLevel, status, object: RoomController.RoomAsyncCallback<Long> {
			override fun onSuccess(id: Long) {
				Log.d(LOG_TAG, "insertWord onSuccess id=${id}")
				callback.onResult(id)
			}

			override fun onComplete() {
			}
		})
	}

	/**
	 * Проверяет наличие слова в категории
	 * @param callback если слова нет в категории, вернет -5, в остальных случаях вернет id слова
	 */
	private fun isWordExist(original: String?, category: String?, callback: SimpleCallback<Int?>) {
		roomController.getByOriginal(original, object: RoomController.RoomAsyncCallback<Word> {
			override fun onSuccess(item: Word) {
				if (item.category.equals(category)) {
					callback.onResult(item.id)
				} else {
					callback.onResult(-5)
				}
			}

			override fun onComplete() {
				callback.onResult(-5)
			}
		})
	}

	val LOG_TAG = CategoryPresenter::class.java.name + " BMTH "
	
    override fun onFloatingBtnClicked() {
    }

    override fun onListItemClicked(word: Word) {
		getView()?.showWordData(word)
    }

    override fun viewIsReady() {
		getCategoryData(getView()?.getBundle())
    }

    private val roomController: RoomController

    init {
        this.roomController=roomController
    }
	
	fun getCategoryData(bundle: Bundle?) {
		var categoryName = bundle?.getString("categoryName")
		
		if (categoryName!=null) {
			getView()?.showCategoryName(categoryName)
			getWordList(categoryName)
		} else {
			createCategoryName(object: SimpleCallback<String> {
				override fun onResult(categoryName: String) {
					getView()?.showCategoryName(categoryName)
				}
			})
		}
	}

    internal fun getWordList(category: String?) {
        Log.d(LOG_TAG, "getCategoryList")
        roomController.getWordsByCategory(category, object: RoomController.RoomAsyncCallback<ArrayList<Word>> {
            override fun onSuccess(words: ArrayList<Word>) {
				getView()?.showWordList(words)
            }

			override fun onComplete() {

			}
        })
    }
	
	private fun createCategoryName(callback: SimpleCallback<String>) {
		roomController.getAllCategories(object: RoomController.RoomAsyncCallback<ArrayList<Word>> {
            override fun onSuccess(words: ArrayList<Word>) {
				var category = "категория 1"
				for(i in 1..words.size) {
					words.forEach {
						if (it.category.equals("категория $i")) {
							category = "категория ${i+1}"
						}
					}
				}
				callback.onResult(category)
            }

			override fun onComplete() {

			}
        })
	}

	
	private fun getNewWordRepeatTime(): String {
		var repeatTime = DateHelper.getCurrentLongTime().toString()
		var interval = IntervalLearning()
		return interval.getNewRepeatTime(repeatTime,"1")
	}
	
	private fun getNewWordStatus(bundle: Bundle?): Int {
		var status = bundle?.getString("status")
		if (status!=null) {
			return 1
		} else {
			return 0
		}
	}	
}