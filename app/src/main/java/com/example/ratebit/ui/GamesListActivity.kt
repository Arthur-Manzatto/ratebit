package com.example.ratebit.ui

import com.example.ratebit.ui.adapter.GamesAdapter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ratebit.R

class GamesListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_games_list)

        val lista = listOf(
            "GTA V",
            "Minecraft",
            "FIFA",
            "Call of Duty",
            "The Witcher",
            "Cyberpunk"
        )

        val recycler = findViewById<RecyclerView>(R.id.recyclerGames)

        recycler.layoutManager = GridLayoutManager(this, 2)
        recycler.adapter = GamesAdapter(lista)
    }
}