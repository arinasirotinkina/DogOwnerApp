package com.example.dogownerapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogownerapp.domain.interactor.RecommendationInteractor
import com.example.dogownerapp.domain.interactor.SearchSpecInteractor
import com.example.dogownerapp.domain.model.Recommendation
import com.example.dogownerapp.domain.model.Specialist
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.location.Location


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

    fun filterBySpecialization(geoPoint: GeoPoint, specialization: String) {

        _specsToShow.value = specs.value.sortedBy { getDistance(geoPoint, it.location) }.filter { (it.specialization == specialization)
        }
    }
    fun showFavourites(geoPoint: GeoPoint, favourites: List<String>) {
        _specsToShow.value = specs.value.sortedBy { getDistance(geoPoint, it.location) }.filter { it.id in favourites }
    }

    fun findCloserSpecs(geoPoint: GeoPoint) {
        _specsToShow.value = specsToShow.value.filter { getDistance(geoPoint,it.location) < 10000 }

    }

    private fun getDistance(
        userLocation: GeoPoint,
        specialistLocation: GeoPoint
    ): Float {
        val loc1 = Location("User")
        val loc2 = Location("Other")
        loc1.latitude = userLocation.latitude
        loc1.longitude = userLocation.longitude
        loc2.latitude = specialistLocation.latitude
        loc2.longitude = specialistLocation.longitude
        return (loc1.distanceTo(loc2))
    }


}

