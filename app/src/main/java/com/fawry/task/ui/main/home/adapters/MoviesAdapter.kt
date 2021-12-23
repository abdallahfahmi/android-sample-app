package com.fawry.task.ui.main.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fawry.task.data.models.Movie
import com.fawry.task.databinding.MoviesListItemBinding

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    private var movies = listOf<Movie>()
    private lateinit var onMovieClicked: (Movie) -> Unit

    fun updateData(items: List<Movie>, onClick: (Movie) -> Unit) {
        movies = items
        onMovieClicked = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            MoviesListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.movie = movies[position]
        holder.binding.root.setOnClickListener { onMovieClicked(movies[position]) }
    }

    override fun getItemCount() = movies.size

    class ViewHolder(val binding: MoviesListItemBinding) : RecyclerView.ViewHolder(binding.root)

}