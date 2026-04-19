package com.example.lab12

import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import kotlin.math.abs
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private lateinit var tvGameInfo: TextView
    private lateinit var tvScore: TextView
    private lateinit var gameGrid: GridLayout
    private lateinit var btnRestart: Button

    private var gridSize = 3
    private var mineIndex = 0
    private var score = 0
    private var gameFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        tvGameInfo = findViewById(R.id.tvGameInfo)
        tvScore = findViewById(R.id.tvScore)
        gameGrid = findViewById(R.id.gameGrid)
        btnRestart = findViewById(R.id.btnRestart)

        btnRestart.setOnClickListener {
            startNewGame()
        }

        startNewGame()
    }

    private fun startNewGame() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val playerName = prefs.getString("player_name", "Игрок")
        val difficulty = prefs.getString("difficulty", "Средняя")

        gridSize = when (difficulty) {
            "Лёгкая" -> 3
            "Средняя" -> 4
            "Сложная" -> 5
            else -> 4
        }

        score = 0
        gameFinished = false
        mineIndex = Random.nextInt(gridSize * gridSize)

        tvGameInfo.text = """
            Экран игры: Сапёр
            
            Игрок: $playerName
            Сложность: $difficulty
            
            Правила:
            На поле спрятана одна мина.
            Нажимайте на безопасные клетки и набирайте очки.
            Если нажмёте на мину — игра закончится.
        """.trimIndent()

        updateScoreText()
        createGameField()
    }

    private fun createGameField() {
        gameGrid.removeAllViews()
        gameGrid.columnCount = gridSize
        gameGrid.rowCount = gridSize

        val sizeInPx = (70 * resources.displayMetrics.density).toInt()

        for (i in 0 until gridSize * gridSize) {
            val button = Button(this)
            button.text = "?"
            button.textSize = 18f

            val params = GridLayout.LayoutParams()
            params.width = sizeInPx
            params.height = sizeInPx
            params.setMargins(8, 8, 8, 8)
            button.layoutParams = params

            button.setOnClickListener {
                onCellClicked(button, i)
            }

            gameGrid.addView(button)
        }
    }

    private fun onCellClicked(button: Button, index: Int) {
        if (gameFinished) return
        if (button.text != "?") return

        if (index == mineIndex) {
            button.text = "X"
            gameFinished = true
            saveResult()
            Toast.makeText(this, "Вы нажали на мину. Игра окончена!", Toast.LENGTH_SHORT).show()
            revealAllCells()
        } else {
            val minesAround = countMinesAround(index)
            button.text = minesAround.toString()
            button.isEnabled = false
            score++
            updateScoreText()

            if (score == gridSize * gridSize - 1) {
                gameFinished = true
                saveResult()
                Toast.makeText(
                    this,
                    "Вы открыли все безопасные клетки. Победа!",
                    Toast.LENGTH_SHORT
                ).show()
                revealAllCells()
            }
        }
    }

    private fun revealAllCells() {
        for (i in 0 until gameGrid.childCount) {
            val button = gameGrid.getChildAt(i) as Button

            if (i == mineIndex) {
                button.text = "X"
            } else if (button.text == "?") {
                button.text = countMinesAround(i).toString()
            }

            button.isEnabled = false
        }
    }

    private fun countMinesAround(index: Int): Int {
        val row = index / gridSize
        val col = index % gridSize

        val mineRow = mineIndex / gridSize
        val mineCol = mineIndex % gridSize

        return if (
            abs(row - mineRow) <= 1 &&
            abs(col - mineCol) <= 1 &&
            index != mineIndex
        ) {
            1
        } else {
            0
        }
    }

    private fun updateScoreText() {
        tvScore.text = "Текущий счёт: $score"
    }

    private fun saveResult() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val bestScore = prefs.getInt("best_score", 0)

        prefs.edit()
            .putInt("last_score", score)
            .putInt("best_score", maxOf(score, bestScore))
            .apply()
    }
}