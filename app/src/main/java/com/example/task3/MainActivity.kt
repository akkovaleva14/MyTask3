package com.example.task3

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.task3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val animatedButton = binding.animatedButton
        val redButton1 = binding.redButton1
        val redButton2 = binding.redButton2

        animatedButton.onButtonClick = { isExpanded ->
            if (isExpanded) {
                // Анимация исчезновения
                animateRedButtons(redButton1, redButton2, hide = true)
            } else {
                // Подготовка: убедимся, что кнопки видимы
                redButton1.translationY = animatedButton.height.toFloat()
                redButton2.translationY = animatedButton.height.toFloat()
                redButton1.alpha = 0f
                redButton2.alpha = 0f
                redButton1.visibility = View.VISIBLE
                redButton2.visibility = View.VISIBLE

                // Анимация появления
                animateRedButtons(redButton1, redButton2, hide = false)
            }
        }
    }

    private fun animateRedButtons(
        button1: View,
        button2: View,
        hide: Boolean
    ) {
        button1.animate()
            .translationY(if (hide) button1.height.toFloat() else 0f)
            .alpha(if (hide) 0f else 1f)
            .setDuration(300)
            .start()

        button2.animate()
            .translationY(if (hide) button1.height.toFloat() + 50f else 50f)
            .alpha(if (hide) 0f else 1f)
            .setDuration(300)
            .start()
    }
}