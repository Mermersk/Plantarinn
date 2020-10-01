package com.example.plantarinn

import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PlantingMainGraphics : View {

    private var currentAngle = -90f
    private val tre: Drawable = resources.getDrawable(R.drawable.tre, null)
    private val rTre: RotateDrawable = RotateDrawable()
    private val white: Paint = Paint()
    private val green: Paint = Paint()
    /*
        Gets the width/height in pixels based on the current pixel density of phone.
        This way all graphics on different devices are correct. If I were to just define it in
        pixels (for example in drawCircle(...) Then it would get all messed up on different phone screen sizes).
        (Both because of different resolutions and different pixel densities)
        */
    private var width: Int? = null
    private var height: Int? = null
    private var xMiddle: Float = 0f
    private var yMiddle: Float = 0f

    private var circleRadius: Float

    private val anim: ValueAnimator

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }

    init {
        visibility = View.INVISIBLE

        anim = ValueAnimator.ofFloat(0f, 360f)
        anim.apply {
            duration = 2000
            repeatMode = ValueAnimator.RESTART
            repeatCount = INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener { ValueAnimator ->
                currentAngle = ValueAnimator.animatedValue as Float
                //invalidate requests the view to be redrawn, aka: will call onDraw again
                invalidate()
            }
        }

        white.setARGB(255, 255, 255, 255)
        white.color = Color.parseColor("#fffafafa")
        white.flags = 1

        green.color = ContextCompat.getColor(context, R.color.colorPrimary)
        //Should enable anti-aliasing
        green.flags = 1


        rTre.drawable = tre
        rTre.fromDegrees = 0f
        rTre.toDegrees = 360f
        rTre.pivotX = 0.5f
        rTre.pivotY = 0.6f

        println("Width of plantingView ${width}")
        println("height of plantingView ${height}")


        val screenDensity = resources.displayMetrics.density

        circleRadius = 80f * screenDensity

    }
    //Makes the view visible and starts the animation
    fun startAnimation() {
        visibility = View.VISIBLE
        anim.start()
    }

    fun setAnimationDuration(d: Long) {
        anim.duration = d
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {

        canvas.drawArc(0f, 0f, width!!.toFloat(), height!!.toFloat(), -90f,
            currentAngle, true, green)

        canvas.drawCircle(xMiddle, yMiddle, circleRadius, white)

        //After a lot of looking, this level attribute goes from 0 to 10000 (0% to 100%) In this case on fromDegrees 0 to toDegrees 360
        rTre.level = (currentAngle * 27.78f).toInt()
        rTre.draw(canvas)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val screenDensity = resources.displayMetrics.density

        width = MeasureSpec.getSize(widthMeasureSpec)  //Gets the calculated width of view in pixels
        //10 August edit: Height is now equal to width, this is to keep a 1:1 aspect ratio such that the arc-drawings is always a perfect circle.
        //Think this way didnt work until I calculated all values with the screen density included :S
        height = MeasureSpec.getSize(widthMeasureSpec)
        xMiddle = (width!! /2).toFloat()
        yMiddle = (height!! /2).toFloat()

        rTre.setBounds((xMiddle - (50 * screenDensity)).toInt(), (yMiddle - (60 * screenDensity)).toInt(), (xMiddle + (50 * screenDensity)).toInt(), (yMiddle + (40 * screenDensity)).toInt() )

        //println("Density of this screen:  ${resources.displayMetrics.density}")
        //println("Is this the right width?  ${MeasureSpec.getSize(widthMeasureSpec)}")
    }
}
