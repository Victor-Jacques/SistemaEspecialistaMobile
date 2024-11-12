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

        // Configurando o título e o texto do resultado
        val resultTitle = findViewById<TextView>(R.id.resultTitle)
        val resultText = findViewById<TextView>(R.id.resultText)

        // Aqui, carregamos o resultado a partir das respostas (você pode personalizar essa parte)
        val resultado = calcularResultado()
        resultText.text = resultado

        // Botão para refazer o teste
        val retryButton = findViewById<Button>(R.id.retryButton)
        retryButton.setOnClickListener {
            // Volta para a tela inicial para refazer o quiz
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Fecha a ResultActivity para não acumular no histórico
        }
    }

    // Função para calcular o resultado do quiz (exemplo, personalize conforme a lógica)
    private fun calcularResultado(): String {
        // Aqui você pode obter o resultado com base nas respostas do usuário
        // Por exemplo, você poderia passar as respostas da MainActivity para cá via Intent
        return "Dominância: Alta\nInfluência: Baixa\nEstabilidade: Média\nConformidade: Alta"
    }
}
