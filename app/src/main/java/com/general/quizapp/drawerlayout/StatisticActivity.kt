package com.general.quizapp.drawerlayout

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.general.quizapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_statistic.*

class StatisticActivity : AppCompatActivity() {
    lateinit var firestore: FirebaseFirestore
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var userId:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)
        intent
        back.setOnClickListener {
            onBackPressed()
        }

        firestore= FirebaseFirestore.getInstance()
        firebaseAuth=FirebaseAuth.getInstance()
        userId= firebaseAuth.currentUser?.uid.toString()

        firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener { result ->
                    var awronstatic: String = result.getString("wronganswer").toString()
                    var acorrectstatic: String = result.getString("correctanswer").toString()
                    var ascoresstatic: String = result.getString("scores").toString()
                    userdataname.text=result.getString("username")
                    profilstatic.text=result.getString("firstLetter")
                    profilstatic.setBackgroundColor(Color.parseColor(result.getString("color")))
                    findrank(ascoresstatic)

                    if (!awronstatic.equals("0") || !acorrectstatic.equals("0")) {
                    wrong_statitic.text = awronstatic.substring(0, awronstatic.indexOf("."))
                    correct_statitic.text = acorrectstatic.substring(0, acorrectstatic.indexOf("."))
                    scores.text = ascoresstatic.substring(0, ascoresstatic.indexOf("."))
                    val a: Int = acorrectstatic.substring(0, acorrectstatic.indexOf(".")).toInt()
                    val b: Int = awronstatic.substring(0, awronstatic.indexOf(".")).toInt()
                    val c: Int = a + b
                    var scoreint= ascoresstatic.substring(0, ascoresstatic.indexOf(".")).toInt()
                        findlevel(scoreint)
                        score_sum_statitic.text = c.toString()
                        if (a==0 && b==0){
                            percentage_static_wrong.text= 0.toString()
                            percentage_static_correct.text=0.toString()
                        }
                        else{
                            var d: Int =((a.toDouble() / c) * 100).toInt()
                            percentage_static_correct.text = d.toString() + "%"
                            donutprogress.setDonut_progress(d.toString())
                            var e: Int = 100 - d
                            percentage_static_wrong.text = e.toString()+"%"
                        }

                    }
                }
                }

    private fun findlevel(ascoresstatic: Int) {
      var documentReference:DocumentReference=firestore.collection("users").document(userId)
        var user=HashMap<String,String>()
        firestore.collection("users").document(userId).get()
                .addOnSuccessListener {result->
                    var levelstring=result.getString("level")
                    var levelint=levelstring?.substring(0,levelstring.indexOf("."))?.toInt()
                    var leveldivide_int=levelint?.times(10)
                    if(leveldivide_int?.compareTo(ascoresstatic)!=1){
                        levelint=levelint?.plus(1)
                        user.put("level",levelint.toString()+".0")
                        documentReference.update(user as Map<String, Any>)
                    }
                    level.text=levelint.toString()
                }

    }

    private fun findrank(ascoresstatic: String) {
        var listleaders= arrayListOf<Int>()
        firestore.collection("users").addSnapshotListener { querySnapsots, firestoresnapsot ->

            for (i in 0 until querySnapsots?.documents?.size!!) {
                var stscores = querySnapsots.documents[i]?.getString("scores").toString()
                var intscores= stscores.substring(0, stscores.indexOf(".")).toInt()
                listleaders.add(intscores)
            }
            listleaders.sortDescending()
            var scorerank:String
            if (!listleaders.isEmpty()) {
                for (i in 0..listleaders.size - 1) {
                    scorerank = listleaders[i].toString() + ".0"
                    if (ascoresstatic.equals(scorerank)) {
                        rank.text = (i + 1).toString()
                        break
                    }
                }
            }

        }
    }
}