package com.general.quizapp.logun

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.general.quizapp.MainActivity
import com.general.quizapp.prefence.MyPrefence
import com.general.quizapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*
import kotlin.collections.HashMap


class RegisterActivity : AppCompatActivity() {
    lateinit var prefence: MyPrefence
    lateinit var firebaseUser: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        connectfirebase()
        intent
    }
    override fun onBackPressed(){
        val main= Intent(Intent.ACTION_MAIN)
        main.addCategory(Intent.CATEGORY_HOME)
        main.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(main)

    }

    fun connectfirebase() {
        val firebaseauth: FirebaseAuth = FirebaseAuth.getInstance()
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        prefence= MyPrefence(this@RegisterActivity)
        var userID: String
        if (firebaseauth.currentUser != null && intent.getStringExtra("register").equals(null) && firebaseauth.currentUser?.isEmailVerified!! && !prefence.getString("login").equals("logout")) {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
        register.setOnClickListener {
            var emails: String = email.text.toString().trim()
            var passwords: String = password.text.toString().trim()
            var usernames: String = username.text.toString().trim()

            if (TextUtils.isEmpty(emails)) {
                email.setError("Email is required")
                return@setOnClickListener
            }
            if (usernames.length<3){
                username.setError("Username must be more than 3 characters")
                return@setOnClickListener
            }
            if (usernames.length>15){
                username.setError("Username must be less than 15 characters")
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
            progress.visibility = View.VISIBLE
            // register with firebase
            firebaseauth.createUserWithEmailAndPassword(emails, passwords)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            firebaseUser = firebaseauth.currentUser!!
                            if (!firebaseUser?.isEmailVerified()!!) {
                                firebaseUser?.sendEmailVerification()
                                        ?.addOnCompleteListener(this,
                                                OnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        Toast.makeText(
                                                                applicationContext,
                                                                "Verification link email sent to " + firebaseUser.getEmail(),
                                                                Toast.LENGTH_SHORT
                                                        ).show()
                                                        startActivity(Intent(this, LoginActivity::class.java))
                                                        finish()
                                                    } else {
                                                        Toast.makeText(
                                                                applicationContext,
                                                                "Failed to send verification email.",
                                                                Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                })
                            } else {
                                Toast.makeText(
                                        applicationContext,
                                        "Error!" + task.exception?.message,
                                        Toast.LENGTH_LONG
                                ).show()
                                progress.visibility = View.GONE
                            }

                                var colorList= arrayListOf("#B3E9F811","#0FDA18","#E68E24AA","#80E53935","#B3F4511E","#43A047","#3949AB","#FFB300","#00897B","#FF5C6E4A","#98928A","#E3BB86")
                                var color=colorList.random()
                                var firstLetter=usernames.substring(0,1).toUpperCase()
                                userID = firebaseauth.currentUser?.uid.toString()
                                var documentReference: DocumentReference = firestore.collection("users").document(userID)
                                var user = HashMap<String, String>()
                                user.put("username", usernames)
                                user.put("email",emails)
                                user.put("password",passwords)
                                user.put("scores","0.0")
                                user.put("correctanswer","0.0")
                                user.put("wronganswer","0.0")
                                user.put("color",color)
                                user.put("firstLetter",firstLetter)
                                user.put("level","1.0")
                                documentReference.set(user).addOnSuccessListener { void ->
                                    Log.d("TAG", "User profile is created succesfully" + userID)
                                }.addOnFailureListener { exception ->

                                    Log.d("TAG", "Error!" + exception.message)
                                }


                        }
                        else{
                            Toast.makeText(applicationContext,"Error!"+task.exception?.message,Toast.LENGTH_LONG).show()
                            progress.visibility=View.GONE
                        }
                    }

        }
        returnlogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}







