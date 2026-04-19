package com.example.lab12

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

class MainActivity : AppCompatActivity() {

    private lateinit var btnGame: Button
    private lateinit var btnSettings: Button
    private lateinit var btnRecords: Button
    private lateinit var btnShowInfo: Button
    private lateinit var tvInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGame = findViewById(R.id.btnGame)
        btnSettings = findViewById(R.id.btnSettings)
        btnRecords = findViewById(R.id.btnRecords)
        btnShowInfo = findViewById(R.id.btnShowInfo)
        tvInfo = findViewById(R.id.tvInfo)

        btnGame.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        btnRecords.setOnClickListener {
            startActivity(Intent(this, RecordsActivity::class.java))
        }

        btnShowInfo.setOnClickListener {
            showCurrentSettings()
        }
    }

    override fun onResume() {
        super.onResume()
        showCurrentSettings()
    }

    private fun showCurrentSettings() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val playerName = prefs.getString("player_name", "Не указано")
        val soundEnabled = prefs.getBoolean("sound_enabled", true)
        val difficulty = prefs.getString("difficulty", "Средняя")
        val lastScore = prefs.getInt("last_score", 0)
        val bestScore = prefs.getInt("best_score", 0)

        val soundText = if (soundEnabled) "включён" else "выключен"

        tvInfo.text = """
            Игра: Сапёр
            
            Текущие настройки:
            Имя игрока: $playerName
            Звук: $soundText
            Сложность: $difficulty
            
            Результаты:
            Последний счёт: $lastScore
            Лучший счёт: $bestScore
        """.trimIndent()
    }
}