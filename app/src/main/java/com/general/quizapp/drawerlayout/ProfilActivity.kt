package com.general.quizapp.drawerlayout

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.general.quizapp.MainActivity
import com.general.quizapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profil.*
import kotlinx.android.synthetic.main.activity_register.*

class ProfilActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    lateinit var userid:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        intent
        firebaseAuth=FirebaseAuth.getInstance()
        firestore= FirebaseFirestore.getInstance()
        userid=firebaseAuth.currentUser?.uid.toString()
        var documentReference:DocumentReference=firestore.collection("users").document(userid)
        backmain.setOnClickListener {
            var intent=Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        var editString:String?=null
        editname.setOnClickListener {
            profilname.visibility=View.GONE
            editname.visibility=View.GONE
            editsave.visibility=View.VISIBLE
            profilnameedit.visibility=View.VISIBLE
            editString= profilname.text.toString()
            profilnameedit.setText(editString)
        }

        var usernamelist= arrayListOf<String>()

        firestore.collection("users").addSnapshotListener { snapsot, firestrosnapsot ->
            for (i in 0 until snapsot?.documents?.size!!) {
                var usernamefromdata: String = snapsot.documents[i]?.getString("username").toString()
                usernamelist.add(usernamefromdata)

            }


        }


            editsave.setOnClickListener {
                usernamelist.remove(profilname.text.toString())
            var username:String=profilnameedit.text.toString()

                if (usernamelist.contains(username)){
                    profilnameedit.setError("Username has token")
                    return@setOnClickListener
                }
            if (TextUtils.isEmpty(username)){
                profilnameedit.setError("Username must be not empty")
                return@setOnClickListener
            }

            if (username.length<3){
                profilnameedit.setError("Username must be more than 3 characters")
                return@setOnClickListener
            }
            if (username.length>15){
                profilnameedit.setError("Username must be less than 15 characters")
                return@setOnClickListener
            }
            if (username==editString){
                Toast.makeText(applicationContext,"Username didn't changed",Toast.LENGTH_LONG).show()
                profilname.visibility=View.VISIBLE
                editname.visibility=View.VISIBLE
                editsave.visibility=View.GONE
                profilnameedit.visibility=View.GONE
            }
            else{
            profilname.visibility=View.VISIBLE
            editname.visibility=View.VISIBLE
            editsave.visibility=View.GONE
            profilnameedit.visibility=View.GONE
            var user= HashMap<String,String>()
            firestore.collection("users").document(userid).get().addOnSuccessListener { result->
                user.put("username",profilnameedit.text.toString())
                user.put("firstLetter",profilnameedit.text.substring(0,1).toUpperCase())
                profilname.setText(profilnameedit.text.toString())
                profil_activity.text=profilnameedit.text.substring(0,1).toUpperCase()
                documentReference.update(user as Map<String, Any>)
                Toast.makeText(applicationContext,"Username changed",Toast.LENGTH_LONG).show()
            }
        }}
        editpasword.setOnClickListener {
            editpasword.visibility=View.GONE
            password_activty.visibility=View.GONE
            profilpaswordedit.visibility=View.VISIBLE
            confirmpasword.visibility=View.VISIBLE

        }
        confirmpasword.setOnClickListener {
            editpasword.visibility=View.GONE
            password_activty.visibility=View.GONE

            var editPasword:String=password_activty.text.toString()
            if (editPasword==profilpaswordedit.text.toString()){
                profilpaswordedit.visibility=View.GONE
                confirmpasword.visibility=View.GONE
                enternewpasword.visibility=View.VISIBLE
                savenewpasword.visibility=View.VISIBLE

            }
            else{
                Toast.makeText(applicationContext,"This password is not current password",Toast.LENGTH_LONG).show()
            }
        }
        savenewpasword.setOnClickListener {
            profilpaswordedit.visibility=View.GONE
            confirmpasword.visibility=View.GONE
            var passwords: String = enternewpasword.text.toString().trim()

            if (TextUtils.isEmpty(passwords)) {
                enternewpasword.setError("Password is required")
                return@setOnClickListener
            }
            if (passwords.length < 6) {
                enternewpasword.setError("Password must be more than 6 characters")
                return@setOnClickListener
            }
            if (profilpaswordedit.text.toString()==enternewpasword.text.toString()){
                enternewpasword.setError("This password is current password")
                return@setOnClickListener
            }
            else{
                editpasword.visibility=View.VISIBLE
                password_activty.visibility=View.VISIBLE
                savenewpasword.visibility=View.GONE
                enternewpasword.visibility=View.GONE
                profilpaswordedit.setText("")
                enternewpasword.setText("")
                var user= HashMap<String,String>()
                    firestore.collection("users").document(userid).get().addOnSuccessListener { result->
                    user.put("password",passwords)
                    password_activty.setText(passwords)
                        firebaseAuth.currentUser?.updatePassword(passwords)?.addOnCompleteListener {task->
                            if (task.isSuccessful){
                                Log.d("TAG","Password saved");
                            }
                        }
                    documentReference.update(user as Map<String, Any>)
                    Toast.makeText(applicationContext,"Password changed",Toast.LENGTH_LONG).show()
                }
            }

        }
        firestore.collection("users").document(userid).get().addOnSuccessListener { result->
            profil_activity.text=result.getString("firstLetter")
            profil_activity.setBackgroundColor(Color.parseColor(result.getString("color")))
            profilemail.text=result.getString("email")
            profilname.text=result.getString("username")
            password_activty.text=result.getString("password")
        }
    }

    override fun onBackPressed() {
    }
}