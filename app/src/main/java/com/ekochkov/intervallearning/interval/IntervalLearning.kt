package com.ekochkov.intervallearning.interval

import android.content.Context
import android.util.Log
import com.ekochkov.intervallearning.room.RoomController
import com.ekochkov.intervallearning.room.Word
import com.ekochkov.intervallearning.utils.DateHelper
import com.ekochkov.intervallearning.utils.SimpleCallback
import java.util.*


/**
 * Класс для расчета интервального повторения
 */
public class IntervalLearning {

    val LOG_TAG = IntervalLearning::class.java.getName() + " BMTH "
	
	companion object {
		val FIVE_MINUTES_REPEAT_TIME = 300000L
		val TEN_MINUTES_REPEAT_TIME = 600000L
		val ONE_DAY = 86400000L
		val LEVEL_ONE_REPEAT_TIME = ONE_DAY
		val LEVEL_TWO_REPEAT_TIME = LEVEL_ONE_REPEAT_TIME*2+ ONE_DAY
		val LEVEL_THREE_REPEAT_TIME = LEVEL_TWO_REPEAT_TIME*2+ ONE_DAY
		val LEVEL_FOUR_REPEAT_TIME = LEVEL_THREE_REPEAT_TIME*2+ ONE_DAY
		val LEVEL_FIVE_REPEAT_TIME = LEVEL_FOUR_REPEAT_TIME*2+ ONE_DAY
		val LEVEL_SIX_REPEAT_TIME = LEVEL_FIVE_REPEAT_TIME*2+ ONE_DAY
		val LEVEL_SEVEN_REPEAT_TIME = LEVEL_SIX_REPEAT_TIME*2+ ONE_DAY
		val LEVEL_EIGHT_REPEAT_TIME = LEVEL_SEVEN_REPEAT_TIME*2+ ONE_DAY
		val LEVEL_NINE_REPEAT_TIME = LEVEL_EIGHT_REPEAT_TIME*2+ ONE_DAY
		val LEVEL_TEN_REPEAT_TIME = LEVEL_NINE_REPEAT_TIME*2+ ONE_DAY
	}

	/**
	 * возвращает количество дней до повторения
	 */
	fun getDaysToRepeat(repeatTime: Long): Int {
		Log.d(LOG_TAG, "getDaysToRepeat")
		Log.d(LOG_TAG, "repeatTime: ${repeatTime}")
		var days = 0
		var cuttentTime = DateHelper.getCurrentLongTime()

		days = ((repeatTime-cuttentTime)/ONE_DAY).toInt()
		Log.d(LOG_TAG, "days: ${days}")
		if (days==0) {

			var repeatDate = DateHelper.convertFromLongToStringFormat(DateHelper.FORMAT_yyyyMMdd, repeatTime).toInt()
			var currentDate = DateHelper.convertFromLongToStringFormat(DateHelper.FORMAT_yyyyMMdd, cuttentTime).toInt()
			if (repeatDate>currentDate) {
				Log.d(LOG_TAG, "repeatDate: ${repeatDate}")
				Log.d(LOG_TAG, "currentDate: ${currentDate}")
				//days = repeatDate-currentDate
				days = 1
			}
		}
		Log.d(LOG_TAG, "days: ${days}")
		return days
	}
	
	fun rebuildWord(word: Word?, answer: Boolean) {
		//Log.d(LOG_TAG, "rebuildWord()")
		if (answer) {
			setlevelUpWord(word)
		} else {
			setlevelDownWord(word)
		}
		word?.repeat_time = getNewRepeatTime(DateHelper.getCurrentLongTime().toString(), word?.interval_level!!)
		Log.d(LOG_TAG, "newRepeatTime: ${word.repeat_time}")
	}

    /**
     * поднять слово в уровне
     */
    private fun setlevelUpWord(word: Word?): Word? {
        var value = word?.interval_level?.toInt()
		if (value!=null && value<10) {
			value++
			word?.interval_level="$value"
		}
		return word
    }
	
	/**
     * опустить слово в уровне
     */
    private fun setlevelDownWord(word: Word?): Word? {
		Log.d(LOG_TAG, "setlevelDownWord...")
		Log.d(LOG_TAG, "word: ${word?.original}, level: ${word?.interval_level}")
        var value = word?.interval_level?.toInt()
		if (value!=null && value>1) {
			value=1
			word?.interval_level="$value"
		}
		Log.d(LOG_TAG, "result level: ${word?.interval_level}")
		return word
    }

	/**
	* получить время повторения нового слова (1 уровень )
	* @param oldTime предыдущее время повторения
	* @param level уровень повторения
	* @return время в Long в виде строки
	*/
	fun getNewRepeatTime(oldTime: String, level: String): String {
		Log.d(LOG_TAG, "getNewRepeatTime: ")
		Log.d(LOG_TAG, "oldTime: ${oldTime}, level: ${level}")
		when(level) {
			"0" -> {return (oldTime.toLong()).toString()}
			"1" -> {return (oldTime.toLong()+ LEVEL_ONE_REPEAT_TIME).toString()}
			"2" -> {return (oldTime.toLong()+ LEVEL_TWO_REPEAT_TIME).toString()}
			"3" -> {return (oldTime.toLong()+ LEVEL_THREE_REPEAT_TIME).toString()}
			"4" -> {return (oldTime.toLong()+ LEVEL_FOUR_REPEAT_TIME).toString()}
			"5" -> {return (oldTime.toLong()+ LEVEL_FIVE_REPEAT_TIME).toString()}
			"6" -> {return (oldTime.toLong()+ LEVEL_SIX_REPEAT_TIME).toString()}
			"7" -> {return (oldTime.toLong()+ LEVEL_SEVEN_REPEAT_TIME).toString()}
			"8" -> {return (oldTime.toLong()+ LEVEL_EIGHT_REPEAT_TIME).toString()}
			"9" -> {return (oldTime.toLong()+ LEVEL_NINE_REPEAT_TIME).toString()}
			"10" -> {return (oldTime.toLong()+ LEVEL_TEN_REPEAT_TIME).toString()}
			else -> {return (oldTime)}
		}
	}
}