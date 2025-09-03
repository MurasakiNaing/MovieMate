package com.example.moviemate.home.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviemate.databinding.FragmentHomeBinding
import com.example.moviemate.genremovie.ui.GenreMoviesActivity
import com.example.moviemate.home.ui.adapters.GenreAdapter
import com.example.moviemate.home.ui.adapters.HomeMainRecyclerAdapter
import com.example.moviemate.home.ui.adapters.MovieSectionAdapter
import com.example.moviemate.home.ui.adapters.MoviesAdapter
import com.example.moviemate.shared.model.Genre
import com.example.moviemate.shared.model.MovieCardPreview
import com.example.moviemate.shared.retrofit.RetrofitInstance
import com.example.moviemate.shared.retrofit.TMdbApiService
import com.example.moviemate.shared.shimmer.ShimmerGenreCardAdapter
import com.example.moviemate.shared.shimmer.ShimmerMovieCardAdapter
import com.example.moviemate.shared.utils.NavigationUtils

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var tMdbApiService: TMdbApiService
    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(tMdbApiService)
    }
    private lateinit var movieGenreAdapter: GenreAdapter
    private lateinit var popularMoviesAdapter: MoviesAdapter
    private lateinit var nowPlayingMoviesAdapter: MoviesAdapter
    private lateinit var trendingMoviesAdapter: MoviesAdapter
    private lateinit var upcomingMoviesAdapter: MoviesAdapter

    private lateinit var mainRecyclerAdapter: HomeMainRecyclerAdapter
    private lateinit var movieConcatAdapter: ConcatAdapter

    private lateinit var popularSection: MovieSectionAdapter
    private lateinit var nowPlayingSection: MovieSectionAdapter
    private lateinit var trendingSection: MovieSectionAdapter
    private lateinit var upcomingSection: MovieSectionAdapter


    val onMovieClick: (MovieCardPreview) -> Unit =  {
        NavigationUtils.navigateToMovieDetails(requireContext(), it)
    }

    val onGenreClick: (Genre) -> Unit = {
        val intent = Intent(activity, GenreMoviesActivity::class.java)
        intent.putExtra("GENRE", it)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tMdbApiService = RetrofitInstance.api
        movieGenreAdapter = GenreAdapter()
        popularMoviesAdapter = MoviesAdapter()
        nowPlayingMoviesAdapter = MoviesAdapter()
        trendingMoviesAdapter = MoviesAdapter()
        upcomingMoviesAdapter = MoviesAdapter()
        prepareMainAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareMainRecycler()

        homeViewModel.fetchMovieGenres()
        observeMovieGenreListLiveData()

        homeViewModel.fetchPopularMovies()
        homeViewModel.fetchNowPlayingList()
        homeViewModel.fetchTrendingMovies()
        homeViewModel.fetchUpcomingMovies()

        observeAllLiveData()
        onMovieCardClicked()
    }

    private fun prepareMainAdapter() {
        popularSection = MovieSectionAdapter("Popular", ShimmerMovieCardAdapter(5))
        nowPlayingSection = MovieSectionAdapter("Now Playing", ShimmerMovieCardAdapter(5))
        trendingSection = MovieSectionAdapter("Trending", ShimmerMovieCardAdapter(5))
        upcomingSection = MovieSectionAdapter("Upcoming", ShimmerMovieCardAdapter(5))
        movieConcatAdapter =
            ConcatAdapter(popularSection, nowPlayingSection, trendingSection, upcomingSection)
        mainRecyclerAdapter = HomeMainRecyclerAdapter(ShimmerGenreCardAdapter(5), movieConcatAdapter)
    }

    private fun prepareMainRecycler() {
        binding.mainRecycler.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = mainRecyclerAdapter
        }
    }

    private fun onMovieCardClicked() {
        popularMoviesAdapter.onClick = onMovieClick
        nowPlayingMoviesAdapter.onClick = onMovieClick
        trendingMoviesAdapter.onClick = onMovieClick
        upcomingMoviesAdapter.onClick = onMovieClick
    }

    private fun observeMovieGenreListLiveData() {
        homeViewModel.observeMovieGenreListLiveData().observe(viewLifecycleOwner) {
            movieGenreAdapter.submitList(it)
        }
        movieGenreAdapter.onClick = onGenreClick
    }

    private fun observeAllLiveData() {
        homeViewModel.observePopularMoviesLiveData().observe(viewLifecycleOwner) {
            popularSection.updateAdapter(popularMoviesAdapter)
            popularMoviesAdapter.setMovies(it)
        }
        homeViewModel.observeNowPlayingLiveData().observe(viewLifecycleOwner) {
            nowPlayingSection.updateAdapter(nowPlayingMoviesAdapter)
            nowPlayingMoviesAdapter.setMovies(it)
        }
        homeViewModel.observeTrendingMovieLiveData().observe(viewLifecycleOwner) {
            trendingSection.updateAdapter(trendingMoviesAdapter)
            trendingMoviesAdapter.setMovies(it)
        }
        homeViewModel.observeUpcomingMovieLiveData().observe(viewLifecycleOwner) {
            upcomingSection.updateAdapter(upcomingMoviesAdapter)
            upcomingMoviesAdapter.setMovies(it)
        }
        homeViewModel.observeMovieGenreListLiveData().observe(viewLifecycleOwner) {
            mainRecyclerAdapter.updateGenreAdapter(movieGenreAdapter)
            movieGenreAdapter.submitList(it)
        }
    }

}