package com.example.dogownerapp.presentation.viewmodel.specialists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogownerapp.domain.interactor.DogListInteractor
import com.example.dogownerapp.domain.interactor.SpecialistInteractor
import com.example.dogownerapp.domain.interactor.UserInteractor
import com.example.dogownerapp.domain.model.Dog
import com.example.dogownerapp.domain.model.Specialist
import com.example.dogownerapp.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val specialistInteractor: SpecialistInteractor
) : ViewModel() {
    private val _spec = MutableStateFlow<Specialist>(Specialist())
    var spec: StateFlow<Specialist> = _spec.asStateFlow()

    init {
        loadSpec()
    }

    private fun loadSpec() {
        viewModelScope.launch {
            specialistInteractor.loadSpecialist().collect { spec ->
                _spec.value = spec
            }
        }
    }

    fun updateSpec(spec: Specialist) {
        viewModelScope.launch {
            specialistInteractor.updateSpecialist(spec)
        }
    }

}