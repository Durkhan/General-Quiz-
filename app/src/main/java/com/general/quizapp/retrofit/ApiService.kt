package com.general.quizapp.retrofit

import com.general.quizapp.modell.Response
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
   @GET("api.php?")
    fun quizquestions(@Query("amount") s:Int,@Query("difficulty") difficulty:String,@Query("category") category:Int,@Query("type") type:String):Observable<Response>
}