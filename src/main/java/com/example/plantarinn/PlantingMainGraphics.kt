package com.example.plantarinn

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

/**
 * TODO: document your custom view class.
 */
class PlantingMainGraphics : View {

    //constructor(context: Context) : super(context) {
        //init(null, 0)
    //}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        //init(attrs, 0)
    }



    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cc = Paint()
        cc.setARGB(255, 45, 78, 160)
        canvas.drawCircle(200f, 200f, 300f, cc)


    }
}
