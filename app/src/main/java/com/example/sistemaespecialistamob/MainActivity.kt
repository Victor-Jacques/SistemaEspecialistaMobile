package com.example.sistemaespecialistamob

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    private val apiRepository = ApiRepository()
    private var currentQuestionIndex = 0
    private val selectedAnswers = mutableListOf<Int>()
    private val perfis: List<String> = listOf("dominancia", "influencia", "estabilidade", "conformidade")
    private val questionsPerPerfil = 5
    private val totalQuestions = perfis.size * questionsPerPerfil
    val result = Resultado()
    val builder = StringBuilder()
    var completedCalls = 0 // Contador para chamadas concluídas


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
        loadQuestion()
    }

    private fun loadQuestion() {
        val perfilIndex = currentQuestionIndex / questionsPerPerfil
        val questionNumber = (currentQuestionIndex % questionsPerPerfil) + 1

        // Resetar as cores dos botões
        resetButtonStyles()

        if (perfilIndex < perfis.size) {
            selectPerfil(perfis[perfilIndex], questionNumber)
        } else {
            findViewById<TextView>(R.id.questionText).text = "Fim do questionário"
        }
    }

    private fun resetButtonStyles() {
        val optionButtons = listOf(
            findViewById<MaterialButton>(R.id.stronglyAgreeButton),
            findViewById<MaterialButton>(R.id.agreeButton),
            findViewById<MaterialButton>(R.id.neutralButton),
            findViewById<MaterialButton>(R.id.disagreeButton),
            findViewById<MaterialButton>(R.id.stronglyDisagreeButton)
        )

        optionButtons.forEach { button ->
            button.setBackgroundColor(resources.getColor(R.color.white))  // Cor de fundo padrão
            button.setTextColor(resources.getColor(R.color.gray_800))  // Cor de texto padrão
        }
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
        val nextButton = findViewById<MaterialButton>(R.id.nextButton)

        findViewById<MaterialButton>(R.id.previousButton).setOnClickListener {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--
                loadQuestion()
                updateNavigationButtons()
            }
        }

        nextButton.setOnClickListener {
            if (currentQuestionIndex < totalQuestions - 1) {
                currentQuestionIndex++
                loadQuestion()
                updateNavigationButtons()
            } else {
                // Ao chegar na última pergunta, redireciona para a nova tela
                val intent = Intent(this, ResultActivity::class.java)
                startActivity(intent)
                calcularScore()
                finish() // Finaliza a Activity atual para remover do stack
            }
        }


        val optionButtons = listOf(
            findViewById<MaterialButton>(R.id.stronglyDisagreeButton),
            findViewById<MaterialButton>(R.id.disagreeButton),
            findViewById<MaterialButton>(R.id.neutralButton),
            findViewById<MaterialButton>(R.id.agreeButton),
            findViewById<MaterialButton>(R.id.stronglyAgreeButton)
        )

        optionButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                onOptionSelected(button, index)
            }
        }

        updateNavigationButtons()
    }


    private fun updateQuestionUI(questionText: String) {
        findViewById<TextView>(R.id.questionText).text = questionText
        findViewById<TextView>(R.id.questionNumberText).text = "Pergunta ${currentQuestionIndex + 1} de $totalQuestions"
        findViewById<ProgressBar>(R.id.progressBar).progress = ((currentQuestionIndex + 1) * 100) / totalQuestions

        findViewById<MaterialButton>(R.id.nextButton).isEnabled = selectedAnswers.getOrNull(currentQuestionIndex) != null

        // Muda o texto do botão para "Finalizar Teste" na última pergunta
        if (currentQuestionIndex == totalQuestions - 1) {
            findViewById<MaterialButton>(R.id.nextButton).text = "Finalizar Teste"
        } else {
            findViewById<MaterialButton>(R.id.nextButton).text = "Próxima Pergunta"
        }
    }

    private fun onOptionSelected(selectedButton: MaterialButton, answerIndex: Int) {
        // Desmarcar todos os outros botões e alterar sua cor de fundo e texto
        val optionButtons = listOf(
            findViewById<MaterialButton>(R.id.stronglyAgreeButton),
            findViewById<MaterialButton>(R.id.agreeButton),
            findViewById<MaterialButton>(R.id.neutralButton),
            findViewById<MaterialButton>(R.id.disagreeButton),
            findViewById<MaterialButton>(R.id.stronglyDisagreeButton)
        )

        optionButtons.forEach { button ->
            button.setBackgroundColor(resources.getColor(R.color.white))  // Cor de fundo padrão
            button.setTextColor(resources.getColor(R.color.gray_800))  // Cor de texto padrão
        }

        // Alterar a cor do botão selecionado
        selectedButton.setBackgroundColor(resources.getColor(R.color.purple_700))  // Cor de fundo quando selecionado
        selectedButton.setTextColor(resources.getColor(R.color.white))  // Cor de texto quando selecionado

        // Armazenar a resposta no array como valor de 1 a 5
        val selectedValue = answerIndex + 1 // Converte de 0-4 para 1-5
        if (selectedAnswers.size > currentQuestionIndex) {
            selectedAnswers[currentQuestionIndex] = selectedValue
        } else {
            selectedAnswers.add(selectedValue)
        }
        // Habilita o botão "Próxima Pergunta" após a seleção
        findViewById<MaterialButton>(R.id.nextButton).isEnabled = true
    }

    private fun calcularScore() {
        // Divida as respostas para os perfis
        for (i in 0..19) {
            separa_resp(i)
        }


        for (i in 1..4) { // Ajustado para 4 perfis: dominância, influência, estabilidade, conformidade
            when (i) {
                1 -> resultado("dominancia", result.dominancia) { completedCalls++ }
                2 -> resultado("influencia", result.influencia) { completedCalls++ }
                3 -> resultado("estabilidade", result.estabilidade) { completedCalls++ }
                4 -> resultado("conformidade", result.conformidade) { completedCalls++ }
            }
        }
    }

    private fun resultado(perfil: String, respostaPerfil: List<Int>, onCompleted: () -> Unit) {
        apiRepository.scoreCalc(perfil, respostaPerfil) { resultIndividual ->
            if (resultIndividual != null) {
                builder.append(resultIndividual.toString()).append("\n")
            } else {
                Log.e("MainActivity", "Erro ao calcular o score completo.")
            }

            // Chama a função onCompleted quando a operação estiver concluída
            onCompleted()

            // Verifica se todas as chamadas foram concluídas
            if (completedCalls == 4) { // 4 chamadas, uma para cada perfil
                finalizarCalculo()
            }
        }
    }

    // Função para iniciar a ResultActivity após o cálculo completo
    private fun finalizarCalculo() {
        val stringResposta = builder.toString()
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("RESULTADO", stringResposta)
        startActivity(intent)
    }

    // Método para separar as respostas dos perfis
    private fun separa_resp(i: Int) {
        when {
            i <= 4 -> result.dominancia.add(selectedAnswers[i])
            i <= 9 -> result.influencia.add(selectedAnswers[i])
            i <= 14 -> result.estabilidade.add(selectedAnswers[i])
            i <= 19 -> result.conformidade.add(selectedAnswers[i])
        }
    }



    private fun updateNavigationButtons() {
        findViewById<MaterialButton>(R.id.previousButton).visibility =
            if (currentQuestionIndex > 0) View.VISIBLE else View.GONE

        findViewById<MaterialButton>(R.id.nextButton).isEnabled =
            selectedAnswers.getOrNull(currentQuestionIndex) != null
    }
}
