package com.general.quizapp.logun

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.general.quizapp.MainActivity
import com.general.quizapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*
import kotlin.collections.HashMap

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) 
        connectfirebase()
        intent
    }

   fun connectfirebase() {
       val firebaseauth: FirebaseAuth = FirebaseAuth.getInstance()
       val firestore: FirebaseFirestore= FirebaseFirestore.getInstance()
       var userID:String
       if (firebaseauth.currentUser != null) {
           var intent = Intent(this, MainActivity::class.java)
           startActivity(intent)

       }
       register.setOnClickListener {
           var emails: String = email.text.toString().trim()
           var passwords: String = password.text.toString().trim()
           var username:String=username.text.toString().trim()

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
           progress.visibility= View.VISIBLE
           // register with firebase
           firebaseauth.createUserWithEmailAndPassword(emails, passwords)
               .addOnCompleteListener { task ->
                   if (task.isSuccessful) {
                       userID= firebaseauth.currentUser?.uid.toString()
                       var documentReference:DocumentReference=firestore.collection("users").document(userID)
                       var user= HashMap<String,String>()
                       user.put("username",username)
                       documentReference.set(user).addOnSuccessListener { void ->
                           Log.d("TAG","User profile is created succesfully"+userID)
                       }.addOnFailureListener{exception ->

                           Log.d("TAG","Error!"+exception.message)
                       }
                       startActivity(Intent(this, MainActivity::class.java))
                       finish()
                   }else {
                       Toast.makeText(
                           applicationContext,
                           "Error!" + task.exception?.message,
                           Toast.LENGTH_LONG
                       ).show()
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

