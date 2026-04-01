package com.example.ratebit.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ratebit.R
import com.example.ratebit.model.Game
import com.example.ratebit.ui.adapter.GamesAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.Slider

class GamesListActivity : AppCompatActivity() {

    private var selectedGenre: String = "Nenhum"
    private var selectedMinRating: Float = 0.0f
    private lateinit var listaDeJogos: List<Game>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_games_list)

        listaDeJogos = listOf(
            Game(1, "GTA V", "Action", "2013", "Rockstar", 9.5, "Mundo aberto"),
            Game(2, "Minecraft", "Action", "2011", "Mojang", 9.0, "Blocos"),
            Game(3, "FIFA 24", "Sports", "2023", "EA Sports", 7.5, "Futebol"),
            Game(4, "The Witcher 3", "RPG", "2015", "CD Projekt", 9.8, "Bruxo"),
            Game(5, "Cyberpunk 2077", "RPG", "2020", "CD Projekt", 8.5, "Futurista"),
            Game(6, "Elden Ring", "RPG", "2022", "FromSoftware", 9.9, "Desafiador"),
            Game(7, "Mario", "Platform", "1998", "Nintendo", 9.5, "Plataforma")
        )

        val recycler = findViewById<RecyclerView>(R.id.recyclerGames)
        val btnSearch = findViewById<ImageView>(R.id.btnSearch)
        val btnFilter = findViewById<ImageView>(R.id.btnFilter)
        val editSearch = findViewById<EditText>(R.id.editSearch)
        val txtTitle = findViewById<TextView>(R.id.txtTitle)

        val adapter = GamesAdapter(listaDeJogos)
        recycler.layoutManager = GridLayoutManager(this, 2)
        recycler.adapter = adapter

        btnSearch.setOnClickListener {
            if (editSearch.visibility == View.GONE) {
                editSearch.visibility = View.VISIBLE
                txtTitle.visibility = View.GONE
                btnFilter.visibility = View.GONE
                btnSearch.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
                editSearch.requestFocus()
            } else {
                editSearch.visibility = View.GONE
                txtTitle.visibility = View.VISIBLE
                btnFilter.visibility = View.VISIBLE
                editSearch.text.clear()
                btnSearch.setImageResource(R.drawable.ic_search)
                adapter.filtrar("")
            }
        }

        btnFilter.setOnClickListener {
            showFilterSheet(adapter)
        }

        editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filtrar(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun showFilterSheet(adapter: GamesAdapter) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_filter_sheet, null)

        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroupGenre)
        val slider = view.findViewById<Slider>(R.id.sliderRating)
        val btnApply = view.findViewById<MaterialButton>(R.id.btnApplyFilter)

        // PEGA GÊNEROS ÚNICOS DA LISTA E CRIA CHIPS DINAMICAMENTE
        val generosUnicos = listaDeJogos.map { it.categoriaJogo }.distinct().sorted()
        
        generosUnicos.forEach { genero ->
            val chip = Chip(this)
            chip.text = genero
            chip.isCheckable = true
            chip.setChipBackgroundColorResource(R.color.chip_state_list)
            chip.setTextColor(android.graphics.Color.WHITE)
            
            if (genero == selectedGenre) {
                chip.isChecked = true
            }
            
            chipGroup.addView(chip)
        }

        slider.value = selectedMinRating

        btnApply.setOnClickListener {
            val selectedChipId = chipGroup.checkedChipId
            
            selectedGenre = if (selectedChipId != View.NO_ID) {
                view.findViewById<Chip>(selectedChipId).text.toString()
            } else {
                "Nenhum"
            }
            
            selectedMinRating = slider.value
            adapter.filtrarPorFiltros(selectedGenre, selectedMinRating)
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }
}