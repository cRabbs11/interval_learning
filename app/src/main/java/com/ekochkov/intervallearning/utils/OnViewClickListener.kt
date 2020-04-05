package com.ekochkov.intervallearning.utils

import android.view.View

interface OnViewClickListener<view: View, T> {
    fun onViewClick(view: View, item: T)
}