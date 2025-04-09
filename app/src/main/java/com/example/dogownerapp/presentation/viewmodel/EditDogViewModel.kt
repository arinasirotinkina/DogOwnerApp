package com.example.dogownerapp.presentation.viewmodel

import SaveImageService
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogownerapp.domain.interactor.DogListInteractor
import com.example.dogownerapp.domain.model.Dog
import com.example.dogownerapp.domain.model.Treatment
import com.example.dogownerapp.domain.model.Vaccination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditDogViewModel @Inject constructor(
    private val dogListInteractor: DogListInteractor
): ViewModel() {
    private val _dog = MutableStateFlow<Dog>(Dog())
    var dog: StateFlow<Dog> = _dog.asStateFlow()

    private val _vaccines = MutableStateFlow<List<Vaccination>>(emptyList())
    private val _treatments = MutableStateFlow<List<Treatment>>(emptyList())
    val vaccines: StateFlow<List<Vaccination>> = _vaccines
    val treatments: StateFlow<List<Treatment>> = _treatments

    fun getDogbyId(dogId: String)  {
        viewModelScope.launch {
            dogListInteractor.getDogById(dogId).collect { dog ->
                _dog.value = dog
                _vaccines.value = dog.vaccinations
                _treatments.value = dog.treatments
            }
        }
    }

    fun addVaccine(name: String, date: String) {
        _vaccines.value = listOf(Vaccination(name, date)) + _vaccines.value
    }

    fun removeVaccine(vaccine: Vaccination) {
        _vaccines.value -= vaccine
    }

    fun addTreatment(name: String, date: String) {
        _treatments.value = listOf(Treatment(name, date)) + _treatments.value
    }

    fun removeTreatment(treatment: Treatment) {
        _treatments.value -= treatment
    }
    fun clear() {
        _dog.value = Dog()
        _vaccines.value = mutableListOf()
        _treatments.value = mutableListOf()
    }
    fun uploadPhoto(uri: Uri, context: Context, dogId: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                val imS = SaveImageService()
                imS.uploadFileToFTP(uri, context, "dogs", dogId)
                onComplete()
            } catch (e: Exception) {
                Log.e("UploadPhoto", "Ошибка загрузки фото", e)
            }
        }
    }
}


