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
                // Убедимся, что кнопки видимы перед началом анимации
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
        val translationY1 = if (hide) 200f else 0f // Сместить вверх или вниз
        val translationY2 = if (hide) 250f else 0f

        button1.animate()
            .translationY(translationY1)
            .alpha(if (hide) 0f else 1f)
            .setDuration(300)
            .withEndAction {
                onEnd?.invoke()
            }
            .start()

        button2.animate()
            .translationY(translationY2)
            .alpha(if (hide) 0f else 1f)
            .setDuration(300)
            .start()
    }
}
