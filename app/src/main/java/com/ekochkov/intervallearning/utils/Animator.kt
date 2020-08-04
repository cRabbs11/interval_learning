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

    interface AnimationListener {
        fun animationStart()
        fun animationOnHalf()
        fun animationEnd()
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

    /*
    * Поворот по горизонтали (переворот)
     */
    fun turnOverHorizontal(view: View, listener: AnimationListener) {
        val reduceX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0.01f)
        val enlargeX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1f)
		
		val reduceY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.7f)
		val enlargeY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f)
		
        val set = AnimatorSet()
		set.play(reduceX).with(reduceY)
		set.play(enlargeX).with(enlargeY)
		
        set.play(enlargeX).after(reduceX)
		
        set.duration=300

        reduceX.addListener( object: Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                listener.animationOnHalf()
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationStart(p0: Animator?) {
                listener.animationStart()
            }

        })

        enlargeX.addListener( object: Animator.AnimatorListener{
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                listener.animationEnd()
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationStart(p0: Animator?) {

            }
        })

        set.start()    
    }

    fun hideToRightShowFromLeft(view: View, listener: AnimationListener) {
        val translationToRightX = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 700f)

        val translationFromLeftX = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, -700f, 1f)

        val set = AnimatorSet()
        //set.play(reduceX).with(reduceY)
        //set.play(enlargeX).with(enlargeY)

        set.play(translationFromLeftX).after(translationToRightX)

        set.duration=400

        translationToRightX.addListener( object: Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                listener.animationOnHalf()
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationStart(p0: Animator?) {
                listener.animationStart()
            }

        })

        translationFromLeftX.addListener( object: Animator.AnimatorListener{
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                listener.animationEnd()
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationStart(p0: Animator?) {

            }
        })

        set.start()
    }
}