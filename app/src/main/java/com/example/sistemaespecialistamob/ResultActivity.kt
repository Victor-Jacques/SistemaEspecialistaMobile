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

        // Recupera o HashMap com o resultado
        val resultMap = intent.getSerializableExtra("RESULTADOS") as? HashMap<String, String>

        // Exibe o resultado de forma organizada
        if (resultMap != null) {
            val resultString = resultMap.entries.joinToString("\n") { "${it.key}: ${it.value}" }
            resultText.text = resultString
        } else {
            resultText.text = "Nenhum resultado disponível"
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

