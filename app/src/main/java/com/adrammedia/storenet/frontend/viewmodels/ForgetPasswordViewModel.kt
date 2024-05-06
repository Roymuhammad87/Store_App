package com.adrammedia.storenet.frontend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrammedia.storenet.backend.data.users.passwords.ForgetPasswordRequest
import com.adrammedia.storenet.backend.data.users.passwords.ForgetPasswordResponse
import com.adrammedia.storenet.backend.repos.PasswordRepo
import com.adrammedia.storenet.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(private val passwordRepo: PasswordRepo):ViewModel() {

    private val _forgetPasswordStateFlow = MutableStateFlow<DataStatus<ForgetPasswordResponse>>(DataStatus.loading())
    val forgetPasswordStateFlow: StateFlow<DataStatus<ForgetPasswordResponse>> = _forgetPasswordStateFlow

    fun forgetPassword(email:ForgetPasswordRequest) = viewModelScope.launch {
        passwordRepo.forgetPassword(email).collect{
            _forgetPasswordStateFlow.emit(it)
        }
    }
}