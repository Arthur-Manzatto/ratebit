package com.example.ratebit.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ratebit.R
import com.example.ratebit.model.Game
import com.example.ratebit.model.Rating
import com.example.ratebit.model.User
import com.example.ratebit.repository.GameRepository
import com.example.ratebit.repository.UserRepository
import com.example.ratebit.ui.adapter.ReviewsAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.util.Locale

class GamePageActivity : AppCompatActivity() {

    private val gameRepository = GameRepository()
    private val userRepository = UserRepository()
    private val db = FirebaseFirestore.getInstance()
    
    private var gameId: Int = -1
    private var userEmail: String? = null
    private var isFavorite: Boolean = false
    private var selectedFilterRating: Int = 0
    
    private lateinit var reviewsAdapter: ReviewsAdapter
    private val allReviews = mutableListOf<Rating>()
    private val reviewsList = mutableListOf<Rating>()
    private var userReview: Rating? = null
    private var favoritesListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_page)
        
        val sharedPref = getSharedPreferences("ratebit_prefs", Context.MODE_PRIVATE)
        userEmail = sharedPref.getString("user_email", null)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_header)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        gameId = intent.getIntExtra("GAME_ID", -1)

        setupReviewsList()
        setupButtons()
        
        if (gameId != -1) {
            loadGameData()
            loadReviews()
            checkUserReview()
            observeFavorites()
        } else {
            Toast.makeText(this, "Game not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupReviewsList() {
        val rvReviews = findViewById<RecyclerView>(R.id.rv_reviews)
        reviewsAdapter = ReviewsAdapter(reviewsList)
        rvReviews.layoutManager = LinearLayoutManager(this)
        rvReviews.adapter = reviewsAdapter
    }

    private fun setupButtons() {
        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }

        findViewById<MaterialButton>(R.id.btn_review_it).setOnClickListener {
            if (userEmail != null) {
                showAddReviewDialog()
            } else {
                Toast.makeText(this, "Please login to review", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<ImageView>(R.id.btn_favorite).setOnClickListener {
            userEmail?.let { email ->
                if (gameId != -1) {
                    val nextState = !isFavorite
                    userRepository.toggleFavorite(email, gameId, nextState) { }
                }
            } ?: run {
                Toast.makeText(this, "Please login to favorite games", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<ImageView>(R.id.btn_filter_reviews).setOnClickListener {
            showFilterReviewsSheet()
        }
    }

    private fun showFilterReviewsSheet() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_filter_sheet, null)

        view.findViewById<TextView>(R.id.labelGenre).visibility = View.GONE
        view.findViewById<View>(R.id.chipGroupGenre).visibility = View.GONE
        
        val layoutStars = view.findViewById<LinearLayout>(R.id.layoutStarsFilter)
        val btnApply = view.findViewById<MaterialButton>(R.id.btnApplyFilter)
        val btnClear = view.findViewById<TextView>(R.id.btnClearFilters)

        var tempRating = selectedFilterRating
        updateFilterStars(layoutStars, tempRating)

        for (i in 0 until layoutStars.childCount) {
            val star = layoutStars.getChildAt(i) as ImageView
            star.setOnClickListener {
                tempRating = i + 1
                updateFilterStars(layoutStars, tempRating)
            }
        }

        btnClear.setOnClickListener {
            selectedFilterRating = 0
            applyReviewsFilter()
            dialog.dismiss()
        }

        btnApply.setOnClickListener {
            selectedFilterRating = tempRating
            applyReviewsFilter()
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun applyReviewsFilter() {
        reviewsList.clear()
        
        // Exclude current user review from the general list if it exists
        val otherReviews = allReviews.filter { it.userEmail != userEmail }
        
        if (selectedFilterRating == 0) {
            reviewsList.addAll(otherReviews)
        } else {
            val filtered = otherReviews.filter { it.score >= selectedFilterRating }
            reviewsList.addAll(filtered)
        }
        reviewsAdapter.updateData(reviewsList)
    }

    private fun updateFilterStars(layoutStars: LinearLayout, rating: Int) {
        val yellowColor = Color.parseColor("#FFD700")
        val whiteColor = Color.WHITE
        for (i in 0 until layoutStars.childCount) {
            val star = layoutStars.getChildAt(i) as ImageView
            if (i < rating) {
                star.setImageResource(android.R.drawable.btn_star_big_on)
                star.setColorFilter(yellowColor)
            } else {
                star.setImageResource(android.R.drawable.btn_star_big_off)
                star.setColorFilter(whiteColor)
            }
        }
    }

    private fun observeFavorites() {
        userEmail?.let { email ->
            favoritesListener = userRepository.observeFavoriteIds(email) { ids ->
                isFavorite = ids.contains(gameId)
                updateFavoriteUI()
            }
        }
    }

    private fun updateFavoriteUI() {
        val btnFavorite = findViewById<ImageView>(R.id.btn_favorite)
        if (isFavorite) {
            btnFavorite.setImageResource(android.R.drawable.btn_star_big_on)
            btnFavorite.setColorFilter(Color.YELLOW)
        } else {
            btnFavorite.setImageResource(android.R.drawable.btn_star_big_off)
            btnFavorite.setColorFilter(Color.WHITE)
        }
    }

    private fun checkUserReview() {
        val layoutYourReview = findViewById<LinearLayout>(R.id.layout_your_review)
        if (userEmail == null) {
            layoutYourReview.visibility = View.GONE
            return
        }
        
        db.collection("reviews")
            .whereEqualTo("gameId", gameId)
            .whereEqualTo("userEmail", userEmail)
            .get()
            .addOnSuccessListener { snapshots ->
                if (!snapshots.isEmpty) {
                    userReview = snapshots.documents[0].toObject(Rating::class.java)
                    findViewById<MaterialButton>(R.id.btn_review_it).text = "EDIT REVIEW"
                    displayYourReview()
                } else {
                    userReview = null
                    findViewById<MaterialButton>(R.id.btn_review_it).text = "REVIEW IT"
                    layoutYourReview.visibility = View.GONE
                }
                applyReviewsFilter() // Refresh general list after checking
            }
    }

    private fun displayYourReview() {
        val review = userReview ?: return
        val layoutYourReview = findViewById<LinearLayout>(R.id.layout_your_review)
        layoutYourReview.visibility = View.VISIBLE
        
        val pfp = findViewById<ImageView>(R.id.img_user_pfp)
        val name = findViewById<TextView>(R.id.txt_review_username)
        val rating = findViewById<TextView>(R.id.txt_review_rating)
        val content = findViewById<TextView>(R.id.txt_review_content)
        
        rating.text = String.format(Locale.US, "%.1f", review.score)
        content.text = review.comment
        
        db.collection("users").document(review.userEmail).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.toObject(User::class.java)
                name.text = user?.name ?: review.userEmail
                if (user?.urlPfp != null && user.urlPfp.isNotEmpty()) {
                    Glide.with(this).load(user.urlPfp).circleCrop().into(pfp)
                }
            }
    }

    private fun showAddReviewDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_add_review, null)

        val layoutStars = view.findViewById<LinearLayout>(R.id.layoutStars)
        val editComment = view.findViewById<EditText>(R.id.editComment)
        val btnSubmit = view.findViewById<MaterialButton>(R.id.btnSubmitReview)
        
        var selectedRating = 0
        
        userReview?.let {
            selectedRating = it.score.toInt()
            editComment.setText(it.comment)
            updateStarVisuals(layoutStars, selectedRating)
            btnSubmit.text = "UPDATE REVIEW"
        }

        for (i in 0 until layoutStars.childCount) {
            val star = layoutStars.getChildAt(i) as ImageView
            star.setOnClickListener {
                selectedRating = i + 1
                updateStarVisuals(layoutStars, selectedRating)
            }
        }

        btnSubmit.setOnClickListener {
            val comment = editComment.text.toString()

            if (selectedRating == 0) {
                Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (comment.isNotEmpty()) {
                val review = Rating(gameId, userEmail!!, selectedRating.toDouble(), comment)
                submitReview(review, dialog)
            } else {
                Toast.makeText(this, "Please write a comment", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun updateStarVisuals(layoutStars: LinearLayout, rating: Int) {
        val yellowColor = Color.parseColor("#FFD700")
        val whiteColor = Color.WHITE

        for (i in 0 until layoutStars.childCount) {
            val star = layoutStars.getChildAt(i) as ImageView
            if (i < rating) {
                star.setImageResource(android.R.drawable.btn_star_big_on)
                star.setColorFilter(yellowColor)
            } else {
                star.setImageResource(android.R.drawable.btn_star_big_off)
                star.setColorFilter(whiteColor)
            }
        }
    }

    private fun submitReview(review: Rating, dialog: BottomSheetDialog) {
        db.collection("reviews")
            .whereEqualTo("gameId", gameId)
            .whereEqualTo("userEmail", userEmail)
            .get()
            .addOnSuccessListener { snapshots ->
                if (!snapshots.isEmpty) {
                    val docId = snapshots.documents[0].id
                    db.collection("reviews").document(docId).set(review)
                        .addOnSuccessListener { handleSuccess(dialog) }
                } else {
                    db.collection("reviews").add(review)
                        .addOnSuccessListener { handleSuccess(dialog) }
                }
            }
    }
    
    private fun handleSuccess(dialog: BottomSheetDialog) {
        Toast.makeText(this, "Review saved!", Toast.LENGTH_SHORT).show()
        updateGameAverageRating()
        dialog.dismiss()
        loadReviews()
        checkUserReview()
    }

    private fun updateGameAverageRating() {
        db.collection("reviews").whereEqualTo("gameId", gameId).get()
            .addOnSuccessListener { snapshots ->
                val reviews = snapshots.toObjects(Rating::class.java)
                if (reviews.isNotEmpty()) {
                    val average = reviews.map { it.score }.average()
                    
                    db.collection("games").whereEqualTo("id", gameId).get()
                        .addOnSuccessListener { gameSnapshots ->
                            if (!gameSnapshots.isEmpty) {
                                val docId = gameSnapshots.documents[0].id
                                db.collection("games").document(docId)
                                    .update("averageRating", average)
                                    .addOnSuccessListener { loadGameData() }
                            }
                        }
                }
            }
    }

    private fun loadReviews() {
        db.collection("reviews")
            .whereEqualTo("gameId", gameId)
            .get()
            .addOnSuccessListener { snapshots ->
                val reviews = snapshots.toObjects(Rating::class.java)
                allReviews.clear()
                allReviews.addAll(reviews)
                applyReviewsFilter()
            }
    }

    private fun loadGameData() {
        gameRepository.getGameById(gameId,
            onResult = { game ->
                if (game != null) {
                    displayGame(game)
                }
            },
            onError = { /* Handle error */ }
        )
    }

    private fun displayGame(game: Game) {
        findViewById<TextView>(R.id.txt_game_name).text = game.name
        findViewById<TextView>(R.id.txt_genre).text = game.category
        findViewById<TextView>(R.id.txt_developer).text = game.developer
        findViewById<TextView>(R.id.txt_release_date).text = game.releaseDate
        findViewById<TextView>(R.id.txt_description).text = game.description
        
        val ratingText = if (game.averageRating > 0) {
            String.format(Locale.US, "%.1f", game.averageRating)
        } else {
            "0.0"
        }
        findViewById<TextView>(R.id.txt_total_rating).text = ratingText

        val imgCover = findViewById<ImageView>(R.id.img_game_cover)
        if (game.coverUrl.isNotEmpty()) {
            Glide.with(this).load(game.coverUrl).centerCrop().into(imgCover)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        favoritesListener?.remove()
    }
}