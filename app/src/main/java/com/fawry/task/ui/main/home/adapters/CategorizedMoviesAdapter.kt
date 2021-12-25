package com.fawry.task.ui.main.home.adapters

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fawry.task.data.models.entities.CategorizedMovies
import com.fawry.task.data.models.entities.Movie
import com.fawry.task.databinding.ListItemCategorizedMoviesBinding

class CategorizedMoviesAdapter(
    private val onMovieClicked: (Movie) -> Unit
) : RecyclerView.Adapter<CategorizedMoviesAdapter.ViewHolder>() {

    private var items: List<CategorizedMovies> = listOf()

    private val scrollPosition =
        hashMapOf<Int, Parcelable?>()  //save the scroll state of each category list when scrolling

    fun updateList(items: List<CategorizedMovies>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemCategorizedMoviesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).also {    //set the adapter here not in onBind for better performance
            it.binding.moviesList.adapter = MoviesAdapter()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.genre.text = items[position].category.name
        with(holder.binding.moviesList.adapter as MoviesAdapter) {
            updateData(items[position].movies, onMovieClicked)
            notifyDataSetChanged()
        }
        scrollPosition[position]?.let {
            holder.binding.moviesList.layoutManager?.onRestoreInstanceState(it)
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        scrollPosition[holder.adapterPosition] =
            holder.binding.moviesList.layoutManager?.onSaveInstanceState()
    }

    override fun getItemCount() = items.size


    class ViewHolder(val binding: ListItemCategorizedMoviesBinding) :
        RecyclerView.ViewHolder(binding.root)

}