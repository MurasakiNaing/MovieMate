package com.example.moviemate.browse.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviemate.browse.ui.adapter.BrowseMoviesAdapter
import com.example.moviemate.databinding.FragmentBrowseBinding
import com.example.moviemate.details.ui.MovieDetailsActivity
import com.example.moviemate.shared.model.MovieCardPreview
import com.example.moviemate.shared.retrofit.RetrofitInstance
import com.example.moviemate.shared.retrofit.TMdbApiService
import com.example.moviemate.shared.utils.NavigationUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BrowseFragment : Fragment() {

    private lateinit var binding: FragmentBrowseBinding
    private lateinit var apiService: TMdbApiService
    private lateinit var browseViewModel: BrowseMovieViewModel
    private lateinit var browseMoviesAdapter: BrowseMoviesAdapter

    private val onMovieClick: (MovieCardPreview) -> Unit = {
        NavigationUtils.navigateToMovieDetails(requireContext(), it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiService = RetrofitInstance.api
        browseViewModel = ViewModelProvider(this, BrowseMovieViewModelFactory(apiService)).get(
            BrowseMovieViewModel::class
        )
        browseMoviesAdapter = BrowseMoviesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBrowseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMainBrowse.apply {
            val lm = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adapter?.getItemViewType(position)) {
                        BrowseMoviesAdapter.Companion.TV_BROWSE_MOVIES -> 2
                        else -> 1
                    }
                }

            }
            layoutManager = lm
            setHasFixedSize(true)
            adapter = browseMoviesAdapter
        }

        onMovieCardClicked()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                browseViewModel.movies.collectLatest { pagingData ->
                    browseMoviesAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
                }
            }
        }
    }

    private fun onMovieCardClicked() {
        browseMoviesAdapter.onClick = onMovieClick
    }
}