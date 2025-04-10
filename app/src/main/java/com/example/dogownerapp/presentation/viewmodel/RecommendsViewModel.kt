package com.example.dogownerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogownerapp.domain.interactor.RecommendationInteractor
import com.example.dogownerapp.domain.model.Recommendation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendsViewModel @Inject constructor(
    private val recommendationInteractor: RecommendationInteractor
) : ViewModel() {

    private val _recs = MutableStateFlow<List<Recommendation>>(emptyList())
    val recs: StateFlow<List<Recommendation>> = _recs.asStateFlow()

    private val _recsToShow = MutableStateFlow<List<Recommendation>>(emptyList())
    val recsToShow: StateFlow<List<Recommendation>> = _recsToShow.asStateFlow()


    init {
        loadRecs()
    }

    private fun loadRecs() {
        viewModelScope.launch {
            recommendationInteractor.loadRecommends().collect { recList ->
                _recs.value = recList
                _recsToShow.value = recList
            }
        }
    }
    fun getRecById(recId: String) : Recommendation? {
        return _recs.value.find { it.id == recId }
    }
    fun filterByTags(health: Boolean, breeds: Boolean, care: Boolean,
                     nutrition: Boolean, education: Boolean) {

        _recsToShow.value = recs.value.filter { (check(health, it.health)
                || check(breeds, it.breeds) || check(care, it.care)
                || check(nutrition, it.nutrition) || check(education, it.education))
        }
    }
    private fun check(tag: Boolean, recTag: Boolean) : Boolean {
        return if (tag) {
            recTag
        } else {
            false
        }
    }
}