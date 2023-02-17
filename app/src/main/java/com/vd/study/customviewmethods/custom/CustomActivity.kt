package com.vd.study.customviewmethods.custom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vd.study.customviewmethods.databinding.ActivityCustomBinding

class CustomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }
}