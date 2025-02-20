package com.zaed.cashier.ui.loss

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.loss.Loss
import com.zaed.common.data.model.loss.request.CreateNewLossRequest
import com.zaed.common.data.model.loss.request.DeleteLossRequest
import com.zaed.common.data.model.loss.request.GetStoreLossesRequest
import com.zaed.common.data.model.loss.request.UpdateLossRequest
import com.zaed.common.domain.loss.CreateNewLossUseCase
import com.zaed.common.domain.loss.DeleteLossUseCase
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.loss.GetStoreLossesUseCase
import com.zaed.common.domain.authentication.LogoutUserUseCase
import com.zaed.common.domain.loss.UpdateLossUseCase
import com.zaed.common.ui.util.DateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LossViewModel(
    private val createNewLossUseCase: CreateNewLossUseCase,
    private val updateLossUseCase: UpdateLossUseCase,
    private val deleteLossUseCase: DeleteLossUseCase,
    private val getStoreLossesUseCase: GetStoreLossesUseCase,
    private val getCurrentUserUseCase: GetCurrentUserLoggedInUseCase,
    private val logOutUseCase: LogoutUserUseCase
) : ViewModel() {
    private val TAG: String = "LossViewModel"
    private val _uiState = MutableStateFlow(LossUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUserUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(currentUser = data)
                    }
                    getAllLosses()
                }.onFailure { e ->
                    Log.e(TAG, "fetchCurrentUser: ${e.message}", e)
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getAllLosses() {
        viewModelScope.launch(Dispatchers.IO) {
            getStoreLossesUseCase(GetStoreLossesRequest(storeId = uiState.value.currentUser.storeId)).collect { result ->
                result.onSuccess { data ->
                    Log.d(TAG, "getAllLosses: ${data.map { it.value.map { it.id } }}")
                    _uiState.update { it.copy(losses = data) }
                }.onFailure { error ->
                    _uiState.update { it.copy(errorMessage = 0) }
                }
            }
        }
    }


    fun handleAction(action: LossUiAction) {
        when (action) {
            is LossUiAction.OnCreateLoss -> submitNewLoss(
                loss = action.loss
            )

            is LossUiAction.OnUpdateLoss -> updateLoss(action.loss)
            is LossUiAction.OnDeleteLoss -> deleteLoss(action.id)
            LossUiAction.ResetError -> resetError()
            LossUiAction.ResetSuccess -> resetSuccessState()
            LossUiAction.OnSignOut -> signOut()
            else -> {}
        }
    }

    private fun signOut() {
        viewModelScope.launch (Dispatchers.IO){
            logOutUseCase().onSuccess {
                _uiState.update { oldState ->
                    oldState.copy(isSignedOut = true)
                }
                Log.d(TAG, "signOut: success")
            }.onFailure {
                Log.e(TAG, "signOut: ${it.message}", it)
                it.printStackTrace()
            }
        }
    }

    private fun updateLoss(loss: Loss) {
        if (!validateInput(loss)) return
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            val originalLoss = uiState.value.losses[SimpleDateFormat(
                DateFormat.DATE.pattern,
                Locale.getDefault()
            ).format(loss.date)]?.find { it.id == loss.id } ?: return@launch
            val logMessage = if (originalLoss.value != loss.value) {
                "${uiState.value.currentUser.fullName} updated the value from ${originalLoss.value} to ${loss.value}}"
            } else if (originalLoss.reason != loss.reason) {
                "${uiState.value.currentUser.fullName} updated the reason from ${originalLoss.reason} to ${loss.reason}}"
            } else {
                "${uiState.value.currentUser.fullName} updated this loss"
            }
            _uiState.update { it.copy(isLoading = true) }
            updateLossUseCase(
                UpdateLossRequest(
                    loss = loss.copy(
                        logs = loss.logs + ChangeLog(
                            date = Date(),
                            employeeId = uiState.value.currentUser.id,
                            employeeName = uiState.value.currentUser.fullName,
                            action = logMessage
                        )
                    )
                )
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Loss updated successfully"
                    )
                }
            }.onFailure {
                _uiState.update { it.copy(isLoading = false, errorMessage = 0) }
            }
        }
    }

    private fun deleteLoss(id: String) {
        Log.d(TAG, "deleteLoss: $id")
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            deleteLossUseCase(
                DeleteLossRequest(
                    lossId = id,
                    employeeId = uiState.value.currentUser.id,
                    employeeName = uiState.value.currentUser.fullName
                )
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Loss deleted successfully"
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = 0
                    )
                }
            }
        }
    }


    private fun submitNewLoss(
        loss: Loss
    ) {
        if (!validateInput(loss)) return
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            createNewLossUseCase(
                CreateNewLossRequest(
                    loss.copy(
                        date = Date(),
                        userId = uiState.value.currentUser.id,
                        userName = uiState.value.currentUser.fullName,
                        storeId = uiState.value.currentUser.storeId,
                        storeName = uiState.value.currentUser.storeName,
                    )
                )
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Loss created successfully"
                    )
                }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = 0) }
            }
        }
    }

    private fun validateInput(
        loss: Loss
    ): Boolean {
        when {
            loss.reason.isEmpty() -> {
                _uiState.update { it.copy(fieldError = LossFieldsError.LOSS_REASON_IS_EMPTY) }
                return false
            }

            else -> {
                _uiState.update { it.copy(fieldError = LossFieldsError.NONE) }
                return true
            }
        }
    }

    private fun resetSuccessState() {
        _uiState.update { it.copy(successMessage = null) }
    }

    private fun resetError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }
}