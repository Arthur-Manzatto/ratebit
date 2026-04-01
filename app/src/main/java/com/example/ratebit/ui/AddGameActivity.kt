package com.example.ratebit.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.ratebit.R
import com.example.ratebit.model.Game
import com.example.ratebit.repository.GameRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class AddGameActivity : AppCompatActivity() {

    private val repository = GameRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_game)

        val editName = findViewById<TextInputEditText>(R.id.editName)
        val editGenre = findViewById<TextInputEditText>(R.id.editGenre)
        val editReleaseDate = findViewById<TextInputEditText>(R.id.editReleaseDate)
        val editDeveloper = findViewById<TextInputEditText>(R.id.editDeveloper)
        val editCoverUrl = findViewById<TextInputEditText>(R.id.editCoverUrl)
        val editDescription = findViewById<TextInputEditText>(R.id.editDescription)
        val btnAdd = findViewById<MaterialButton>(R.id.btnAddGame)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        editReleaseDate.isFocusable = false
        editReleaseDate.isClickable = true
        editReleaseDate.setOnClickListener {
            showDatePicker(editReleaseDate)
        }

        btnAdd.setOnClickListener {
            val name = editName.text.toString()
            val genre = editGenre.text.toString()
            val date = editReleaseDate.text.toString()
            val developer = editDeveloper.text.toString()
            val coverUrl = editCoverUrl.text.toString()
            val description = editDescription.text.toString()

            if (name.isNotEmpty() && genre.isNotEmpty() && date.isNotEmpty()) {
                val randomId = (0..1000000).random()
                
                val newGame = Game(
                    id = randomId,
                    name = name,
                    category = genre,
                    releaseDate = date,
                    developer = developer,
                    averageRating = 0.0,
                    description = description,
                    coverUrl = coverUrl
                )

                repository.addGame(newGame, 
                    onSuccess = {
                        Toast.makeText(this, "Game added successfully!", Toast.LENGTH_SHORT).show()
                        finish()
                    },
                    onFailure = {
                        Toast.makeText(this, "Error adding game", Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                Toast.makeText(this, "Please fill Name, Genre and Date", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePicker(editText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                editText.setText(formattedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}