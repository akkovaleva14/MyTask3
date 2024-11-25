package com.example.task3

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat

class AnimatedButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var isExpanded = true
    var onButtonClick: ((Boolean) -> Unit)? = null

    private val rectPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, android.R.color.holo_blue_light)
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, android.R.color.white)
        textSize = 50f
        textAlign = Paint.Align.CENTER
    }

    private val rect = RectF()
    private var cornerRadius = 0f
    private var textAlpha = 255
    private var currentWidth = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val desiredWidth = 300
        val desiredHeight = 120

        val width = resolveSize(desiredWidth, widthMeasureSpec)
        val height = resolveSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(width, height)

        // Устанавливаем начальные параметры кнопки
        currentWidth = width.toFloat()
        cornerRadius = height / 2f // Радиус закругления — половина высоты
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Рисуем синюю кнопку
        rect.set(
            (width - currentWidth) / 2,
            0f,
            (width + currentWidth) / 2,
            height.toFloat()
        )
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, rectPaint)

        // Рисуем текст
        textPaint.alpha = textAlpha
        val text = if (isExpanded) "Button" else "X"
        canvas.drawText(
            text,
            width / 2f,
            height / 2f - (textPaint.descent() + textPaint.ascent()) / 2,
            textPaint
        )
    }

    private fun toggleState() {
        Log.d("AnimatedButtonView", "toggleState: Starting toggle. isExpanded=$isExpanded")

        // Меняем состояние кнопки
        isExpanded = !isExpanded

        // Анимация кнопки (ширина, радиус, текст)
        val widthAnimator = ValueAnimator.ofFloat(
            if (isExpanded) height.toFloat() else width.toFloat(),
            if (isExpanded) width.toFloat() else height.toFloat()
        )
        val cornerAnimator = ValueAnimator.ofFloat(
            if (isExpanded) cornerRadius else height / 2f,
            if (isExpanded) height / 2f else height / 2f
        )

        val alphaAnimator = ValueAnimator.ofInt(255, 255)

        widthAnimator.addUpdateListener {
            currentWidth = it.animatedValue as Float
            invalidate()
        }
        cornerAnimator.addUpdateListener {
            // Никогда не опускаем cornerRadius ниже 10% высоты кнопки
            val animatedValue = it.animatedValue as Float
            cornerRadius = animatedValue.coerceAtLeast(height * 0.1f)
            invalidate()
        }
        alphaAnimator.addUpdateListener {
            textAlpha = it.animatedValue as Int
            invalidate()
        }

        // Запуск анимации
        val animationDuration = 300L
        widthAnimator.duration = animationDuration
        cornerAnimator.duration = animationDuration
        alphaAnimator.duration = animationDuration
        widthAnimator.start()
        cornerAnimator.start()
        alphaAnimator.start()

        onButtonClick?.invoke(isExpanded)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            Log.d("AnimatedButtonView", "onTouchEvent: ACTION_DOWN detected, toggling state")
            toggleState()
            return true
        }
        return super.onTouchEvent(event)
    }
}
