package com.ekochkov.intervallearning.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Word(
        @PrimaryKey val id: Int?,
        @ColumnInfo(name = "original") var original: String?,
        @ColumnInfo(name = "translate") var translate: String?,
        @ColumnInfo(name = "category") var category: String?,
		@ColumnInfo(name = "repeat_time") var repeat_time: String?, //время, когда надо повторить слово
		@ColumnInfo(name = "interval_level") var interval_level: String?, //"уровень" в интервальном повторении
		@ColumnInfo(name = "status") var status: Int? //проверка на включено/выключено повторение
) : Serializable