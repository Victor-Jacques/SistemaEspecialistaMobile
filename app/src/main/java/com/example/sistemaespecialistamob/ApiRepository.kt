package com.example.sistemaespecialistamob

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiRepository {

    fun fetchQuestion(perfil: String, num: Int, onResult: (String?) -> Unit) {
        Log.d("API", "Iniciando fetchQuestion para perfil: $perfil e número: $num") // Log de depuração
        val call = RetrofitInstance.apiService.getQuestion(perfil, num)
        call.enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    val question = response.body()?.get("question")
                    Log.d("API", "Pergunta recebida: $question") // Log de resposta bem-sucedida
                    onResult(question)
                } else {
                    Log.e("API", "Erro de resposta: ${response.code()}") // Log de erro de resposta
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Log.e("API", "Falha ao buscar a pergunta", t) // Log de falha
                onResult(null)
            }
        })
    }


    fun scoreCalc(perfil: String, answers: List<Int>, onResult: (String?) -> Unit) {
        val call = RetrofitInstance.apiService.scoreCalc(perfil, answers)
        call.enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    val diagnostic = response.body()?.get("diagnostic")
                    onResult(diagnostic)
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun completeScoreCalc(answers: Map<String, List<Int>>, onResult: (Map<String, String>?) -> Unit) {
        val call = RetrofitInstance.apiService.completeScoreCalc(answers)
        call.enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    val results = response.body()
                    onResult(results)
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                onResult(null)
            }
        })
    }
}
