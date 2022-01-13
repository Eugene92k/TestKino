package com.example.myapplication.listFilms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.model.Film
import com.example.myapplication.data.model.FilmItem
import com.example.myapplication.data.model.Genre
import com.example.myapplication.data.model.Header
import com.example.myapplication.databinding.ListItemFilmBinding
import com.example.myapplication.databinding.ListItemGenreBinding
import com.example.myapplication.databinding.ListItemHeaderBinding
import com.example.myapplication.loadImage


class ListFilmsAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var allListD = arrayListOf<FilmItem>()
    var listD = arrayListOf<FilmItem>()
    private var delegate: OnClickItem? = null

    fun setNew(newData: ArrayList<FilmItem>, full: Boolean = true) {
        listD.clear()
        listD.addAll(newData)
        if (full) {
            allListD.clear()
            allListD.addAll(listD)
        }
        notifyDataSetChanged()
    }

    fun clear() {
        listD.clear()
        listD.addAll(allListD)
        notifyDataSetChanged()
    }

    fun attachDelegate(delegate: OnClickItem) {
        this.delegate = delegate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            FilmItem.HEADER -> HeaderViewHolder(ListItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            FilmItem.GENRE -> GenreViewHolder(ListItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false), delegate)
            else -> FilmViewHolder(ListItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false), delegate)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is FilmViewHolder -> holder.bind(model = listD[position] as Film)
            is HeaderViewHolder -> holder.bind(model = listD[position] as Header)
            is GenreViewHolder -> holder.bind(model = listD[position] as Genre)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (listD[position]) {
            is Header -> FilmItem.HEADER
            is Genre -> FilmItem.GENRE
            else -> FilmItem.FILM
        }
    }

    override fun getItemCount(): Int = listD.size

    class FilmViewHolder(val binding: ListItemFilmBinding, val delegate: OnClickItem?) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: Film){
            binding.apply {
                previewImageview.loadImage(model.imageUrl)
                previewTextview.text = model.localizedName
                previewImageview.setOnClickListener {
                    delegate?.openFilm(model)
                }
            }
        }

    }

    class GenreViewHolder(val binding: ListItemGenreBinding, val delegate: OnClickItem?) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: Genre){
            binding.apply {
                genreButton.text = model.title
                genreButton.setOnClickListener {
                    delegate?.filter(model)
                }
                genreButton.isPressed = model.activate
            }
        }
    }

    class HeaderViewHolder(val binding: ListItemHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: Header){
            binding.apply {
                headerTextview.text = model.title
            }
        }
    }
}

interface OnClickItem {
    fun filter(genre: Genre)
    fun openFilm(film: Film)
}