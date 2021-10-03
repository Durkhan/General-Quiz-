package com.general.quizapp

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_result_activty.*


class ResultActivty : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    lateinit var firebaseUser: FirebaseUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_activty)
        intent
        score.text=intent.getStringExtra("score").toString()
        win.text=intent.getStringExtra("won").toString()
        correct.text=intent.getStringExtra("correct").toString()
        wrong.text=intent.getStringExtra("wrong").toString()
        lost.text=intent.getStringExtra("lost").toString()
        addscoretodata()
        exitapp.setOnClickListener {
            val main= Intent(Intent.ACTION_MAIN)
            main.addCategory(Intent.CATEGORY_HOME)
            main.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(main)
        }
        back.setOnClickListener {
            onBackPressed()
        }
        leader.setOnClickListener {
            var intent=Intent(this,LeaderboardActivity::class.java)
            startActivity(intent)
        }
        statistic.setOnClickListener {
            var intent=Intent(this,StatisticActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        var intentback=Intent(this,MainActivity::class.java)
        startActivity(intentback)
    }
    private fun addscoretodata() {
        var results:String
        var corrects:String
        var wrongs:String
        results=score.text.toString()
        wrongs=wrong.text.toString()
        corrects=correct.text.toString()
        firebaseAuth=FirebaseAuth.getInstance()
        firestore= FirebaseFirestore.getInstance()
        var userId:String
        userId=firebaseAuth.currentUser?.uid.toString()
        var documentReference: DocumentReference =firestore.collection("users").document(userId)
        var user=HashMap<String,String>()
        firestore.collection("users").document(userId).get().addOnSuccessListener {result->
            var mresults=results.toFloat()
            var nresults=result.getString("scores")!!.toFloat()
            var csum=nresults+mresults
            var mcorrects=corrects.toFloat()
            var ncorrects=result.getString("correctanswer")!!.toFloat()
            var csumcorect=ncorrects+mcorrects
            var mwrongs=wrongs.toFloat()
            var nwrongs=result.getString("wronganswer")!!.toFloat()
            var csumwrongs=mwrongs+nwrongs
            user.put("scores",csum.toString())
            user.put("correctanswer",csumcorect.toString())
            user.put("wronganswer",csumwrongs.toString())

            documentReference.update(user as Map<String, String>)


        }

    }
}


