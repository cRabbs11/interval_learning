package com.ekochkov.intervallearning.room

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ekochkov.intervallearning.utils.DateHelper
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

/**
 * Запросы к БД через Room
 */
class RoomController(context: Context) {

    val LOG_TAG = RoomController::class.java.getName() + " BMTH "

	var context: Context
	
	interface RoomAsyncCallback<T> {
		fun onSuccess(item: T)
        fun onComplete()
	}

    companion object {
        val WORD_LIST_FILTER_BY_ORIGINAL = 1
        val WORD_LIST_FILTER_BY_TRANSLATE = 2
        val WORD_LIST_FILTER_BY_REPEAT_TIME = 3
    }

    init {
        this.context=context
    }

    fun getByOriginal(originalWord: String?, callback: RoomAsyncCallback<Word>) {
        val db = Room.databaseBuilder(
            context,
            WordDatabase::class.java, "words_db.sqlite")
            .createFromAsset("databases/words_db.sqlite")
            .build()

        db.wordDao().getByOriginal(originalWord)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: DisposableMaybeObserver<Word>() {
                override fun onSuccess(word: Word) {
                    Log.d(LOG_TAG, "db.wordDao().getAllCoins() onSuccess")
                    callback.onSuccess(word)
                    db.close()
                }

                override fun onComplete() {
                    Log.d(LOG_TAG, "db.wordDao().getAllCoins() onComplete")
                    callback.onComplete()
                    db.close()

                }

                override fun onError(e: Throwable) {
                    Log.d(LOG_TAG, "db.wordDao().getAllCoins() onError: $e")
                    db.close()
                }

            })
    }

    /**
     * возвращает список слов, которые необходимо проверить
     */
    public fun getRepeatWords(callback: RoomAsyncCallback<ArrayList<Word>>) {
        val observable = Observable.create<ArrayList<Word>> {
            val db = Room.databaseBuilder(
                context,
                WordDatabase::class.java, "words_db.sqlite")
                .createFromAsset("databases/words_db.sqlite")
                .build()
            var repeatList = arrayListOf<Word>()
            var list = db.wordDao().getAll()
            list.forEach {
                if (it.status==1 && timeHasCome(it)) {
                    repeatList.add(it)
                }
            }
            db.close()
            it.onNext(repeatList)
            it.onComplete()
        }
        observable.
            observeOn(AndroidSchedulers.mainThread()).
            subscribeOn(Schedulers.io()).
            subscribe {
                callback.onSuccess(it)
            }
    }

    /**
     * задает статус в категории
     */
    fun setCategoryStatus(category: Category, callback: RoomAsyncCallback<Int>) {
        val observable = Observable.create<Int> {
            val db = Room.databaseBuilder(
                context,
                WordDatabase::class.java, "words_db.sqlite")
                .createFromAsset("databases/words_db.sqlite")
                .build()
            var value = 0
            var list = db.wordDao().getAll()
            list.forEach {
                if (it.category.equals(category.category)) {
                    it.status=category.status
                }
                value = db.wordDao().updateSync(list)
            }
            db.close()
            it.onNext(value)
            it.onComplete()
        }

        observable.
            observeOn(AndroidSchedulers.mainThread()).
            subscribeOn(Schedulers.io()).
            subscribe {
                Log.d(LOG_TAG, "threadName: ${Thread.currentThread().getName()}")
                callback.onSuccess(it)
            }
    }

    /**
     * Возвращает список категорий
     */
    fun getCategories(callback: RoomAsyncCallback<ArrayList<Category>>) {
        val observable = Observable.create<ArrayList<Category>> {
            val db = Room.databaseBuilder(
                context,
                WordDatabase::class.java, "words_db.sqlite")
                .createFromAsset("databases/words_db.sqlite")
                .build()
            var categoryList = arrayListOf<Category>()
            var list = db.wordDao().getAll()
            list.forEach {
                if (!addWordInCategory(it.category, categoryList)) {
                    categoryList.add(Category(it.category, it.status, 1))
                }

            }
            db.close()
            it.onNext(categoryList)
            it.onComplete()
        }

        observable.
            observeOn(AndroidSchedulers.mainThread()).
            subscribeOn(Schedulers.io()).
            subscribe {
                Log.d(LOG_TAG, "threadName: ${Thread.currentThread().getName()}")
                callback.onSuccess(it)
            }
    }

    /**
     * Добавить монеты в коллекцию
     */
    fun getAllCategories(callback: RoomAsyncCallback<ArrayList<Word>>) {
        val observable = Observable.create<ArrayList<Word>> {
            val db = Room.databaseBuilder(
                context,
                WordDatabase::class.java, "words_db.sqlite")
                .createFromAsset("databases/words_db.sqlite")
                .build()
            var arrayList = arrayListOf<Word>()
            var list = db.wordDao().getAll()
            list.forEach {
                arrayList.add(it)
            }
            db.close()
            it.onNext(arrayList)
            it.onComplete()
        }

        observable.
            observeOn(AndroidSchedulers.mainThread()).
            subscribeOn(Schedulers.io()).
            subscribe {
                Log.d(LOG_TAG, "threadName: ${Thread.currentThread().getName()}")
                callback.onSuccess(it)
            }
    }
	
	fun getAllWords(callback: RoomAsyncCallback<ArrayList<Word>>) {
        val observable = Observable.create<ArrayList<Word>> {
            val db = Room.databaseBuilder(
                context,
                WordDatabase::class.java, "words_db.sqlite")
                .createFromAsset("databases/words_db.sqlite")
                .build()
            var arrayList = arrayListOf<Word>()
            var list = db.wordDao().getAll()
            list.forEach {
                arrayList.add(it)
            }
            db.close()
            it.onNext(arrayList)
            it.onComplete()
        }

        observable.
            observeOn(AndroidSchedulers.mainThread()).
            subscribeOn(Schedulers.io()).
            subscribe {
                //Log.d(LOG_TAG, "threadName: ${Thread.currentThread().getName()}")
                callback.onSuccess(it)
            }
    }
	
	fun getWordsByCategory(category: String?, callback: RoomAsyncCallback<ArrayList<Word>>) {
        val observable = Observable.create<ArrayList<Word>> {
            val db = Room.databaseBuilder(
                context,
                WordDatabase::class.java, "words_db.sqlite")
                .createFromAsset("databases/words_db.sqlite")
                .build()
            var arrayList = arrayListOf<Word>()
            var list = db.wordDao().getByCategory(category)
            list.forEach {
                arrayList.add(it)
            }
            db.close()
            it.onNext(arrayList)
            it.onComplete()
        }

        observable.
            observeOn(AndroidSchedulers.mainThread()).
            subscribeOn(Schedulers.io()).
            subscribe {
                Log.d(LOG_TAG, "threadName: ${Thread.currentThread().getName()}")
                callback.onSuccess(it)
            }
    }
	
	fun searchByOriginal(category: String, search: String, callback: RoomAsyncCallback<ArrayList<Word>>) {
		val observable = Observable.create<ArrayList<Word>> {
            val db = Room.databaseBuilder(
                context,
                WordDatabase::class.java, "words_db.sqlite")
                .createFromAsset("databases/words_db.sqlite")
                .build()
            var arrayList = arrayListOf<Word>()
            //var list = db.wordDao().searchByOriginal(category, search)
            var list = db.wordDao().searchByOriginalOrTranslate(category, search)
            list.forEach {
                arrayList.add(it)
            }
            db.close()
            it.onNext(arrayList)
            it.onComplete()
        }

        observable.
            observeOn(AndroidSchedulers.mainThread()).
            subscribeOn(Schedulers.io()).
            subscribe {
                Log.d(LOG_TAG, "threadName: ${Thread.currentThread().getName()}")
                callback.onSuccess(it)
            }
	}
	
	fun getById(id: Integer?, callback: RoomAsyncCallback<Word>) {
        val db = Room.databaseBuilder(
                context,
                WordDatabase::class.java, "words_db.sqlite")               
                .createFromAsset("databases/words_db.sqlite")
                .build()

        db.wordDao().getById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: DisposableMaybeObserver<Word>() {
                    override fun onSuccess(word: Word) {
                        callback.onSuccess(word)
                        db.close()
                    }

                    override fun onComplete() {
                        Log.d(LOG_TAG, "db.wordDao().getAllCoins() onComplete")
                        db.close()
                    }

                    override fun onError(e: Throwable) {
                        Log.d(LOG_TAG, "db.wordDao().getAllCoins() onError: $e")
                        db.close()
                    }

                })
    }

    /**
     * выдает новый id, которого нет в словах из list
     */
    private fun getNewId(list: List<Word>): Int? {
        var id = 0
        list.forEach {
            if (it.id==id) {
                id++
            }
        }
        return id
    }

    /**
     * добавление слова в список
     */
    fun insertWord(wordOriginal: String?, wordTranslate: String?, categoryName: String?, repeatTime: String?, intervalLevel: String?, status: Int?, callback: RoomAsyncCallback<Long>) {
        val observable = Observable.create<Long> {
            val db = Room.databaseBuilder(
                context,
                WordDatabase::class.java, "words_db.sqlite")
                .createFromAsset("databases/words_db.sqlite")
                .build()

            var id = getNewId(db.wordDao().getAllGroupById())
            var word = Word(id, wordOriginal, wordTranslate, categoryName, repeatTime, intervalLevel, status)
            var long = db.wordDao().insert(word)
            db.close()
            it.onNext(long)
            it.onComplete()
        }

        observable.
            observeOn(AndroidSchedulers.mainThread()).
            subscribeOn(Schedulers.io()).
            subscribe {
                Log.d(LOG_TAG, "threadName: ${Thread.currentThread().getName()}")
                callback.onSuccess(it)
            }
    }
	
	/**
	* удаление слова из списка
	*/
    fun deleteWord(id: Int?, callback: RoomAsyncCallback<Int>) {
        val db = Room.databaseBuilder(
                context,
                WordDatabase::class.java, "words_db.sqlite")
                .createFromAsset("databases/words_db.sqlite")
                .build()
        db.wordDao().deleteById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:
                        DisposableMaybeObserver<Int>() {
                    override fun onSuccess(result: Int) {
                        callback.onSuccess(result)
                        db.close()
                    }

                    override fun onComplete() {
                        Log.d(LOG_TAG, "db.wordDao().deleteWord() onComplete")
                        callback.onComplete()
                        db.close()
                    }

                    override fun onError(e: Throwable) {
                        Log.d(LOG_TAG, "db.wordDao().deleteWord() onError: $e")
                        db.close()
                    }
                })
    }
	
	fun updateWord(word: Word?, callback: RoomAsyncCallback<Int>) {
        val observable = Observable.create<Int> {
            val db = Room.databaseBuilder(
                context,
                WordDatabase::class.java, "words_db.sqlite")
                .createFromAsset("databases/words_db.sqlite")
                .build()
            var value = db.wordDao().update(word)
            db.close()
            it.onNext(value)
            it.onComplete()
        }

        observable.
            observeOn(AndroidSchedulers.mainThread()).
            subscribeOn(Schedulers.io()).
            subscribe {
                Log.d(LOG_TAG, "threadName: ${Thread.currentThread().getName()}")
                callback.onSuccess(it)
            }
	}

    /**
     * сравнивание текущего времени и вермени проверки слова
     */
    private fun timeHasCome(word: Word): Boolean {
        //Log.d(LOG_TAG, " ")
        //Log.d(LOG_TAG, "timeHasCome: currentTime: ${DateHelper.convertFromLongToStringFormat(DateHelper.FORMAT_dd_MM_YYYY_HH_mm_z, DateHelper.getCurrentLongTime())}")
        //Log.d(LOG_TAG, "word repeatTime: ${DateHelper.convertFromLongToStringFormat(DateHelper.FORMAT_dd_MM_YYYY_HH_mm_z, word.repeat_time!!.toLong())}")
        var currentTime = DateHelper.getCurrentLongTime()
        if ((word.repeat_time!!.toLong()<=currentTime) ||
            (DateHelper.convertFromLongToStringFormat(DateHelper.FORMAT_dd_MM_YYYY, word.repeat_time!!.toLong()).equals
                (DateHelper.convertFromLongToStringFormat(DateHelper.FORMAT_dd_MM_YYYY, DateHelper.getCurrentLongTime())))) {
            //Log.d(LOG_TAG, "timeHasCome true")
            return true
        } else {
            //Log.d(LOG_TAG, "timeHasCome false")
            return false
        }
    }

    private fun addWordInCategory(category: String?, list: ArrayList<Category>): Boolean {
        var value = false
        list.forEach {
            if (category.equals(it.category)) {
                value = true
                it.wordCount=it.wordCount!!.plus(1)
            }
        }
        return value
    }
}