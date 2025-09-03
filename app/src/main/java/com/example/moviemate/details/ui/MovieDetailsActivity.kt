package com.example.moviemate.details.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moviemate.R
import com.example.moviemate.databinding.ActivityMovieDetailsBinding
import com.example.moviemate.details.model.Movie
import com.example.moviemate.details.model.ReleaseDate
import com.example.moviemate.details.ui.adapters.CastAdapter
import com.example.moviemate.shared.model.MovieCardPreview
import com.example.moviemate.shared.retrofit.RetrofitInstance
import com.example.moviemate.shared.retrofit.TMdbApiService
import com.example.moviemate.shared.utils.Constants
import kotlin.math.round

class MovieDetailsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var movieApiService: TMdbApiService
    private val movieDetailsViewModel : MovieDetailsViewModel by viewModels {
        MovieDetailsViewModelFactory(movieApiService)
    }
    private lateinit var movie: Movie

    private lateinit var trailer: String

    private lateinit var castListAdapter: CastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val toolbar = binding.toolbar
        toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.white))
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val movieId = intent.getIntExtra("MOVIE_ID", -1)

        movieApiService = RetrofitInstance.api
        movieDetailsViewModel.fetchMovieDetails(movieId)
        observeMovieDetailsLiveData()

        movieDetailsViewModel.fetchMovieReleaseDetails(movieId)
        observeMovieReleaseDetailsLiveData()

        prepareCastRecyclerView()
        movieDetailsViewModel.fetchMovieCastList(movieId)
        observerMovieCastListLiveData()

        movieDetailsViewModel.fetchMovieTrailer(movieId)
        observeMovieTrailerLiveData()

        setOnClickListeners()
    }

    private fun setFavoriteState() {
        if (movieDetailsViewModel.isMovieFavorite(movie.id)) {
            binding.buttonFav.imageTintList = ColorStateList.valueOf(Color.RED)
        } else {
            binding.buttonFav.imageTintList = null
        }
    }

    private fun setWatchlistState() {
        if (movieDetailsViewModel.isMovieWatchlist(movie.id)) {
            binding.buttonWatchlist.imageTintList = ColorStateList.valueOf(Color.parseColor("#2979FF"))
        } else {
            binding.buttonWatchlist.imageTintList = ColorStateList.valueOf(Color.WHITE)
        }
    }

    private fun observeMovieTrailerLiveData() {
        movieDetailsViewModel.observeMovieTrailerLiveData().observe(this) {
            trailer = it
        }
    }

    private fun setOnClickListeners() {
        binding.buttonFav.setOnClickListener(this)
        binding.buttonWatchlist.setOnClickListener(this)
        binding.buttonPlayTrailer.setOnClickListener(this)
    }

    private fun observerMovieCastListLiveData() {
        movieDetailsViewModel.observeMovieCastList().observe(this) {
            castListAdapter.submitList(it.filter { cast -> cast.order < 9 })
        }
    }

    private fun prepareCastRecyclerView() {
        castListAdapter = CastAdapter()
        binding.rvCasts.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = castListAdapter
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                    onBackPressedDispatcher.onBackPressed()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun observeMovieDetailsLiveData() {
        movieDetailsViewModel.observeMovieDetailsLiveData().observe(this) {
            movie = it
            setInformationOnView()
            setFavoriteState()
            setWatchlistState()
        }
    }

    private fun observeMovieReleaseDetailsLiveData() {
        movieDetailsViewModel.observeMovieReleaseDetailsLiveData().observe(this) {
            setReleaseDetailsOnView(it)
        }
    }

    private fun setInformationOnView() {
        Glide.with(this)
            .load(Constants.IMAGE_BASE_PATH + movie.backdrop_path)
            .into(binding.imageBackdrop)

        Glide.with(this)
            .load(Constants.IMAGE_BASE_PATH + movie.poster_path)
            .into(binding.imageMovie)

        binding.collapsingToolbar.title = movie.title
        binding.collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white))
        binding.tvMovieTitle.text = getString(R.string.movie_title_with_year, movie.title, movie.release_date.split("-")[0])
        binding.tvCountry.text = movie.origin_country.toString()
        binding.tvRuntime.text = getString(R.string.movie_runtime, formatTime(movie.runtime))
        binding.tvGenres.text = movie.genres.joinToString(", ") { it.name }
        binding.tvTagline.text = movie.tagline
        binding.tvOverview.text = movie.overview

        val rating = round(movie.vote_average * 10).toInt()
        binding.ratingIndicator.progress = rating
        binding.tvUserScore.text = getString(R.string.user_rating, rating)
        binding.tvLanguage.text = getString(R.string.language, movie.original_language)
        binding.tvStatus.text = getString(R.string.status, movie.status)
        binding.tvRevenue.text = getString(R.string.revenue, String.format( "%,d", movie.revenue))
        binding.tvBudget.text = getString(R.string.budget, String.format( "%,d", movie.budget))
    }

    private fun MovieDetailsActivity.setReleaseDetailsOnView(releaseDateDetails: ReleaseDate) {
        val releaseDate = releaseDateDetails.release_date.split("T")[0]
        binding.tvReleaseDate.text = releaseDate
        if (releaseDateDetails.certification.isBlank()) {
            binding.certificateBadge.text = getString(R.string.unrated_movie)
        } else {
           binding.certificateBadge.text = releaseDateDetails.certification
        }
    }

    private fun formatTime(runtime: Int): String {
        val hours = runtime / 60
        val mins = runtime % 60
        return "${hours}h ${mins}min"
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.buttonFav -> {
                val movieCard = MovieCardPreview(movie.id, movie.poster_path)
                movieDetailsViewModel.toggleFavorites(movieCard, this)
                setFavoriteState()
            }
            binding.buttonPlayTrailer -> {
                if (trailer.isNotBlank()) {
                    val intent = Intent(Intent.ACTION_VIEW, "https://www.youtube.com/watch?v=$trailer".toUri())
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Trailer is not available", Toast.LENGTH_SHORT).show()
                }
            }
            binding.buttonWatchlist -> {
                val movieCard = MovieCardPreview(movie.id, movie.poster_path)
                movieDetailsViewModel.toggleWatchlist(movieCard,this)
                setWatchlistState()
            }
        }
    }
}