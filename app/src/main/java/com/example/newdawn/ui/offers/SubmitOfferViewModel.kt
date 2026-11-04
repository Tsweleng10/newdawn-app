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

data class SubmitOfferUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val submitted: Boolean = false
)

class SubmitOfferViewModel(app: Application) : AndroidViewModel(app) {
    private val TAG = "SubmitOfferVM"
    private val tokenManager = TokenManager(app.applicationContext)
    private val _uiState = MutableStateFlow(SubmitOfferUiState())
    val uiState: StateFlow<SubmitOfferUiState> = _uiState

    fun submit(jobId: Int, price: String, message: String, availability: String) {
        val priceVal = price.toDoubleOrNull()
        if (priceVal == null || priceVal <= 0) {
            _uiState.value = SubmitOfferUiState(error = "Enter a valid price")
            return
        }
        if (message.isBlank()) {
            _uiState.value = SubmitOfferUiState(error = "Message cannot be empty")
            return
        }
        _uiState.value = SubmitOfferUiState(isLoading = true)
        viewModelScope.launch {
            try {
                val auth = tokenManager.getAuthHeader()
                Log.d(TAG, "Submitting offer for job $jobId")
                val res = RetrofitInstance.api.submitOffer(
                    auth, jobId,
                    Offer(job_id = jobId, proposed_price = priceVal,
                        message = message, availability = availability)
                )
                if (res.isSuccessful) {
                    Log.d(TAG, "Offer submitted successfully")
                    _uiState.value = SubmitOfferUiState(submitted = true)
                } else {
                    val err = when (res.code()) {
                        409 -> "You already submitted an offer for this job"
                        400 -> "Cannot apply to your own job"
                        else -> "Failed (${res.code()})"
                    }
                    _uiState.value = SubmitOfferUiState(error = err)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error submitting offer", e)
                _uiState.value = SubmitOfferUiState(error = "Network error: ${e.message}")
            }
        }
    }
}