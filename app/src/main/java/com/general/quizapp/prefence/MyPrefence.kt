package com.general.quizapp.prefence

import android.content.Context

class MyPrefence(context: Context) {
    private var SHARD_PREFENCE="Shared"

    val prefence=context.getSharedPreferences(SHARD_PREFENCE,Context.MODE_PRIVATE)

    fun getBoolen(key:String):Boolean{
        return prefence.getBoolean(key,true)
    }
    fun setBoolen(key:String,bl:Boolean){
        val editor=prefence.edit()
        editor.putBoolean(key,bl)
        editor.apply()
    }
    fun getString(key:String): String? {
        return prefence.getString(key,null)
    }
    fun setString(key:String,st:String){
        val editor=prefence.edit()
        editor.putString(key,st)
        editor.apply()
    }

}