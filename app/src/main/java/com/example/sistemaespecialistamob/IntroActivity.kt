package com.example.sistemaespecialistamob


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sistemaespecialistamob.MainActivity
import com.example.sistemaespecialistamob.R
import com.google.android.material.button.MaterialButton

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro)

        // Encontrar o botão de iniciar o quiz
        val btnStartQuiz: MaterialButton = findViewById(R.id.btnStartQuiz)

        // Definir o comportamento do botão de iniciar o quiz
        btnStartQuiz.setOnClickListener {
            // Criar um Intent para iniciar a MainActivity
            val intent = Intent(this@IntroActivity, MainActivity::class.java)
            // Iniciar a MainActivity
            startActivity(intent)
            // Opcional: fechar a IntroActivity após abrir a MainActivity
            finish() // Se você quiser fechar a IntroActivity após iniciar a MainActivity
        }
    }
}
