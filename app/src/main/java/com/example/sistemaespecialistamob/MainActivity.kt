package com.example.sistemaespecialistamob

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private var currentQuestionIndex = 0
    private val questions = listOf(
        "I enjoy being the center of attention at social gatherings.",
        "I prefer to think things through carefully before making decisions.",
        "I find it easy to empathize with others' feelings.",
        "I enjoy having a structured daily routine.",
        "I often take initiative in social situations.",
        "I prefer working on creative projects rather than analytical ones.",
        "I tend to plan ahead rather than be spontaneous.",
        "I enjoy deep conversations about abstract concepts.",
        "I feel energized after spending time with others.",
        "I prefer practical, hands-on learning experiences."
    )
    private lateinit var optionButtons: List<MaterialButton>

    // Lista para armazenar respostas (0-4 para cada opção, -1 se não houver resposta)
    private val selectedAnswers = MutableList(questions.size) { -1 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
        updateQuestion()
    }

    private fun setupUI() {
        findViewById<MaterialButton>(R.id.previousButton).setOnClickListener {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--
                updateQuestion()
            }
        }

        findViewById<MaterialButton>(R.id.nextButton).setOnClickListener {
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                updateQuestion()
            }
        }

        optionButtons = listOf(
            findViewById(R.id.stronglyAgreeButton),
            findViewById(R.id.agreeButton),
            findViewById(R.id.neutralButton),
            findViewById(R.id.disagreeButton),
            findViewById(R.id.stronglyDisagreeButton)
        )

        optionButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                onOptionSelected(button, index)
            }
        }
    }

    private fun updateQuestion() {
        // Atualiza o texto da pergunta
        findViewById<TextView>(R.id.questionText).text = questions[currentQuestionIndex]

        // Atualiza o número da pergunta
        findViewById<TextView>(R.id.questionNumberText).text = "${currentQuestionIndex + 1}"

        // Atualiza a barra de progresso
        findViewById<ProgressBar>(R.id.progressBar).progress =
            ((currentQuestionIndex + 1) * 100) / questions.size

        // Exibe o botão "Previous" apenas se não estivermos na primeira pergunta
        findViewById<MaterialButton>(R.id.previousButton).visibility =
            if (currentQuestionIndex > 0) View.VISIBLE else View.GONE

        // Habilita/desabilita o botão "Next" com base na resposta atual
        val nextButton = findViewById<MaterialButton>(R.id.nextButton)
        nextButton.isEnabled = selectedAnswers[currentQuestionIndex] != -1

        // Restaura a seleção da resposta para a pergunta atual
        resetButtonColors()
        val selectedAnswer = selectedAnswers[currentQuestionIndex]
        if (selectedAnswer != -1) {
            optionButtons[selectedAnswer].backgroundTintList = getColorStateList(R.color.purple_700)
        }
    }

    private fun onOptionSelected(selectedButton: MaterialButton, answerIndex: Int) {
        // Armazena a resposta selecionada para a pergunta atual
        selectedAnswers[currentQuestionIndex] = answerIndex

        // Define a cor de destaque para o botão selecionado e reseta os outros
        resetButtonColors()
        selectedButton.backgroundTintList = getColorStateList(R.color.purple_700)

        // Habilita o botão "Next" agora que uma resposta foi selecionada
        findViewById<MaterialButton>(R.id.nextButton).isEnabled = true
    }

    private fun resetButtonColors() {
        // Reseta as cores de todos os botões para o padrão
        optionButtons.forEach { button ->
            button.backgroundTintList = getColorStateList(R.color.white)
        }
    }
}
