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
    private var areRedButtonsVisible = false
    var onButtonClick: ((Boolean) -> Unit)? = null

    private val rectPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, android.R.color.holo_blue_light)
    }

    private val redButtonPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, android.R.color.holo_red_light)
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, android.R.color.white)
        textSize = 50f
        textAlign = Paint.Align.CENTER
    }

    private val rect = RectF()

    //    private var cornerRadius = 50f
    private var cornerRadius = 0f
    private var textAlpha = 255
    private var currentWidth = 0f
    private var redButtonOffset = 0f // Смещение красных кнопок

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        currentWidth = w.toFloat()
        cornerRadius = h / 2f // Устанавливаем радиус закругления как половину высоты
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

        // Рисуем красные кнопки, если они видимы
        if (areRedButtonsVisible) {
            val redButtonWidth = 100f
            val redButtonHeight = 50f

            // Первая красная кнопка
            canvas.drawRect(
                (width / 2f - redButtonWidth / 2) + redButtonOffset,
                (height + 10).toFloat(),
                (width / 2f + redButtonWidth / 2) + redButtonOffset,
                height + 10 + redButtonHeight,
                redButtonPaint
            )

            // Вторая красная кнопка
            canvas.drawRect(
                (width / 2f - redButtonWidth / 2) + redButtonOffset,
                (height + 70).toFloat(),
                (width / 2f + redButtonWidth / 2) + redButtonOffset,
                height + 70 + redButtonHeight,
                redButtonPaint
            )
        }
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

        // Сообщаем, что нужно переключить красные кнопки
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
