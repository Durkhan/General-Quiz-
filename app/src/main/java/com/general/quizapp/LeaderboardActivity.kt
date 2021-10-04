package com.general.quizapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_leaderboard.*

class  LeaderboardActivity : AppCompatActivity() {
    lateinit var firestore: FirebaseFirestore
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var leadersAdabter: LeadersAdabter
    lateinit var prefence: MyPrefence
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        var userId:String
        intent
        userId= firebaseAuth.currentUser?.uid.toString()
        firestore.collection("users").document(userId).get()
                .addOnSuccessListener { result->
                        profilleader.text=result.getString("firstLetter")
                        profilleader.setBackgroundColor(Color.parseColor(result.getString("color")))

                }
        setleaderslist()
       backpress.setOnClickListener {
           onBackPressed()
       }

    }


    private fun setleaderslist() {

        var listleaders= arrayListOf<Int>()
        firestore.collection("users").addSnapshotListener { querySnapsots, firestoresnapsot ->

            for(i in 0 until querySnapsots?.documents?.size!!){
                var stscores=querySnapsots.documents[i]?.getString("scores")
                var intscores=stscores?.substring(0,stscores.indexOf("."))?.toInt()
                if (intscores != null) {
                    listleaders.add(intscores)
                }

            }
            listleaders.sortDescending()
            if (!listleaders.isEmpty() && listleaders.size>=3) {
                leaderscore1.text = listleaders.get(0).toString()
                leaderscore2.text = listleaders.get(1).toString()
                leaderscore3.text = listleaders.get(2).toString()
                firestore.collection("users").whereEqualTo("scores", listleaders.get(0).toString() + ".0")
                        .get()
                        .addOnCompleteListener { result ->
                            if (result.isSuccessful) {

                                for (documentSnapshot in result.getResult()!!) {
                                    leaderusrname1.text = documentSnapshot.getString("username").toString()
                                    leader1.text = documentSnapshot.getString("firstLetter").toString()
                                    leader1.setBackgroundColor(Color.parseColor(documentSnapshot.getString("color")))
                                }


                            }

                        }
                firestore.collection("users").whereEqualTo("scores", listleaders.get(1).toString() + ".0")
                        .get()
                        .addOnCompleteListener { result ->
                            if (result.isSuccessful) {
                                for (documentSnapshot in result.getResult()!!) {
                                    if (leaderusrname1.text != documentSnapshot.getString("username")) {
                                        leaderusrname2.text = documentSnapshot.getString("username").toString()
                                        leader2.text = documentSnapshot.getString("firstLetter").toString()
                                        leader2.setBackgroundColor(Color.parseColor(documentSnapshot.getString("color")))
                                    }
                                }
                            }


                        }
                firestore.collection("users").whereEqualTo("scores", listleaders.get(2).toString() + ".0")
                        .get()
                        .addOnCompleteListener { result ->
                            if (result.isSuccessful) {

                                for (documentSnapshot in result.getResult()!!) {
                                    if (leaderusrname1.text != documentSnapshot.getString("username") && leaderusrname2.text != documentSnapshot.getString("username")) {
                                        leaderusrname3.text = documentSnapshot.getString("username").toString()
                                        leader3.text = documentSnapshot.getString("firstLetter").toString()
                                        leader3.setBackgroundColor(Color.parseColor(documentSnapshot.getString("color")))
                                    }
                                }
                                if (!leaderusrname1.text.isEmpty() && !leaderusrname2.text.isEmpty() && !leaderusrname3.text.isEmpty()) {
                                    prefence = MyPrefence(this@LeaderboardActivity, )
                                    prefence.setString("username1", leaderusrname1.text.toString())
                                    prefence.setString("username2", leaderusrname2.text.toString())
                                    prefence.setString("username3", leaderusrname3.text.toString())
                                    leaders.layoutManager = LinearLayoutManager(this)
                                    leadersAdabter = LeadersAdabter(this, listleaders, leaderusrname1.text.toString(), leaderusrname2.text.toString(), leaderusrname3.text.toString())
                                    leaders.adapter = leadersAdabter
                                    leadersAdabter.notifyDataSetChanged()
                                }
                            }

                        }
            }

        }



    }

}

