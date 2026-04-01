package com.example.ratebit.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ratebit.R
import com.example.ratebit.model.Game

class GamesAdapter(private val listaOriginal: List<Game>) :
    RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    private var listaFiltrada = listaOriginal.toList()

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.txtName)
        val genero: TextView = itemView.findViewById(R.id.txtGenre)
        val nota: TextView = itemView.findViewById(R.id.txtRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = listaFiltrada[position]
        holder.nome.text = game.nomeJogo
        holder.genero.text = game.categoriaJogo
        holder.nota.text = game.notaMediaJogo.toString()
    }

    override fun getItemCount(): Int = listaFiltrada.size

    fun filtrar(texto: String) {
        listaFiltrada = if (texto.isEmpty()) {
            listaOriginal
        } else {
            listaOriginal.filter {
                it.nomeJogo.contains(texto, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }

    fun filtrarPorFiltros(genero: String, notaMinima: Float) {
        listaFiltrada = listaOriginal.filter { game ->
            val matchesGenre = if (genero == "Nenhum") true else game.categoriaJogo == genero
            val matchesRating = game.notaMediaJogo >= notaMinima
            matchesGenre && matchesRating
        }
        notifyDataSetChanged()
    }
}