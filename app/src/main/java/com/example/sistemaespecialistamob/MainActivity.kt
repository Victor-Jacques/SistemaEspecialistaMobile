package com.example.sistemaespecialistamob

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private val apiRepository = ApiRepository()
    private var currentQuestionIndex = 0
    private val selectedAnswers = mutableListOf<Int>() // Armazena respostas
    private val perfis: List<String> = listOf("dominancia", "influencia", "estabilidade", "conformidade")
    private val questionsPerPerfil = 5 // Número de perguntas por perfil

    // Lista de botões de opção para manipulação
    private lateinit var optionButtons: List<MaterialButton>
    private var selectedButtonIndex: Int? = null // Índice do botão selecionado para a pergunta atual

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
        loadQuestion() // Carrega a primeira pergunta
    }

    private fun loadQuestion() {
        val perfilIndex = currentQuestionIndex / questionsPerPerfil // Identifica o perfil atual
        val questionNumber = (currentQuestionIndex % questionsPerPerfil) + 1 // Número da pergunta dentro do perfil

        if (perfilIndex < perfis.size) {
            selectPerfil(perfis[perfilIndex], questionNumber)
        } else {
            findViewById<TextView>(R.id.questionText).text = "Fim do questionário"
        }

        // Resetar a cor dos botões ao carregar uma nova pergunta
        resetButtonColors()
        // Atualiza o botão selecionado, se já existir uma resposta para essa pergunta
        selectedButtonIndex = selectedAnswers.getOrNull(currentQuestionIndex)
        selectedButtonIndex?.let { highlightButton(it) }
    }

    private fun selectPerfil(perfil: String, questionNumber: Int) {
        apiRepository.fetchQuestion(perfil, questionNumber) { question ->
            if (question != null) {
                updateQuestionUI(question)
            } else {
                findViewById<TextView>(R.id.questionText).text = "Erro ao carregar a questão"
            }
        }
    }

    private fun setupUI() {
        findViewById<MaterialButton>(R.id.previousButton).setOnClickListener {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--
                loadQuestion() // Carrega a pergunta anterior
                updateNavigationButtons()
            }
        }

        findViewById<MaterialButton>(R.id.nextButton).setOnClickListener {
            if (currentQuestionIndex < perfis.size * questionsPerPerfil - 1) {
                currentQuestionIndex++
                loadQuestion() // Carrega a próxima pergunta
                updateNavigationButtons()
            } else {
                findViewById<TextView>(R.id.questionText).text = "Fim do questionário"
            }
        }

        optionButtons = listOf(
            findViewById<MaterialButton>(R.id.stronglyAgreeButton),
            findViewById<MaterialButton>(R.id.agreeButton),
            findViewById<MaterialButton>(R.id.neutralButton),
            findViewById<MaterialButton>(R.id.disagreeButton),
            findViewById<MaterialButton>(R.id.stronglyDisagreeButton)
        )

        optionButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                onOptionSelected(button, index)
            }
        }

        updateNavigationButtons()
    }

    private fun updateQuestionUI(questionText: String) {
        val totalQuestions = perfis.size * questionsPerPerfil
        findViewById<TextView>(R.id.questionText).text = questionText
        findViewById<TextView>(R.id.questionNumberText).text = "Pergunta ${currentQuestionIndex + 1} de $totalQuestions"
        findViewById<ProgressBar>(R.id.progressBar).progress = ((currentQuestionIndex + 1) * 100) / totalQuestions

        // Restaura o estado do botão "Next" para o caso em que uma resposta não foi selecionada ainda
        findViewById<MaterialButton>(R.id.nextButton).isEnabled = selectedAnswers.getOrNull(currentQuestionIndex) != null
    }

    private fun onOptionSelected(selectedButton: MaterialButton, answerIndex: Int) {
        // Salva o índice da resposta selecionada
        if (selectedAnswers.size > currentQuestionIndex) {
            selectedAnswers[currentQuestionIndex] = answerIndex
        } else {
            selectedAnswers.add(answerIndex)
        }

        // Armazena o índice do botão selecionado
        selectedButtonIndex = answerIndex

        // Muda a cor do botão selecionado
        highlightButton(answerIndex)
        findViewById<MaterialButton>(R.id.nextButton).isEnabled = true
    }

    private fun highlightButton(index: Int) {
        resetButtonColors() // Remove as cores dos botões anteriores
        optionButtons[index].setBackgroundColor(resources.getColor(R.color.purple_700)) // Define a cor do botão selecionado
        optionButtons[index].setTextColor(resources.getColor(R.color.white)) // Define a cor do texto do botão selecionado
    }

    private fun resetButtonColors() {
        // Reseta a cor de fundo e do texto de todos os botões para o padrão
        optionButtons.forEach { button ->
            button.setBackgroundColor(resources.getColor(R.color.white))
            button.setTextColor(resources.getColor(R.color.gray_800))
        }
    }

    private fun updateNavigationButtons() {
        findViewById<MaterialButton>(R.id.previousButton).visibility =
            if (currentQuestionIndex > 0) View.VISIBLE else View.GONE

        findViewById<MaterialButton>(R.id.nextButton).isEnabled =
            selectedAnswers.getOrNull(currentQuestionIndex) != null
    }
}
