package com.udacity

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
private var button_text = resources.getString(R.string.button_name)
  private var progress: Double = 0.0
  private var widthSize = 0
    private var heightSize = 0
    private  var valueAnimator = ValueAnimator()

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

      when (new) {
        ButtonState.Loading -> {
          button_text = resources.getString(R.string.button_loading)
          valueAnimator.start()

        }
        ButtonState.Completed -> {
          button_text = resources.getString(R.string.button_name)
          valueAnimator.cancel()

          progress = 0.0
        }

      }

      invalidate()

    }

  private val updateListener = ValueAnimator.AnimatorUpdateListener {
    progress = (it.animatedValue as Float).toDouble()
    invalidate()    // redraw the screen
    requestLayout() // when rectangular progress dimension changes
  }
    init {
      isClickable = true
      valueAnimator = AnimatorInflater.loadAnimator(
        context,
        // properties for downloading progress is defined
        R.animator.loading_animation
      ) as ValueAnimator

      valueAnimator.repeatCount = ValueAnimator.INFINITE
      valueAnimator.repeatMode = ValueAnimator.RESTART
      valueAnimator.addUpdateListener(updateListener)

    }
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.FILL
    textAlign = Paint.Align.CENTER // button text alignment
    textSize = 55.0f //button text size
    typeface = Typeface.create("", Typeface.BOLD) // button text's font style
  }

    override fun onDraw(canvas: Canvas?) {
      super.onDraw(canvas)



      paint.strokeWidth = 0f
      paint.color =Color.GRAY
      canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

      paint.color = Color.WHITE
      canvas?.drawText(button_text, (width / 2).toFloat(), ((height + 30) / 2).toFloat(), paint)

        if (buttonState == ButtonState.Loading) {
          paint.color = Color.BLACK

          canvas?.drawRect(
            0f, 0f,
            (width * (progress / 100)).toFloat(), height.toFloat(), paint
          )
          paint.color = Color.WHITE
          canvas?.drawText(button_text, (width / 2).toFloat(), ((height + 30) / 2).toFloat(), paint)

        }


    }
//  override fun performClick(): Boolean {
//    super.performClick()
//    if (buttonState == ButtonState.Completed )
//      buttonState = ButtonState.Loading
//    //animation()
//
//    return true
//  }
  private fun animation() {
    valueAnimator.start()
  }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}
