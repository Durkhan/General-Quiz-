package com.general.quizapp

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.leaders.view.*

class LeadersAdabter(val context: Context,var listleaders: ArrayList<Int>, var username1: String, var username2: String, var username3: String):RecyclerView.Adapter<LeadersAdabter.ViewHolder>() {
    lateinit var firestore: FirebaseFirestore
    var holdernusernames:String?=null
    var enternameslist= arrayListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadersAdabter.ViewHolder {
        var view=LayoutInflater.from(context).inflate(R.layout.leaders,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeadersAdabter.ViewHolder, position: Int) {

        firestore= FirebaseFirestore.getInstance()

            firestore.collection("users").whereEqualTo("scores",listleaders.get(position+3).toString()+".0")
                    .get()
                    .addOnCompleteListener { result->
                        if (result.isSuccessful){
                            for (documentSnapshot in result.getResult()!!) {
                                if (username2!=documentSnapshot.getString("username").toString() && username1!=documentSnapshot.getString("username").toString() &&
                                        username3!=documentSnapshot.getString("username").toString() &&
                                        !enternameslist.contains(documentSnapshot.getString("username").toString())){

                                    holder.leadersAdabterusername.text =documentSnapshot.getString("username").toString()
                                    holder.leadersAdabsteruserpoint.text = listleaders.get(position+3).toString()
                                    holder.leadersAdabteruserfirstletter.text = documentSnapshot.getString("firstLetter")
                                    if ((position+4)==10){
                                        holder.leadersAdabteranumber.text =(position + 4).toString()}
                                    else{
                                    holder.leadersAdabteranumber.text ="  "+(position + 4).toString()}
                                    holder.leadersAdabteruserfirstletter.setBackgroundColor(Color.parseColor(documentSnapshot.getString("color")))
                                }



                            }
                            holdernusernames= holder.leadersAdabterusername.text.toString()
                            enternameslist.add(holdernusernames.toString())
                        }

                    }




    }
    override fun getItemCount(): Int {
        if (listleaders.size<=10){
            return listleaders.size-3
        }
        else
            return 7
    }





    class ViewHolder(view:View): RecyclerView.ViewHolder(view) {
              var leadersAdabterusername=view.leadernamerecycle
              var leadersAdabsteruserpoint=view.pointleaderreycle
              var leadersAdabteruserfirstletter=view.leaderrecycle
              var leadersAdabteranumber=view.anumber
    }


}