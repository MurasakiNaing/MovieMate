package com.example.moviemate.search.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviemate.databinding.FragmentSearchBinding
import com.example.moviemate.details.ui.MovieDetailsActivity
import com.example.moviemate.shared.retrofit.RetrofitInstance
import com.example.moviemate.shared.retrofit.TMdbApiService
import com.example.moviemate.search.ui.adapters.SearchMainAdapter
import com.example.moviemate.search.ui.adapters.SearchViewItems
import com.example.moviemate.shared.model.MovieCardPreview
import com.example.moviemate.shared.utils.NavigationUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding
    private lateinit var searchMainAdapter: SearchMainAdapter

    private lateinit var apiService: TMdbApiService
    private lateinit var searchViewModel: SearchViewModel

    private val onMovieClick: (MovieCardPreview) -> Unit = {
        NavigationUtils.navigateToMovieDetails(requireContext(), it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiService = RetrofitInstance.api
        searchViewModel = ViewModelProvider(this, SearchViewModelFactory(apiService)).get(
            SearchViewModel::class)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchMainAdapter = SearchMainAdapter {
            searchViewModel.searchMovies(it)
        }

        searchMainAdapter.onClick = onMovieClick

        val lm = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when(position) {
                    SearchMainAdapter.VIEW_TYPE_SEARCH -> 2
                    else -> 1
                }
            }
        }

        binding.rvSearchMain.apply {
            layoutManager = lm
            adapter = searchMainAdapter
        }

        lifecycleScope.launch {
            searchViewModel.searchMoviesFlow.collectLatest {
                searchMainAdapter.submitData(it)
            }
        }
    }
}