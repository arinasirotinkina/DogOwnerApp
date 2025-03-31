package com.example.dogownerapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogownerapp.domain.interactor.RecommendationInteractor
import com.example.dogownerapp.domain.interactor.SearchSpecInteractor
import com.example.dogownerapp.domain.model.Recommendation
import com.example.dogownerapp.domain.model.Specialist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SpecsViewModel @Inject constructor(
    private val searchSpecInteractor: SearchSpecInteractor
) : ViewModel() {

    private val _specs = MutableStateFlow<List<Specialist>>(emptyList())
    val specs: StateFlow<List<Specialist>> = _specs.asStateFlow()

    private val _specsToShow = MutableStateFlow<List<Specialist>>(emptyList())
    val specsToShow: StateFlow<List<Specialist>> = _specsToShow.asStateFlow()


    init {
        loadSpecs()
    }


    private fun loadSpecs() {
        viewModelScope.launch {
            searchSpecInteractor.loadSpecs().collect { specList ->
                _specs.value = specList
                _specsToShow.value = specList
            }
        }
    }

    fun getSpecById(specId: String): Specialist? {
        return _specs.value.find { it.id == specId }
    }


}

