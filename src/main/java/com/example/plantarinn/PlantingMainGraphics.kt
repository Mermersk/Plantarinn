package com.example.plantarinn

import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import android.text.TextPaint
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PlantingMainGraphics : View {

    private var currentAngle = -90f
    private val tre: Drawable = resources.getDrawable(R.drawable.tre, null)
    //rotatable tree
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

    //private lateinit var arcBox: FloatArray
    private var circleRadius: Float

    private val anim: ValueAnimator
    //private val readySP: SoundPool
    //private val oneLoopCompleteSoundID: Int

    constructor(context: Context) : super(context) {
        //init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        //init(attrs, 0)

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

        //anim.start()

        white.setARGB(255, 255, 255, 255)
        white.color = Color.parseColor("#fffafafa")
        white.flags = 1

        green.color = ContextCompat.getColor(context, R.color.colorPrimary)
        //Should enable anti-aliasing
        green.flags = 1

        //width = measuredWidth
        //println("Width with getWidth: ${width}")
        //height = measuredHeight //resources.getDimensionPixelSize(R.dimen.plantingGraphics_height)

        //xMiddle = (width!! /2).toFloat()
        //yMiddle = (height!! /2).toFloat()

        rTre.drawable = tre
        //rTre.setBounds((xMiddle - 125).toInt(), (yMiddle - 250).toInt(), (xMiddle + 125).toInt(), (yMiddle).toInt())
        rTre.fromDegrees = 0f
        rTre.toDegrees = 360f
        rTre.pivotX = 0.5f
        rTre.pivotY = 0.6f

        println("Width of plantingView ${width}")
        println("height of plantingView ${height}")

        //val sp = SoundPool.Builder()
        //sp.setMaxStreams(1)
        //readySP = sp.build()
        //oneLoopCompleteSoundID = readySP.load(context, R.raw.duck1, 1)

        val screenDensity = resources.displayMetrics.density
        //arcBox = floatArrayOf(xMiddle - (100f * screenDensity), yMiddle - (100f * screenDensity), xMiddle + (100f * screenDensity), yMiddle + (100f * screenDensity))
        //val rTreRect = rTre.bounds
        //val arcBox = floatArrayOf(xMiddle - 400f, yMiddle - 400f, xMiddle + 400f, yMiddle + 400f)
        //arcBox = floatArrayOf(rTreRect.left.toFloat() - 50f, rTreRect.top.toFloat(), rTreRect.right.toFloat() + 400f, rTreRect.bottom.toFloat())

        circleRadius = 80f * screenDensity


    }
    //Makes the view visible and starts the animation
    public fun startAnimation() {
        visibility = View.VISIBLE
        anim.start()
    }

    public fun setAnimationDuration(d: Long) {
        anim.duration = d
    }

    //called when play button is pressed
    private fun playSound() {
        //var mp: MediaPlayer? = MediaPlayer.create(context, R.raw.duck1)
        //mp?.start()
        //mp?.setOnCompletionListener { MediaPlayer ->
            //MediaPlayer.stop()
        //}
        //val sp = SoundPool.Builder()
        //sp.setMaxStreams(1)
        //val readySP = sp.build()
        //val oneLoopCompleteSoundID = readySP.load(context, R.raw.duck1, 1)
        //println("sdsdsd ${readySP.play(oneLoopCompleteSoundID, 1.0f, 1.0f, 1, 0, 1.0f)}")
        //readySP.play(oneLoopCompleteSoundID, 1.0f, 1.0f, 1, 0, 1.0f)



    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //width = w
        //height = h

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //println("Is antialiasing on? ${green.isAntiAlias}")
        //println("current Angle: ${currentAngle}")
        //width = measuredWidth
        //println("xMiddle: ${xMiddle}")
        val rTreRect = rTre.bounds
        val screenDensity = resources.displayMetrics.density
        val arcBox = floatArrayOf(xMiddle - (150f * screenDensity), yMiddle - (150f * screenDensity), xMiddle + (150f * screenDensity), yMiddle + (150f * screenDensity))
        //val arcBox = floatArrayOf(rTreRect.left.toFloat() - (50f * screenDensity), rTreRect.top.toFloat() - (75f * screenDensity), rTreRect.right.toFloat() + (50f * screenDensity), rTreRect.bottom.toFloat() + (25f * screenDensity))

        //canvas.drawArc(arcBox[0], arcBox[1], arcBox[2], arcBox[3], -90f,
            //currentAngle, true, green)

        canvas.drawArc(0f, 0f, width!!.toFloat(), height!!.toFloat(), -90f,
            currentAngle, true, green)

        //canvas.drawArc(0f, 0f, width!!.toFloat(), height!!.toFloat(), -90f,
            //currentAngle, true, green)


        canvas.drawCircle(xMiddle, yMiddle, circleRadius, white)

        //After a lot of looking, this level attribute goes from 0 to 10000 (0% to 100%) In this case on fromDegrees 0 to toDegrees 360
        rTre.level = currentAngle.toInt() * 28
        rTre.draw(canvas)

        //val dm = DisplayMetrics()
        //println("Density of this screen:  ${resources.displayMetrics.density}")
        //println(anim.repeatCount)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val screenDensity = resources.displayMetrics.density

        width = MeasureSpec.getSize(widthMeasureSpec)  //Gets the calculated width of view in pixels
        height = MeasureSpec.getSize(heightMeasureSpec)
        xMiddle = (width!! /2).toFloat()
        yMiddle = (height!! /2).toFloat()

        rTre.setBounds((xMiddle - (50 * screenDensity)).toInt(), (yMiddle - (60 * screenDensity)).toInt(), (xMiddle + (50 * screenDensity)).toInt(), (yMiddle + (40 * screenDensity)).toInt() )

        println("Density of this screen:  ${resources.displayMetrics.density}")
        println("Is this the right width?  ${MeasureSpec.getSize(widthMeasureSpec)}")
    }
}
