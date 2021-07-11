package com.paint.moveviewbytouch

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity


/**
 * Вся логика должна быть во ViewModel or Presenter.
 */

class MainActivity : AppCompatActivity() {

    private var xStart = 0f
    private var yStart = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAndMoveViewByTouch()

        val addView = findViewById<Button>(R.id.add_view)
        addView.setOnClickListener { addAndMoveViewByTouch() }
    }

    //Двигать уже созданную View
    private fun initAndMoveViewByTouch() {
        val image = findViewById<ImageView>(R.id.image)
        //Эта функция из файла ViewExtension
        image.setOnTouchListener(actionDown = { x, y ->
            xStart = x
            yStart = y
        }, actionMove = { x, y ->
            val layoutParams = image.layoutParams as RelativeLayout.LayoutParams
            layoutParams.leftMargin += (x - xStart).toInt()
            layoutParams.topMargin += (y - yStart).toInt()
            image.layoutParams = layoutParams
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
            xStart = x
            yStart = y
        }, actionMove = { x, y ->
            val actionLayoutParams = stickerView.layoutParams as RelativeLayout.LayoutParams
            actionLayoutParams.leftMargin += (x - xStart).toInt()
            actionLayoutParams.topMargin += (y - yStart).toInt()
            stickerView.layoutParams = actionLayoutParams
        })
        root.addView(stickerView)
    }
}