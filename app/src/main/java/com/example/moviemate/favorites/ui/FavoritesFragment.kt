package com.example.moviemate.favorites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviemate.R
import com.example.moviemate.databinding.FragmentFavoritesBinding
import com.example.moviemate.favorites.ui.adapter.FavoritesMainAdapter
import com.example.moviemate.shared.model.MovieCardPreview
import com.example.moviemate.shared.retrofit.RetrofitInstance
import com.example.moviemate.shared.retrofit.TMdbApiService
import com.example.moviemate.shared.utils.NavigationUtils

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var apiService: TMdbApiService
    private lateinit var favoritesMainAdapter: FavoritesMainAdapter
    private val viewModel : FavoritesViewModel by viewModels {
        FavoritesViewModelFactory(apiService)
    }
    private val onMovieClick: (MovieCardPreview) -> Unit = {
        NavigationUtils.navigateToMovieDetails(requireContext(), it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiService = RetrofitInstance.api
        favoritesMainAdapter = FavoritesMainAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesMainAdapter.onClick = onMovieClick

        val lm = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when(favoritesMainAdapter.getItemViewType(position)) {
                    R.layout.favorites_tv_layout -> 2
                    else -> 1
                }
            }
        }

        binding.rvFavoritesMain.apply {
            layoutManager = lm
            adapter = favoritesMainAdapter
        }

        viewModel.getFavoriteMovies()
        viewModel.observeFavoriteItemsLiveData().observe(viewLifecycleOwner) {
            favoritesMainAdapter.submitList(it)
        }

    }
}