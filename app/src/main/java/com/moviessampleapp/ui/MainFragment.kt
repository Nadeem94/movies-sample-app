package com.moviessampleapp.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviessampleapp.R
import com.moviessampleapp.databinding.MainFragmentBinding
import com.moviessampleapp.ui.adapter.MoviesAdapter
import com.moviessampleapp.ui.view.RecyclerItemDecor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by Nadeem on 19/10/2021.
 */
@AndroidEntryPoint
class MainFragment : Fragment() {

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.vm = mainViewModel
        binding.moviesList.setup()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.run {
            movies.observe(viewLifecycleOwner) {
                moviesAdapter.setMovieList(it)
            }
            searchText.observe(viewLifecycleOwner) {
                moviesAdapter.filter.filter(it)
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
                Configuration.ORIENTATION_LANDSCAPE -> 3
                else -> 2
            }
        )
        adapter = moviesAdapter
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}