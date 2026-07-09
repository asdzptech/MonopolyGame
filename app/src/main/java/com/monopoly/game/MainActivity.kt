package com.monopoly.game

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var playerCountSpinner: Spinner
    private lateinit var difficultySpinner: Spinner
    private lateinit var startButton: Button
    private lateinit var exitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        setupSpinners()
        setupClickListeners()
    }

    private fun setupViews() {
        playerCountSpinner = findViewById(R.id.playerCountSpinner)
        difficultySpinner = findViewById(R.id.difficultySpinner)
        startButton = findViewById(R.id.startButton)
        exitButton = findViewById(R.id.exitButton)
    }

    private fun setupSpinners() {
        // 玩家数量选项
        val playerOptions = listOf("2 玩家", "3 玩家", "4 玩家")
        val playerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, playerOptions)
        playerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        playerCountSpinner.adapter = playerAdapter
        playerCountSpinner.setSelection(1) // 默认3玩家

        // AI难度选项
        val difficultyOptions = listOf("简单 AI", "普通 AI", "困难 AI")
        val difficultyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, difficultyOptions)
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        difficultySpinner.adapter = difficultyAdapter
        difficultySpinner.setSelection(1) // 默认普通难度
    }

    private fun setupClickListeners() {
        startButton.setOnClickListener {
            val playerCount = playerCountSpinner.selectedItemPosition + 2
            val difficulty = difficultySpinner.selectedItemPosition + 1

            Toast.makeText(this, "游戏开始！玩家数: $playerCount", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("player_count", playerCount)
            intent.putExtra("ai_difficulty", difficulty)
            startActivity(intent)
        }

        exitButton.setOnClickListener {
            finish()
        }
    }
}
