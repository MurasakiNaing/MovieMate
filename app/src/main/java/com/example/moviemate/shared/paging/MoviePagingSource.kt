package com.example.moviemate.shared.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviemate.shared.model.MovieCardPreview
import com.example.moviemate.shared.retrofit.TMdbApiService
import com.example.moviemate.shared.utils.Constants

class MoviePagingSource(private val apiService: TMdbApiService, private val mode: Constants.Mode, private val query: String = "", private val genreId: Int = -1) : PagingSource<Int, MovieCardPreview>() {
    override fun getRefreshKey(state: PagingState<Int, MovieCardPreview>): Int? {
        return state.anchorPosition?.let { anchor ->
            val anchorPage = state.closestPageToPosition(anchor)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieCardPreview> {
        val page = params.key ?: 1
        return try {
            val response = when(mode) {
                Constants.Mode.Browse -> {
                    if (genreId > 0) apiService.getMovies(page, genreId.toString())
                    else apiService.getMovies(page)
                }
                Constants.Mode.Search -> apiService.searchMovies(query, page)
            }
            Log.d("LOAD", response.toString())
            Log.d("LOAD", genreId.toString())
            LoadResult.Page(
                data = response.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.results.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}