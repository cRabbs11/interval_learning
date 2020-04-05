package com.ekochkov.intervallearning.room

import androidx.room.*
import java.io.Serializable

data class Category(

        var category: String?,
		var status: Int?, //проверка на включено/выключено повторение
        var wordCount: Int?

) : Serializable