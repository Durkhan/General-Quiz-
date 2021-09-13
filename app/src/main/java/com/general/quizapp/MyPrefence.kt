package com.general.quizapp

import android.content.Context

class MyPrefence(context: Context) {
    private var SHARD_PREFENCE="Shared"

    val prefence=context.getSharedPreferences(SHARD_PREFENCE,Context.MODE_PRIVATE)

    fun getBoolen(key:String):Boolean{
        return prefence.getBoolean(key,false)
    }
    fun setBoolen(key:String,bl:Boolean){
        val editor=prefence.edit()
        editor.putBoolean(key,bl)
        editor.apply()
    }

}