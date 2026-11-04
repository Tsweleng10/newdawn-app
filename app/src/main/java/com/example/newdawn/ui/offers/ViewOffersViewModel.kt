package com.example.newdawn.ui.offers

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.newdawn.data.models.Offer
import com.example.newdawn.data.remote.RetrofitInstance
import com.example.newdawn.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ViewOffersUiState(
    val offers: List<Offer> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class ViewOffersViewModel(app: Application) : AndroidViewModel(app) {
    private val TAG = "ViewOffersVM"
    private val tokenManager = TokenManager(app.applicationContext)
    private val _uiState = MutableStateFlow(ViewOffersUiState())
    val uiState: StateFlow<ViewOffersUiState> = _uiState

    fun load(jobId: Int) {
        _uiState.value = ViewOffersUiState(isLoading = true)
        viewModelScope.launch {
            try {
                val auth = tokenManager.getAuthHeader()
                val res = RetrofitInstance.api.getOffersForJob(auth, jobId)
                if (res.isSuccessful) {
                    Log.d(TAG, "Loaded ${res.body()?.size ?: 0} offers")
                    _uiState.value = ViewOffersUiState(
                        offers = res.body() ?: emptyList(),
                        isLoading = false
                    )
                } else {
                    _uiState.value = ViewOffersUiState(
                        isLoading = false,
                        error = "Failed (${res.code()})"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error", e)
                _uiState.value = ViewOffersUiState(isLoading = false, error = e.message)
            }
        }
    }

    fun accept(offerId: Int, jobId: Int) {
        viewModelScope.launch {
            try {
                val auth = tokenManager.getAuthHeader()
                val res = RetrofitInstance.api.acceptOffer(auth, offerId)
                if (res.isSuccessful) {
                    Log.d(TAG, "Offer accepted")
                    load(jobId)
                } else {
                    _uiState.value = _uiState.value.copy(error = "Failed (${res.code()})")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
}