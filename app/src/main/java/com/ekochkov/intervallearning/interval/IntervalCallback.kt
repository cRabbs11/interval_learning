package com.ekochkov.intervallearning.interval

interface IntervalCallback<T> {
    fun onResult(item: T)
    fun getBound(): Boolean
}