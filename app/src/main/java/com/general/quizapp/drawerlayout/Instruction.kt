package com.general.quizapp.drawerlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.general.quizapp.R
import kotlinx.android.synthetic.main.activity_instruction.*

class Instruction : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instruction)
        intent
        backdrawer.setOnClickListener {
            onBackPressed()
        }
    }
}