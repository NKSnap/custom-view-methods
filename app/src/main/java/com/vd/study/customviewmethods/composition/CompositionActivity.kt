package com.vd.study.customviewmethods.composition

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vd.study.customviewmethods.databinding.ActivityCompositionBinding

class CompositionActivity : AppCompatActivity() {

    var i = 1

    private lateinit var binding: ActivityCompositionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompositionBinding.inflate(layoutInflater, null, false).also {
            setContentView(it.root)
        }

        binding.customView.onButtonClickListener {
            when(it) {
                ButtonsListeners.CONFIRM -> {
                    Toast.makeText(applicationContext, "button clicked", Toast.LENGTH_SHORT).show()
                }
            }
        }

        if (i == 1) {
            binding.customView.buttonColor = Color.GREEN
            i++
        }
    }
}