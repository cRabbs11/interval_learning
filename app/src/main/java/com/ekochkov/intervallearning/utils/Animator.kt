package com.ekochkov.intervallearning.utils

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator

public class Animator {

    companion object {
        const val HIDE_ANIMATION=0
        const val SHOW_ANIMATION=1
    }

    val LOG_TAG = Animator::class.java.name + " BMTH "

    fun hideMoveToRight(view: View) {
         val translationX = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 50f)
         val alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f)
         val set = AnimatorSet()
         view.isEnabled=false
         set.play(translationX).with(alpha)
         set.duration=200
         set.interpolator= DecelerateInterpolator()
         set.addListener( object : Animator.AnimatorListener {
             override fun onAnimationRepeat(animation: Animator?) {
                 TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
             }

             override fun onAnimationCancel(animation: Animator?) {
                 TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
             }

             override fun onAnimationStart(animation: Animator?) {
                 Log.d(LOG_TAG, "show onAnimationStart...")
             }

             override fun onAnimationEnd(animation: Animator?) {
                 Log.d(LOG_TAG, "show onAnimationEnd...")
             }

         })
         set.start()
    }

    fun showMoveFromRight(view: View) {
        val translationX = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0f)
        val alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f)
        val set = AnimatorSet()
        set.play(translationX).with(alpha)
        set.duration=200
        set.interpolator= DecelerateInterpolator()
        view.isEnabled=true
        set.addListener( object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationCancel(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationStart(animation: Animator?) {
                Log.d(LOG_TAG, "show onAnimationStart...")
            }

            override fun onAnimationEnd(animation: Animator?) {
                Log.d(LOG_TAG, "show onAnimationEnd...")
            }

        })
        set.start()
    }
}