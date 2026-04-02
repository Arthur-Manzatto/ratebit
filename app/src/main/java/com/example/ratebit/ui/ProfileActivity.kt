package com.example.ratebit.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ratebit.R
import com.example.ratebit.model.Game
import com.example.ratebit.model.User
import com.example.ratebit.repository.UserRepository
import com.example.ratebit.ui.adapter.GamesAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private val userRepository = UserRepository()
    private val db = FirebaseFirestore.getInstance()
    private val favoriteGamesList = mutableListOf<Game>()
    private var favoriteIds = mutableListOf<Int>()
    private lateinit var adapter: GamesAdapter
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnMenu = findViewById<ImageView>(R.id.btnMenu)
        val imgProfile = findViewById<ImageView>(R.id.imgProfile)
        val txtUsername = findViewById<TextView>(R.id.txtUsername)
        val txtEmail = findViewById<TextView>(R.id.txtEmail)
        val recycler = findViewById<RecyclerView>(R.id.recyclerFavorites)

        btnBack.setOnClickListener { finish() }
        btnMenu.setOnClickListener { showMenu() }

        adapter = GamesAdapter(
            originalList = favoriteGamesList,
            favoriteIds = favoriteIds,
            onFavoriteClick = { game, isFavorite ->
                currentUser?.let { user ->
                    userRepository.toggleFavorite(user.email, game.id, isFavorite) {
                        loadFavorites(user.email)
                    }
                }
            }
        )

        recycler.layoutManager = GridLayoutManager(this, 2)
        recycler.adapter = adapter

        val sharedPref = getSharedPreferences("ratebit_prefs", Context.MODE_PRIVATE)
        val userEmail = sharedPref.getString("user_email", null)

        userEmail?.let { email ->
            loadUserProfile(email, txtUsername, txtEmail, imgProfile)
            loadFavorites(email)
        }
    }

    private fun loadUserProfile(email: String, nameView: TextView, emailView: TextView, imgView: ImageView) {
        userRepository.getUser(email,
            onSuccess = { user ->
                currentUser = user
                user?.let {
                    nameView.text = it.name
                    emailView.text = it.email
                    if (it.urlPfp.isNotEmpty()) {
                        Glide.with(this).load(it.urlPfp).placeholder(R.drawable.ic_launcher_background).into(imgView)
                    } else {
                        imgView.setImageResource(R.drawable.ic_launcher_background)
                    }
                }
            },
            onFailure = { Toast.makeText(this, "Error loading profile", Toast.LENGTH_SHORT).show() }
        )
    }

    private fun loadFavorites(email: String) {
        userRepository.getFavoriteIds(email) { ids ->
            favoriteIds.clear()
            favoriteIds.addAll(ids)
            
            if (ids.isEmpty()) {
                favoriteGamesList.clear()
                adapter.updateData(mutableListOf(), ids)
                return@getFavoriteIds
            }

            db.collection("games").whereIn("id", ids).get()
                .addOnSuccessListener { snapshots ->
                    val games = snapshots.toObjects(Game::class.java)
                    favoriteGamesList.clear()
                    favoriteGamesList.addAll(games)
                    adapter.updateData(games, ids)
                }
        }
    }

    private fun showMenu() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_profile_menu, null)

        view.findViewById<TextView>(R.id.menuEditProfile).setOnClickListener {
            dialog.dismiss()
            showEditProfileDialog()
        }

        view.findViewById<TextView>(R.id.menuLogout).setOnClickListener {
            dialog.dismiss()
            logout()
        }

        view.findViewById<TextView>(R.id.menuDeleteAccount).setOnClickListener {
            dialog.dismiss()
            deleteAccount()
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun showEditProfileDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_edit_profile, null)

        val editName = view.findViewById<TextInputEditText>(R.id.editProfileName)
        val editPfpUrl = view.findViewById<TextInputEditText>(R.id.editProfilePfpUrl)
        val btnSave = view.findViewById<MaterialButton>(R.id.btnSaveProfile)

        currentUser?.let {
            editName.setText(it.name)
            editPfpUrl.setText(it.urlPfp)
        }

        btnSave.setOnClickListener {
            val newName = editName.text.toString()
            val newPfpUrl = editPfpUrl.setText(editPfpUrl.text.toString()).toString() // Fixing a small logic error in previous thoughts

            currentUser?.let { user ->
                val updatedUser = user.copy(name = newName, urlPfp = editPfpUrl.text.toString())
                userRepository.updateUser(updatedUser,
                    onSuccess = {
                        Toast.makeText(this, getString(R.string.profile_updated), Toast.LENGTH_SHORT).show()
                        loadUserProfile(user.email, findViewById(R.id.txtUsername), findViewById(R.id.txtEmail), findViewById(R.id.imgProfile))
                        dialog.dismiss()
                    },
                    onFailure = { Toast.makeText(this, "Error updating profile", Toast.LENGTH_SHORT).show() }
                )
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun logout() {
        val sharedPref = getSharedPreferences("ratebit_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().remove("user_email").apply()
        
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun deleteAccount() {
        currentUser?.let { user ->
            userRepository.deleteUser(user.email,
                onSuccess = {
                    Toast.makeText(this, getString(R.string.account_deleted), Toast.LENGTH_SHORT).show()
                    logout()
                },
                onFailure = { Toast.makeText(this, "Error deleting account", Toast.LENGTH_SHORT).show() }
            )
        }
    }
}