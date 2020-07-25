package com.example.plantarinn

import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.os.Build
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PlantingMainGraphics : View {

    private var currentAngle = -90f
    private val tre: Drawable = resources.getDrawable(R.drawable.tre, null)
    private val rr = RotateDrawable()

    //constructor(context: Context) : super(context) {
        //init(null, 0)
    //}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        //init(attrs, 0)

    }

    init {

        val anim = ValueAnimator.ofFloat(0f, 360f)
        anim.apply {
            duration = 2000
            repeatMode = ValueAnimator.RESTART
            repeatCount = INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener { ValueAnimator ->
                currentAngle = ValueAnimator.animatedValue as Float
                //invalidate requests the view to be readrawn, aka: will call onDraw again
                invalidate()
            }
        }

        anim.start()

        //tre.setBounds(0, 0, 250, 250)

        rr.drawable = tre
        rr.fromDegrees = 0f
        rr.toDegrees = 360f
        rr.pivotX = 0.5f
        rr.pivotY = 0.6f


    }



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //{

        /*
        Gets the width/height in pixels based on the current pixel density of phone.
        This way all graphics on different devices are correct. If I were to just define it in
        pixels (for example in drawCircle(...) Then it would get all messed up on different phone screen sizes).
        (Both bevause of different resolutions and different pixel densities)
        */
        val width = resources.getDimensionPixelSize(R.dimen.plantingGraphics_width)
        val height = resources.getDimensionPixelSize(R.dimen.plantingGraphics_height)

        //middle in float
        val xMiddle = (width/2).toFloat()
        val yMiddle = (height/2).toFloat()

        println("Width of platningView ${width}")
        println("height of platningView ${height}")

        val white = Paint()
        white.setARGB(255, 255, 255, 255)
        white.color = Color.parseColor("#fffafafa")
        white.flags = 1
        //canvas.drawCircle(xMiddle, yMiddle, 300f, blue)

        val green = Paint()
        green.color = ContextCompat.getColor(context, R.color.colorPrimary)
        //Should enable anti-aliasing
        green.flags = 1

        //println("Is antialiasing on? ${green.isAntiAlias}")
        println("current Angle: ${currentAngle}")

        val arcBox = floatArrayOf(xMiddle - 400f, yMiddle - 500f, xMiddle + 400f, yMiddle + 300f)

        canvas.drawArc(arcBox[0], arcBox[1], arcBox[2], arcBox[3], -90f,
            currentAngle, true, green)

        canvas.drawCircle(xMiddle, yMiddle - 100f, 200f, white)
        //tre.draw(canvas)

        rr.setBounds((xMiddle - 125).toInt(), (yMiddle - 250).toInt(), (xMiddle + 125).toInt(), (yMiddle).toInt())
        //rr.setBounds(arcBox[0] + )
        //After a lot of looking, this level attribute goes from 0 to 10000 (0% to 100%) In this case on fromDegrees 0 to toDegrees 360
        rr.level = currentAngle.toInt() * 28
        rr.draw(canvas)


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}
