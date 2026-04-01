package com.example.ratebit.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ratebit.R
import com.example.ratebit.model.Game

class GamesAdapter(private var originalList: List<Game>) :
    RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    private var filteredList = originalList.toList()

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.txtName)
        val genre: TextView = itemView.findViewById(R.id.txtGenre)
        val rating: TextView = itemView.findViewById(R.id.txtRating)
        val cover: ImageView = itemView.findViewById(R.id.imgGame)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = filteredList[position]
        holder.name.text = game.name
        holder.genre.text = game.category
        holder.rating.text = game.averageRating.toString()

        // CARREGA A IMAGEM USANDO GLIDE
        if (game.coverUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(game.coverUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background) // Imagem enquanto carrega
                .into(holder.cover)
        } else {
            holder.cover.setImageResource(R.drawable.ic_launcher_background)
        }
    }

    override fun getItemCount(): Int = filteredList.size

    fun updateData(newList: List<Game>) {
        originalList = newList
        filteredList = newList.toList()
        notifyDataSetChanged()
    }

    fun filter(text: String) {
        filteredList = if (text.isEmpty()) {
            originalList
        } else {
            originalList.filter {
                it.name.contains(text, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }

    fun filterByCriteria(genre: String, minRating: Float) {
        filteredList = originalList.filter { game ->
            val matchesGenre = if (genre == "None") true else game.category == genre
            val matchesRating = game.averageRating >= minRating
            matchesGenre && matchesRating
        }
        notifyDataSetChanged()
    }
}