package com.example.lab12

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

class RecordsActivity : AppCompatActivity() {

    private lateinit var tvRecords: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records)

        tvRecords = findViewById(R.id.tvRecords)
    }

    override fun onResume() {
        super.onResume()
        showRecords()
    }

    private fun showRecords() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val playerName = prefs.getString("player_name", "Не указано")
        val lastScore = prefs.getInt("last_score", 0)
        val bestScore = prefs.getInt("best_score", 0)

        tvRecords.text = """
            Экран рекордов
            
            Игрок: $playerName
            Последний результат: $lastScore
            Лучший результат: $bestScore
        """.trimIndent()
    }
}