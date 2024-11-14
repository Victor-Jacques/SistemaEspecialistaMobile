package com.example.sistemaespecialistamob

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val resultText = findViewById<TextView>(R.id.resultText)

        // Recupera a String passada
        val resultString = intent.getStringExtra("RESULTADO")

        // Verifica se a String não é nula
        if (!resultString.isNullOrEmpty()) {
            resultText.text = resultString // Exibe o resultado no TextView
        } else {
            resultText.text = "Nenhum resultado disponível" // Caso não haja dado
        }

        // Botão para refazer o teste
        val retryButton = findViewById<Button>(R.id.retryButton)
        retryButton.setOnClickListener {
            // Volta para a tela inicial para refazer o quiz
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Fecha a ResultActivity para não acumular no histórico
        }
    }
}
