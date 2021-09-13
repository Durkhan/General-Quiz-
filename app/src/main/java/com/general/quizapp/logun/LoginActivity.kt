package com.general.quizapp.logun

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.general.quizapp.MainActivity
import com.general.quizapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.resetpasworddialog.*
import kotlinx.android.synthetic.main.resetpasworddialog.view.*



class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        var firebaseAuth:FirebaseAuth= FirebaseAuth.getInstance()
        login.setOnClickListener {

            var emails: String = email.text.toString().trim()
            var passwords: String = password.text.toString().trim()

            if (TextUtils.isEmpty(emails)) {
                email.setError("Email is required")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(passwords)) {
                password.setError("Password is required")
                return@setOnClickListener
            }
            if (passwords.length < 6) {
                password.setError("Password must be more than 6 characters")
                return@setOnClickListener
            }
            progressbar.visibility=View.VISIBLE

            //login firebase
            firebaseAuth.signInWithEmailAndPassword(emails, passwords).addOnCompleteListener { task ->
                if (task.isSuccessful){
                   startActivity(Intent(this, MainActivity::class.java))
                }
                else{
                    Toast.makeText(applicationContext, "Error!" + task.exception?.message, Toast.LENGTH_LONG).show()
                    progressbar.visibility=View.GONE
                }
            }

        }
        signup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        forgotpassword.setOnClickListener {
            val inflater = LayoutInflater.from(this@LoginActivity)
            var view=inflater.inflate(R.layout.resetpasworddialog, null)
          view.send.setAllCaps(false)
            view.send.setOnClickListener {
                var edittextemails:String=view.emailresetpassword.text.toString()
                if (TextUtils.isEmpty(edittextemails)){
                    view.emailresetpassword.emailresetpassword.setError("Email does not entered")
                    return@setOnClickListener
                }
                firebaseAuth.sendPasswordResetEmail(edittextemails).addOnSuccessListener {void ->
                    Toast.makeText(this,"Check your Email.Reset link sent",Toast.LENGTH_LONG).show()
                }.addOnFailureListener{ exception ->
                    Toast.makeText(this,"Error"+exception.message,Toast.LENGTH_LONG).show()
                }
            }
         view.dialogreturnregister.setOnClickListener {
             startActivity(Intent(this,RegisterActivity::class.java))
             finish()
         }
            val alertDialog: AlertDialog = AlertDialog.Builder(this@LoginActivity)
                    .setView(view)
                    .create()
            alertDialog.show()
        }
    }
}