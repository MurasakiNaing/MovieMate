package com.example.moviemate.genremovie.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviemate.R
import com.example.moviemate.databinding.ActivityGenreMoviesBinding
import com.example.moviemate.genremovie.ui.adapters.GenreMoviesAdapter
import com.example.moviemate.shared.model.Genre
import com.example.moviemate.shared.model.MovieCardPreview
import com.example.moviemate.shared.retrofit.RetrofitInstance
import com.example.moviemate.shared.retrofit.TMdbApiService
import com.example.moviemate.shared.utils.NavigationUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class GenreMoviesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenreMoviesBinding
    private lateinit var genreMoviesAdapter: GenreMoviesAdapter
    private lateinit var apiService: TMdbApiService
    private lateinit var genre: Genre
    private lateinit var genreMoviesViewModel: GenreMoviesViewModel

    private val onMovieClick: (MovieCardPreview) -> Unit = {
        NavigationUtils.navigateToMovieDetails(this, it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGenreMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        apiService = RetrofitInstance.api
        genre = intent.getParcelableExtra("GENRE", Genre::class.java)!!
        genreMoviesViewModel = ViewModelProvider(this, GenreMoviesViewModelFactory(apiService, genre.id)).get(
            GenreMoviesViewModel::class)

        genreMoviesAdapter = GenreMoviesAdapter(genre.name)
        genreMoviesAdapter.onClick = onMovieClick

        val lm = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when(genreMoviesAdapter.getItemViewType(position)) {
                    R.layout.genre_movies_tv_layout -> 2
                    else -> 1
                }
            }
        }

        binding.rvGenreMovie.apply {
            layoutManager = lm
            adapter = genreMoviesAdapter
        }

        lifecycleScope.launch {
            genreMoviesViewModel.movies.collectLatest {
                genreMoviesAdapter.submitData(it)
            }
        }


    }
}