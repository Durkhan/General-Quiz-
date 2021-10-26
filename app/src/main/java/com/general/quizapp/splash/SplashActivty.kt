package com.general.quizapp.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.general.quizapp.MainActivity
import com.general.quizapp.R
import com.general.quizapp.logun.RegisterActivity
import com.general.quizapp.prefence.MyPrefence
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_splash_activty.*

class SplashActivty : AppCompatActivity() {
    lateinit var handler:Handler
    lateinit var animation: Animation
    lateinit var prefence: MyPrefence
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_activty)
        val firebaseauth: FirebaseAuth = FirebaseAuth.getInstance()
        prefence= MyPrefence(this@SplashActivty)
        animation=AnimationUtils.loadAnimation(this, R.anim.logoanim)
        logo.startAnimation(animation)
        handler= Handler()
        handler.postDelayed({
            if (firebaseauth.currentUser != null && intent.getStringExtra("register").equals(null) && firebaseauth.currentUser?.isEmailVerified!! && !prefence.getString("login").equals("logout")) {
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }
            else{
                var intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        },3000)

    }
}