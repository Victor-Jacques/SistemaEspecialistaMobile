package com.example.sistemaespecialistamob

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("questions/{perfil}/{num}/")
    fun getQuestion(
        @Path("perfil") perfil: String,
        @Path("num") num: Int
    ): Call<Map<String, String>>

    @POST("diagnostic/")
    fun scoreCalc(
        @Query("perfil") perfil: String,
        @Body answers: List<Int>
    ): Call<Map<String, String>>

    @POST("diagnostic/complete/")
    fun completeScoreCalc(
        @Body answers: Map<String, List<Int>>
    ): Call<Map<String, String>>
}
