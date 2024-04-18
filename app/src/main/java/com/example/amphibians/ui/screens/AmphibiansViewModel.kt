package com.example.amphibians.ui.screens


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.amphibians.AmphibiansPhotosApplication
import com.example.amphibians.data.AmphibiansPhotosRepository
import com.example.amphibians.model.AmphibiansPhoto
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * UI state for the Home screen
 */
sealed interface AmphibiansUiState {
    data class Success(val photos: List<AmphibiansPhoto>) : AmphibiansUiState
    object Error : AmphibiansUiState
    object Loading : AmphibiansUiState
}

class AmphibiansViewModel (private val amphibiansPhotosRepository: AmphibiansPhotosRepository) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as AmphibiansPhotosApplication)
                val amphibiansPhotosRepository = application.container.amphibiansPhotosRepository
                AmphibiansViewModel(amphibiansPhotosRepository = amphibiansPhotosRepository)
            }
        }
    }


    /** The mutable State that stores the status of the most recent request */
    var amphibiansUiState: AmphibiansUiState by mutableStateOf(AmphibiansUiState.Loading)
        private set

    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getAmphibiansPhotos()
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [MutableList].
     */
    fun getAmphibiansPhotos() {
        viewModelScope.launch {
            amphibiansUiState = AmphibiansUiState.Loading
            amphibiansUiState = try {
                val result = amphibiansPhotosRepository.getAmphibiansPhotos()

                AmphibiansUiState.Success(
                    amphibiansPhotosRepository.getAmphibiansPhotos()
                )
            } catch (e: IOException) {
                AmphibiansUiState.Error
            } catch (e: HttpException) {
                AmphibiansUiState.Error
            }
        }
    }


}