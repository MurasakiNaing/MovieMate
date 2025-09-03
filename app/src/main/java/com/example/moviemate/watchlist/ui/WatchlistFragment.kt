package com.example.moviemate.watchlist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviemate.R
import com.example.moviemate.databinding.FragmentWatchlistBinding
import com.example.moviemate.shared.model.MovieCardPreview
import com.example.moviemate.shared.utils.NavigationUtils
import com.example.moviemate.watchlist.ui.adapter.WatchlistMainAdapter

class WatchlistFragment : Fragment() {

    private lateinit var binding: FragmentWatchlistBinding
    private val watchlistViewModel: WatchlistViewModel by viewModels()
    private lateinit var watchlistMainAdapter: WatchlistMainAdapter
    private val onMovieClick: (MovieCardPreview) -> Unit = {
        NavigationUtils.navigateToMovieDetails(requireContext(), it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        watchlistMainAdapter = WatchlistMainAdapter()
        watchlistMainAdapter.onClick = onMovieClick
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lm = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when(watchlistMainAdapter.getItemViewType(position)) {
                    R.layout.watchlist_tv_layout -> 2
                    else -> 1
                }
            }
        }

        binding.rvWatchlistMain.apply {
            layoutManager = lm
            adapter = watchlistMainAdapter
        }

        watchlistViewModel.getWatchlistMovies()
        watchlistViewModel.observeWatchListMoviesLiveData().observe(viewLifecycleOwner) {
            watchlistMainAdapter.submitList(it)
        }
    }
}