package com.general.quizapp.logun

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.util.PatternsCompat
import com.general.quizapp.MainActivity
import com.general.quizapp.prefence.MyPrefence
import com.general.quizapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.email
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.resetpasworddialog.*
import kotlinx.android.synthetic.main.resetpasworddialog.view.*


class LoginActivity : AppCompatActivity(){
    lateinit var prefence: MyPrefence
    lateinit var firebaseAuth:FirebaseAuth
    lateinit var firestore:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        firebaseAuth= FirebaseAuth.getInstance()
        firestore= FirebaseFirestore.getInstance()

        login.setOnClickListener {

            var usernames: String = email.text.toString().trim()
            var passwords: String = password.text.toString().trim()

            if (TextUtils.isEmpty(usernames)) {
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
            if (!PatternsCompat.EMAIL_ADDRESS.matcher(usernames).matches()){
                firestore.collection("users").whereEqualTo("username", usernames)
                        .get()
                        .addOnCompleteListener {task->

                            if (task.isSuccessful){
                                var emailfromdata=""
                                for (documentSnapshot in task.getResult()!!) {
                                    emailfromdata = documentSnapshot.getString("email").toString()
                                }

                                if (emailfromdata!=""){
                                    firestore.collection("users").whereEqualTo("email", emailfromdata)
                                            .get()
                                            .addOnCompleteListener {task->
                                                if (task.isSuccessful){
                                                    firebaseAuth.signInWithEmailAndPassword(emailfromdata, passwords).addOnCompleteListener { task ->
                                                        if (task.isSuccessful){
                                                            if (firebaseAuth.currentUser?.isEmailVerified!!){
                                                                prefence= MyPrefence(this@LoginActivity)
                                                                prefence.setString("login","singin")
                                                                startActivity(Intent(this, MainActivity::class.java))
                                                                 updatepassword(passwords)

                                                            }
                                                            else
                                                                Toast.makeText(applicationContext, "Please verify your email", Toast.LENGTH_LONG).show()
                                                        }

                                                        else{
                                                            Toast.makeText(applicationContext, "Username or password is wrong", Toast.LENGTH_LONG).show()
                                                            progressbar.visibility=View.GONE
                                                        }
                                                    }
                                                }
                                                else{
                                                    Toast.makeText(applicationContext, "Error!" + task.exception?.message, Toast.LENGTH_LONG).show()
                                                    progressbar.visibility=View.GONE
                                                }

                                            }
                                }
                                else{
                                    Toast.makeText(applicationContext, "Username or password is wrong", Toast.LENGTH_LONG).show()
                                    progressbar.visibility=View.GONE
                                }


                            }
                            else{
                                Toast.makeText(applicationContext, "Error!" + task.exception?.message, Toast.LENGTH_LONG).show()
                                progressbar.visibility=View.GONE
                            }
                        }

            }


            else{
                firebaseAuth.signInWithEmailAndPassword(usernames,passwords).addOnCompleteListener {task->
                    if (task.isSuccessful){
                        if (firebaseAuth.currentUser?.isEmailVerified!!) {
                            prefence = MyPrefence(this@LoginActivity)
                            prefence.setString("login", "singin")
                            startActivity(Intent(this, MainActivity::class.java))
                            updatepassword(passwords)

                        }
                        else {
                            Toast.makeText(applicationContext, "Please verify your email", Toast.LENGTH_LONG).show()
                            progressbar.visibility = View.GONE
                        }
                    }
             else {
                        Toast.makeText(applicationContext, "Username or password is wrong", Toast.LENGTH_LONG).show()
                        progressbar.visibility = View.GONE
                    }
                }
            }


        }
        signup.setOnClickListener {
            var intent=Intent(this, RegisterActivity::class.java)
            intent.putExtra("register", "again")
            startActivity(intent)
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
                firebaseAuth.sendPasswordResetEmail(edittextemails).addOnSuccessListener { void ->
                    Toast.makeText(this, "Check your Email.Reset link sent", Toast.LENGTH_LONG).show()
                }.addOnFailureListener{ exception ->
                    Toast.makeText(this, "Error" + exception.message, Toast.LENGTH_LONG).show()
                }
            }
            view.dialogreturnregister.setOnClickListener {
                startActivity(Intent(this, RegisterActivity::class.java))
                finish()
            }
            var alertDialog: AlertDialog = AlertDialog.Builder(this@LoginActivity)
                    .setView(view)
                    .create()
            alertDialog.window?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()
        }
    }

    private fun updatepassword(passwords: String) {
        var userId:String
        userId=firebaseAuth.currentUser?.uid.toString()
        var documentReference: DocumentReference =firestore.collection("users").document(userId)
        var user=HashMap<String,String>()
        firestore.collection("users").document(userId).get().addOnSuccessListener {result->
            user.put("password",passwords)
            documentReference.update(user as Map<String, String>)


        }
    }

    override fun onBackPressed() {
    }
}

