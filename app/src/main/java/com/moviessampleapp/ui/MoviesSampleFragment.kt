package com.moviessampleapp.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviessampleapp.R
import com.moviessampleapp.data.Movie
import com.moviessampleapp.databinding.MoviesSampleFragmentBinding
import com.moviessampleapp.ui.adapter.MoviesAdapter
import com.moviessampleapp.ui.adapter.OnMovieItemClickListener
import com.moviessampleapp.ui.view.RecyclerItemDecor
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Nadeem on 19/10/2021.
 */
@AndroidEntryPoint
class MoviesSampleFragment : Fragment(), OnMovieItemClickListener {

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    private val mainViewModel by viewModels<MoviesSampleViewModel>()

    private var toast: Toast? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = MoviesSampleFragmentBinding.inflate(inflater, container, false)
        binding.vm = mainViewModel
        binding.setup()
        return binding.root
    }

    private fun MoviesSampleFragmentBinding.setup() {
        moviesList.setup()
        mainViewModel.run {
            movies.observe(viewLifecycleOwner) {
                moviesAdapter.setMovieList(it)
            }
            searchText.observe(viewLifecycleOwner) {
                it?.let { moviesAdapter.filter.filter(it) }
            }
            errorMessage.observe(viewLifecycleOwner) {
                it?.let(::showToast)
            }
            moviesSwipeRefresh.apply {
                isLoading.observe(viewLifecycleOwner) {
                    isRefreshing = mainViewModel.isLoading.value ?: false
                }
                setOnRefreshListener { retrieveSampleMovies() }
            }
            retrieveSampleMovies()
        }
    }

    private fun RecyclerView.setup() {
        addItemDecoration(
            RecyclerItemDecor(
                resources.getDimension(R.dimen.recycler_item_spacing).toInt()
            )
        )
        layoutManager = GridLayoutManager(
            context,
            when (resources.configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> DEFAULT_GRID_COLUMNS
                Configuration.ORIENTATION_LANDSCAPE -> LANDSCAPE_GRID_COLUMNS
                else -> DEFAULT_GRID_COLUMNS
            }
        )
        adapter = moviesAdapter
    }

    override fun onMovieItemClick(movie: Movie) {
        Timber.d("Movie item clicked - id: ${movie.id}, title: ${movie.title}")
        showToast(getString(R.string.movie_title_and_year, movie.title, movie.year))
    }

    private fun showToast(msg: String) {
        toast?.cancel()
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        toast?.show()
    }

    companion object {
        private const val DEFAULT_GRID_COLUMNS = 3
        private const val LANDSCAPE_GRID_COLUMNS = 5
        fun newInstance() = MoviesSampleFragment()
    }
}