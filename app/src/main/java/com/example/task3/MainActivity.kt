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
        setupAnimatedButton()
    }

    private fun setupAnimatedButton() {
        val animatedButton = binding.animatedButton
        val redButton1 = binding.redButton1
        val redButton2 = binding.redButton2

        animatedButton.onButtonClick = { isExpanded ->
            if (isExpanded) {
                animateRedButtons(redButton1, redButton2, hide = true)
            } else {
                prepareRedButtonsForAnimation(
                    translationY = animatedButton.height.toFloat(),
                    redButton1,
                    redButton2
                )
                animateRedButtons(redButton1, redButton2, hide = false)
            }
        }
    }

    private fun prepareRedButtonsForAnimation(translationY: Float, vararg buttons: View) {
        buttons.forEach { button ->
            button.translationY = translationY
            button.alpha = 0f
            button.visibility = View.VISIBLE
        }
    }

    private fun animateRedButtons(button1: View, button2: View, hide: Boolean) {
        animateView(
            view = button1,
            translationY = if (hide) button1.height.toFloat() else 0f,
            alpha = if (hide) 0f else 1f
        )
        animateView(
            view = button2,
            translationY = if (hide) button1.height.toFloat() + 50f else 50f,
            alpha = if (hide) 0f else 1f
        )
    }

    private fun animateView(
        view: View,
        translationY: Float,
        alpha: Float,
        duration: Long = 300
    ) {
        view.animate()
            .translationY(translationY)
            .alpha(alpha)
            .setDuration(duration)
            .start()
    }
}