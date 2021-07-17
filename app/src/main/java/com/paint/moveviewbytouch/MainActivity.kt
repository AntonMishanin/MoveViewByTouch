package com.paint.moveviewbytouch

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity


/**
 * Вся логика должна быть во ViewModel or Presenter или вынести в кастомную вью.
 */

class MainActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initAndMoveViewByTouch()

        /**
         * Достаточно использовать вот эти строки ниже
         */
        val image = findViewById<ImageView>(R.id.image)
        TouchHandler().addOnTouchListener(image) { leftMargin, topMargin, width, height ->
            //Вот сюда приходят актуальные значения вьюшки
        }

        val addView = findViewById<Button>(R.id.add_view)
        addView.setOnClickListener { addAndMoveViewByTouch() }
    }

    //Двигать уже созданную View
    private fun initAndMoveViewByTouch() {
        val image = findViewById<ImageView>(R.id.image)
        //Эта функция из файла ViewExtension
        image.setOnTouchListener(actionDown = { x, y ->
            isDoubleTouch = false
            startX = x
            startY = y
        }, actionMove = { x, y ->
            if (isDoubleTouch) return@setOnTouchListener

            val layoutParams = image.layoutParams as RelativeLayout.LayoutParams
            layoutParams.leftMargin += (x - startX).toInt()
            layoutParams.topMargin += (y - startY).toInt()
            image.layoutParams = layoutParams
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

            val actionLayoutParams = image.layoutParams as RelativeLayout.LayoutParams
            startWidth = actionLayoutParams.width
            startHeight = actionLayoutParams.height
        }, actionPointerMove = { x0, y0, x1, y1 ->
            val actionLayoutParams = image.layoutParams as RelativeLayout.LayoutParams
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
            image.layoutParams = actionLayoutParams

            previousX0 = x0
            previousY0 = y0
            previousX1 = x1
            previousY1 = y1
        })
    }

    //Програамно создавать View и двигать пальцем
    private fun addAndMoveViewByTouch() {
        val root = findViewById<RelativeLayout>(R.id.root)

        val stickerView = LayoutInflater.from(this).inflate(R.layout.item_sticer, root, false)

        val layoutParams = RelativeLayout.LayoutParams(150, 150)
        layoutParams.leftMargin = 50
        layoutParams.topMargin = 50
        layoutParams.bottomMargin = -250
        layoutParams.rightMargin = -250
        stickerView.layoutParams = layoutParams

        stickerView.setOnTouchListener(actionDown = { x, y ->
            startX = x
            startY = y
        }, actionMove = { x, y ->
            val actionLayoutParams = stickerView.layoutParams as RelativeLayout.LayoutParams
            actionLayoutParams.leftMargin += (x - startX).toInt()
            actionLayoutParams.topMargin += (y - startY).toInt()
            stickerView.layoutParams = actionLayoutParams
        })
        root.addView(stickerView)
    }
}