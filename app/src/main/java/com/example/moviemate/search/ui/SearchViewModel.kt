package com.example.moviemate.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertHeaderItem
import androidx.paging.map
import com.example.moviemate.search.ui.adapters.SearchViewItems
import com.example.moviemate.shared.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class SearchViewModel(val repository: MovieRepository) : ViewModel() {
    private val searchQuery = MutableStateFlow("")

    val searchMoviesFlow: Flow<PagingData<SearchViewItems>> = searchQuery
        .debounce(500)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                flowOf(PagingData.from(listOf(SearchViewItems.SearchBar as SearchViewItems)))
            } else {
                repository.getSearchMoviePager(query).map {
                    it.map { movie -> SearchViewItems.MovieCard(movie) as SearchViewItems }
                        .insertHeaderItem(item = SearchViewItems.SearchBar)
                }
            }
        }.cachedIn(viewModelScope)


    fun searchMovies(query: String) {
        searchQuery.value = query
    }
}