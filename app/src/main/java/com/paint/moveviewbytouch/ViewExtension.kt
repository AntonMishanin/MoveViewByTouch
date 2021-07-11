package com.paint.moveviewbytouch

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

@SuppressLint("ClickableViewAccessibility")
fun View.setOnTouchListener(
    actionDown: ((x: Float, y: Float) -> Unit)? = null,
    actionPointerDown: ((x: Float, y: Float) -> Unit)? = null,
    actionMove: ((x: Float, y: Float) -> Unit)? = null,
    actionUp: ((x: Float, y: Float) -> Unit)? = null,
    actionPointerUp: ((x: Float, y: Float) -> Unit)? = null,
) {
    this.setOnTouchListener { view, event ->
        when (event.action and MotionEvent.ACTION_MASK) {
            //Это событие вызывается, когда нажимает одним пальцем на View
            MotionEvent.ACTION_DOWN -> {
                actionDown?.invoke(event.x, event.y)
            }
            //Это событие вызывается, когда нажимает двумя пальцами на View
            MotionEvent.ACTION_POINTER_DOWN -> {
                actionPointerDown?.invoke(event.x, event.y)
            }
            //Это событие вызывается, когда двигаем View
            MotionEvent.ACTION_MOVE -> {
                actionMove?.invoke(event.x, event.y)
            }
            //Это событие вызывается, когда поднимаем один палец
            MotionEvent.ACTION_UP -> {
                actionUp?.invoke(event.x, event.y)
            }
            //Это событие вызывается, когда поднимаем  пальца
            MotionEvent.ACTION_POINTER_UP -> {
                actionPointerUp?.invoke(event.x, event.y)
            }
        }
        true
    }
}