package com.snavi.swim_photos.touch_listeners

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.GestureDetector.SimpleOnGestureListener


class OnSwipeListener(m_context: Context,
                      val onSwipeRight  : () -> Unit = {},
                      val onSwipeLeft   : () -> Unit = {},
                      val onSwipeUp     : () -> Unit = {},
                      val onSwipeDown   : () -> Unit = {}) : View.OnTouchListener {



    companion object
    {
        const val SWIPE_THRESHOLD = 100
        const val SWIPE_VELOCITY_THRESHOLD = 100
    }



    private val m_gestureDetector = GestureDetector(m_context, GestureListener())



    override fun onTouch(a_view: View?, a_motionEvent: MotionEvent?): Boolean
    {
        return m_gestureDetector.onTouchEvent(a_motionEvent)
    }


    private inner class GestureListener : SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean
        {
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean
        {
            var result = false
            try
            {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (Math.abs(diffX) > Math.abs(diffY))
                {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
                    {
                        if (diffX > 0)
                        {
                            onSwipeRight()
                        }
                        else
                        {
                            onSwipeLeft()
                        }
                        result = true
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD)
                {
                    if (diffY > 0)
                    {
                        onSwipeDown()
                    }
                    else
                    {
                        onSwipeUp()
                    }
                    result = true
                }
            }
            catch (exception: Exception)
            {
                exception.printStackTrace()
            }

            return result
        }
    }
}