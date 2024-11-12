package com.example.sistemaespecialistamob

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiRepository {

    // Chamada para obter uma questão
    fun getQuestion(perfil: String, num: Int, onResult: (String?) -> Unit) {
        val call = RetrofitInstance.api.getQuestion(perfil, num)
        call.enqueue(object : Callback<QuestionResponse> {
            override fun onResponse(call: Call<QuestionResponse>, response: Response<QuestionResponse>) {
                if (response.isSuccessful) {
                    val question = response.body()?.question
                    onResult(question)
                } else {
                    onResult(null) // Ou trate o erro de forma específica
                }
            }

            override fun onFailure(call: Call<QuestionResponse>, t: Throwable) {
                onResult(null) // Pode tratar falhas também aqui
            }
        })
    }

    // Chamada para calcular o diagnóstico
    fun scoreCalc(perfil: String, answers: List<Int>, onResult: (String?) -> Unit) {
        val call = RetrofitInstance.api.scoreCalc(perfil, answers)
        call.enqueue(object : Callback<DiagnosticResponse> {
            override fun onResponse(call: Call<DiagnosticResponse>, response: Response<DiagnosticResponse>) {
                if (response.isSuccessful) {
                    val diagnostic = response.body()?.diagnostic
                    onResult(diagnostic)
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<DiagnosticResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }

    // Chamada para calcular diagnósticos completos
    fun completeScoreCalc(answers: Map<String, List<Int>>, onResult: (Map<String, String>?) -> Unit) {
        val call = RetrofitInstance.api.completeScoreCalc(answers)
        call.enqueue(object : Callback<CompleteScoreResponse> {
            override fun onResponse(call: Call<CompleteScoreResponse>, response: Response<CompleteScoreResponse>) {
                if (response.isSuccessful) {
                    val results = response.body()?.results
                    onResult(results)
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<CompleteScoreResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }
}
