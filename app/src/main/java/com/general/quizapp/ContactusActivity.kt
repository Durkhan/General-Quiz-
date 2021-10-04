package com.general.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_contactus.*

class ContactusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contactus)
        openmyemail.setText(R.string.emailunderline)
        backdrawercontact.setOnClickListener {
            onBackPressed()
        }
    }
}