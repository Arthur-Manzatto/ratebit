package com.example.ratebit.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ratebit.R
import com.example.ratebit.model.Game
import com.example.ratebit.repository.GameRepository
import com.example.ratebit.repository.UserRepository
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
    private var favoriteIds: MutableList<Int> = mutableListOf()
    
    private val gameRepository = GameRepository()
    private val userRepository = UserRepository()
    
    private lateinit var adapter: GamesAdapter
    private var gamesListener: ListenerRegistration? = null
    private var favoritesListener: ListenerRegistration? = null
    private var userEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_games_list)

        val sharedPref = getSharedPreferences("ratebit_prefs", Context.MODE_PRIVATE)
        userEmail = sharedPref.getString("user_email", null)

        val recycler = findViewById<RecyclerView>(R.id.recyclerGames)
        val btnSearch = findViewById<ImageView>(R.id.btnSearch)
        val btnFilter = findViewById<ImageView>(R.id.btnFilter)
        val editSearch = findViewById<EditText>(R.id.editSearch)
        val txtTitle = findViewById<TextView>(R.id.txtTitle)

        adapter = GamesAdapter(
            originalList = gamesList,
            favoriteIds = favoriteIds,
            onFavoriteClick = { game, isFavorite ->
                userEmail?.let { email ->
                    userRepository.toggleFavorite(email, game.id, isFavorite) {
                        // Real-time listener will update local list
                    }
                }
            }
        )
        
        recycler.layoutManager = GridLayoutManager(this, 2)
        recycler.adapter = adapter

        observeUserFavorites()
        observeGames()

        val btnAdd = findViewById<ImageView>(R.id.btnAdd)
        btnAdd.setOnClickListener {
            val intent = Intent(this, AddGameActivity::class.java)
            startActivity(intent)
        }

        val btnProfile = findViewById<ImageView>(R.id.btnProfile)
        btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        btnSearch.setOnClickListener {
            if (!editSearch.isVisible) {
                editSearch.isVisible = true
                txtTitle.isVisible = false
                btnFilter.isVisible = false
                btnSearch.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
                editSearch.requestFocus()
            } else {
                editSearch.isVisible = false
                txtTitle.isVisible = true
                btnFilter.isVisible = true
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

        editSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                adapter.filter(editSearch.text.toString())
                true
            } else false
        }
    }

    private fun observeUserFavorites() {
        userEmail?.let { email ->
            favoritesListener = userRepository.observeFavoriteIds(email) { ids ->
                favoriteIds.clear()
                favoriteIds.addAll(ids)
                adapter.updateFavorites(favoriteIds)
            }
        }
    }

    private fun observeGames() {
        gamesListener = gameRepository.observeGames(
            onResult = { games ->
                gamesList.clear()
                gamesList.addAll(games)
                adapter.updateData(games, favoriteIds)
            },
            onError = {
                Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        gamesListener?.remove()
        favoritesListener?.remove()
    }

    private fun showFilterSheet() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_filter_sheet, findViewById(android.R.id.content), false)

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