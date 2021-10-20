package com.moviessampleapp.ui.adapter

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import com.moviessampleapp.R
import com.moviessampleapp.data.Movie
import com.moviessampleapp.databinding.MovieItemBinding
import com.moviessampleapp.ui.MoviesSampleFragment
import javax.inject.Inject

/**
 * Created by Nadeem on 19/10/2021.
 */
class MoviesAdapter @Inject constructor(private val imageLoader: ImageLoader) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>(), Filterable {

    private val fullMovieList: MutableList<Movie> = mutableListOf()
    private val adapterItems: MutableList<Movie> = mutableListOf()

    fun setMovieList(movies: List<Movie>) {
        fullMovieList.clear()
        fullMovieList.addAll(movies)
        setData(fullMovieList)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setData(data: List<Movie>) {
        adapterItems.clear()
        adapterItems.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(MovieItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(adapterItems[position])
    }

    override fun getItemCount(): Int = adapterItems.size

    override fun getItemId(position: Int): Long = adapterItems[position].id

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                return constraint?.toString()?.let { searchText ->
                    val filtered = fullMovieList.filter {
                        it.title.contains(searchText, ignoreCase = true) ||
                                it.genre.contains(searchText, ignoreCase = true)
                    }
                    FilterResults().apply {
                        values = filtered
                        count = filtered.size
                    }
                } ?: FilterResults()
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
                filterResults?.takeIf { it.count > 0 }?.let {
                    setData(it.values as List<Movie>)
                }
            }
        }
    }

    inner class MovieViewHolder(
        private val binding: MovieItemBinding,
        private val resources: Resources = binding.root.resources
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) = movie.run {
            binding.titleYear.text = resources.getString(R.string.movie_title_and_year, title, year)
            binding.genre.text = genre
            binding.image.load(poster, imageLoader) {
                error(R.drawable.ic_movie_placeholder)
                placeholder(R.drawable.ic_movie_placeholder)
            }
            binding.root.setOnClickListener {
                it.findFragment<MoviesSampleFragment>().onMovieItemClick(this)
            }
        }
    }
}