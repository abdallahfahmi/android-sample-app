package com.fawry.task.ui.main.home.adapters

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fawry.task.data.models.CategorizedMovies
import com.fawry.task.data.models.Movie
import com.fawry.task.databinding.GenresListItemBinding

class GenresAdapter(
    private val onMovieClicked: (Movie) -> Unit
) : RecyclerView.Adapter<GenresAdapter.ViewHolder>() {

    private var items: List<CategorizedMovies> = listOf()

    private val scrollPosition =
        hashMapOf<Int, Parcelable?>()  //save the scroll state of each genre list when scrolling

    fun updateList(items: List<CategorizedMovies>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            GenresListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ).also {    //set the adapter here not in onBind to avoid performance issues
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


    class ViewHolder(val binding: GenresListItemBinding) : RecyclerView.ViewHolder(binding.root)

}