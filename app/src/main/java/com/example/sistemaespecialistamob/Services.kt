package com.example.sistemaespecialistamob

import retrofit2.http.*
import retrofit2.Call

// Mapeando respostas
data class QuestionResponse(val question: String)
data class DiagnosticResponse(val diagnostic: String)
data class CompleteScoreResponse(val results: Map<String, String>)

// Interface da API
interface ApiService {
    @GET("/questions/{perfil}/{num}/")
    fun getQuestion(
        @Path("perfil") perfil: String,
        @Path("num") num: Int
    ): Call<QuestionResponse>

    @POST("/diagnostic/")
    fun scoreCalc(
        @Query("perfil") perfil: String,
        @Body answers: List<Int>
    ): Call<DiagnosticResponse>

    @POST("/diagnostic/complete/")
    fun completeScoreCalc(
        @Body answers: Map<String, List<Int>>
    ): Call<CompleteScoreResponse>
}
