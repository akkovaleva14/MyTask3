package com.example.task3

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val animatedButton: AnimatedButtonView = findViewById(R.id.animatedButton)
        val redButton1: View = findViewById(R.id.redButton1)
        val redButton2: View = findViewById(R.id.redButton2)

        animatedButton.onButtonClick = { isExpanded ->
            if (isExpanded) {
                // Анимация исчезновения
                animateRedButtons(redButton1, redButton2, hide = true) {
                    redButton1.visibility = View.GONE
                    redButton2.visibility = View.GONE
                }
            } else {
                // Подготовка: убедимся, что кнопки видимы
                redButton1.translationY = animatedButton.height.toFloat()
                redButton2.translationY = animatedButton.height.toFloat()
                redButton1.alpha = 0f
                redButton2.alpha = 0f
                redButton1.visibility = View.VISIBLE
                redButton2.visibility = View.VISIBLE

                // Анимация появления
                animateRedButtons(redButton1, redButton2, hide = false, onEnd = null)
            }
        }
    }

    private fun animateRedButtons(
        button1: View,
        button2: View,
        hide: Boolean,
        onEnd: (() -> Unit)?
    ) {
        val buttonHeight = button1.height.toFloat()
        val targetTranslationY1 = if (hide) buttonHeight else 0f
        val targetTranslationY2 = if (hide) buttonHeight + 50f else 50f

        button1.animate()
            .translationY(targetTranslationY1)
            .alpha(if (hide) 0f else 1f)
            .setDuration(300)
            .withEndAction { onEnd?.invoke() }
            .start()

        button2.animate()
            .translationY(targetTranslationY2)
            .alpha(if (hide) 0f else 1f)
            .setDuration(300)
            .start()
    }
}