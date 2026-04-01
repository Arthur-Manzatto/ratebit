package com.example.ratebit.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ratebit.R
import com.example.ratebit.model.Game
import com.example.ratebit.repository.GameRepository
import com.example.ratebit.ui.adapter.GamesAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.Slider
import com.google.firebase.firestore.ListenerRegistration

class GamesListActivity : AppCompatActivity() {

    private var selectedGenre: String = "None"
    private var selectedMinRating: Float = 0.0f
    private var gamesList: MutableList<Game> = mutableListOf()
    private val repository = GameRepository()
    private lateinit var adapter: GamesAdapter
    private var firestoreListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_games_list)

        val recycler = findViewById<RecyclerView>(R.id.recyclerGames)
        val btnSearch = findViewById<ImageView>(R.id.btnSearch)
        val btnFilter = findViewById<ImageView>(R.id.btnFilter)
        val editSearch = findViewById<EditText>(R.id.editSearch)
        val txtTitle = findViewById<TextView>(R.id.txtTitle)

        adapter = GamesAdapter(gamesList)
        recycler.layoutManager = GridLayoutManager(this, 2)
        recycler.adapter = adapter

        firestoreListener = repository.observeGames(
            onResult = { games ->
                gamesList.clear()
                gamesList.addAll(games)
                adapter.updateData(games)
            },
            onError = {
                Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show()
            }
        )

        val btnAdd = findViewById<ImageView>(R.id.btnAdd)
        btnAdd.setOnClickListener {
            val intent = Intent(this, AddGameActivity::class.java)
            startActivity(intent)
        }

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
                adapter.filter("")
            }
        }

        btnFilter.setOnClickListener {
            showFilterSheet()
        }

        editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        firestoreListener?.remove()
    }

    private fun showFilterSheet() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_filter_sheet, null)

        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroupGenre)
        val slider = view.findViewById<Slider>(R.id.sliderRating)
        val btnApply = view.findViewById<MaterialButton>(R.id.btnApplyFilter)

        val uniqueGenres = gamesList.map { it.category }.distinct().sorted()
        
        uniqueGenres.forEach { genre ->
            val chip = Chip(this)
            chip.text = genre
            chip.isCheckable = true
            chip.setChipBackgroundColorResource(R.color.chip_state_list)
            chip.setTextColor(android.graphics.Color.WHITE)
            if (genre == selectedGenre) chip.isChecked = true
            chipGroup.addView(chip)
        }

        slider.value = selectedMinRating

        btnApply.setOnClickListener {
            val selectedChipId = chipGroup.checkedChipId
            selectedGenre = if (selectedChipId != View.NO_ID) {
                view.findViewById<Chip>(selectedChipId).text.toString()
            } else {
                "None"
            }
            selectedMinRating = slider.value
            adapter.filterByCriteria(selectedGenre, selectedMinRating)
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }
}