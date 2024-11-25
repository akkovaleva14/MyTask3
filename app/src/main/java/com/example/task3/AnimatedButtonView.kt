package com.example.task3

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat

class AnimatedButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var onButtonClick: ((Boolean) -> Unit)? = null

    private val rectPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, android.R.color.holo_blue_light)
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, android.R.color.white)
        textSize = 50f
        textAlign = Paint.Align.CENTER
    }
    private var isExpanded: Boolean = true
    private var cornerRadius = 0f
    private var currentWidth = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        currentWidth = measuredWidth.toFloat()
        cornerRadius = measuredHeight / 2f
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Рисуем кнопку
        canvas.drawRoundRect(
            (width - currentWidth) / 2,
            0f,
            (width + currentWidth) / 2,
            height.toFloat(), cornerRadius, cornerRadius, rectPaint
        )

        // Рисуем текст
        canvas.drawText(
            if (isExpanded)
                context.getString(R.string.expanded_button_text)
            else
                context.getString(R.string.collapsed_button_text),
            width / 2f,
            height / 2f - (textPaint.descent() + textPaint.ascent()) / 2,
            textPaint
        )
    }

    private fun toggleState() {
        isExpanded = !isExpanded
        animateButton()
        onButtonClick?.invoke(isExpanded)
    }

    private fun animateButton() {
        AnimatorSet()
            .apply {
                playTogether(
                    ValueAnimator.ofFloat(
                        currentWidth,
                        if (isExpanded) width.toFloat() else height.toFloat()
                    ).apply {
                        duration = 300
                        addUpdateListener {
                            currentWidth = it.animatedValue as Float
                            invalidate()
                        }
                    },
                    ValueAnimator.ofFloat(
                        cornerRadius,
                        if (isExpanded) height / 2f else height / 2f
                    ).apply {
                        duration = 300
                        addUpdateListener {
                            cornerRadius = it.animatedValue as Float
                            invalidate()
                        }
                    }
                )
                start()
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Проверяем, попадает ли нажатие в видимую область кнопки
        if (event.action == MotionEvent.ACTION_DOWN &&
            event.x in (width - currentWidth) / 2..(width + currentWidth) / 2 &&
            event.y in 0f..height.toFloat()
        ) {
            toggleState()
            return true
        }
        return false
    }
}
