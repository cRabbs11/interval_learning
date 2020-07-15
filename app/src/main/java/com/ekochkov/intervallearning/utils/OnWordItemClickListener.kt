package com.ekochkov.intervallearning.utils

interface OnWordItemClickListener<T> {
    fun onDeleteClick(item: T)
	fun onChangeClick(item: T)
}