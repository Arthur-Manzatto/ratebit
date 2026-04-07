package com.example.ratebit.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ratebit.R
import com.example.ratebit.model.Rating
import com.example.ratebit.model.User
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class ReviewsAdapter(private var reviews: List<Rating>) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    private val db = FirebaseFirestore.getInstance()

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtUsername: TextView = itemView.findViewById(R.id.txt_review_username)
        val txtRating: TextView = itemView.findViewById(R.id.txt_review_rating)
        val txtContent: TextView = itemView.findViewById(R.id.txt_review_content)
        val imgProfile: ImageView = itemView.findViewById(R.id.img_user_pfp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.txtRating.text = String.format(Locale.US, "%.1f", review.score)
        holder.txtContent.text = review.comment

        // Limpa imagem anterior e remove tint para que a foto real apareça colorida
        holder.imgProfile.setImageResource(R.drawable.ic_profile)
        holder.imgProfile.clearColorFilter() 

        db.collection("users").document(review.userEmail).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.toObject(User::class.java)
                holder.txtUsername.text = user?.name ?: review.userEmail
                
                if (user?.urlPfp != null && user.urlPfp.isNotEmpty()) {
                    Glide.with(holder.itemView.context)
                        .load(user.urlPfp)
                        .circleCrop()
                        .placeholder(R.drawable.ic_profile)
                        .error(R.drawable.ic_profile)
                        .into(holder.imgProfile)
                }
            }
            .addOnFailureListener {
                holder.txtUsername.text = review.userEmail
                holder.imgProfile.setImageResource(R.drawable.ic_profile)
            }
    }

    override fun getItemCount(): Int = reviews.size

    fun updateData(newReviews: List<Rating>) {
        reviews = newReviews
        notifyDataSetChanged()
    }
}