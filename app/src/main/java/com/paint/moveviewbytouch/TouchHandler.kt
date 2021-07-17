package com.paint.moveviewbytouch

import android.view.View
import android.widget.RelativeLayout

class TouchHandler {

    //Single touch event
    private var startX = 0f
    private var startY = 0f

    //Double touch event
    private var previousX0 = 0f
    private var previousY0 = 0f
    private var previousX1 = 0f
    private var previousY1 = 0f

    private var startX0 = 0f
    private var startY0 = 0f
    private var startX1 = 0f
    private var startY1 = 0f

    private var startWidth = 0
    private var startHeight = 0

    //Костылек, чтобы вьюшка не прыгала, когда поднимаем два пальца.
    private var isDoubleTouch = false

    fun addOnTouchListener(
        view: View, onViewParamsChanged: (
            leftMargin: Int,
            topMargin: Int,
            width: Int,
            height: Int
        ) -> Unit
    ) {
        //Эта функция из файла ViewExtension
        view.setOnTouchListener(actionDown = { x, y ->
            isDoubleTouch = false
            startX = x
            startY = y
        }, actionMove = { x, y ->
            if (isDoubleTouch) return@setOnTouchListener

            val layoutParams = view.layoutParams as RelativeLayout.LayoutParams
            layoutParams.leftMargin += (x - startX).toInt()
            layoutParams.topMargin += (y - startY).toInt()
            view.layoutParams = layoutParams

            onViewParamsChanged(
                layoutParams.leftMargin,
                layoutParams.topMargin,
                layoutParams.width,
                layoutParams.height
            )
        }, actionPointerDown = { x0, y0, x1, y1 ->
            isDoubleTouch = true

            previousX0 = x0
            previousY0 = y0
            previousX1 = x1
            previousY1 = y1

            startX0 = x0
            startY0 = y0
            startX1 = x1
            startY1 = y1

            val actionLayoutParams = view.layoutParams as RelativeLayout.LayoutParams
            startWidth = actionLayoutParams.width
            startHeight = actionLayoutParams.height
        }, actionPointerMove = { x0, y0, x1, y1 ->
            val actionLayoutParams = view.layoutParams as RelativeLayout.LayoutParams
            if (x0 > x1) {
                actionLayoutParams.width += (x0 - previousX0).toInt()
                actionLayoutParams.leftMargin += (x1 - startX1).toInt() / 2
            } else {
                actionLayoutParams.width += +(x1 - previousX1).toInt()
                actionLayoutParams.leftMargin += (x0 - startX0).toInt() / 2
            }

            if (y0 > y1) {
                actionLayoutParams.height += (y0 - previousY0).toInt()
                actionLayoutParams.topMargin += (y1 - startY1).toInt() / 2
            } else {
                actionLayoutParams.height += (y1 - previousY1).toInt()
                actionLayoutParams.topMargin += (y0 - startY0).toInt() / 2
            }
            view.layoutParams = actionLayoutParams

            previousX0 = x0
            previousY0 = y0
            previousX1 = x1
            previousY1 = y1

            onViewParamsChanged(
                actionLayoutParams.leftMargin,
                actionLayoutParams.topMargin,
                actionLayoutParams.width,
                actionLayoutParams.height
            )
        })
    }
}