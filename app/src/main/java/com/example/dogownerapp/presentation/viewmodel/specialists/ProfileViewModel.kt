package com.example.dogownerapp.presentation.viewmodel.specialists

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogownerapp.domain.interactor.SpecialistInteractor
import com.example.dogownerapp.domain.model.Specialist
import com.example.dogownerapp.domain.repository.SaveImageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val specialistInteractor: SpecialistInteractor,
    private val imageService: SaveImageService
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
    fun uploadPhoto(uri: Uri, context: Context, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                imageService.uploadFileToFTP(uri, context, "specs", _spec.value.id)
                onComplete()
            } catch (e: Exception) {
                Log.e("UploadPhoto", "Ошибка загрузки фото", e)
            }
        }
    }
}