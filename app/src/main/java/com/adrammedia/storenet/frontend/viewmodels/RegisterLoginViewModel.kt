package com.adrammedia.storenet.frontend.viewmodels


import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrammedia.storenet.backend.api.user.RegisterLoginApiApi
import com.adrammedia.storenet.backend.data.users.login.LoginResponse
import com.adrammedia.storenet.backend.data.users.register.RegisterResponse
import com.adrammedia.storenet.backend.repos.RegisterLoginRepo
import com.adrammedia.storenet.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterLoginViewModel @Inject constructor(private val registerLoginRepo: RegisterLoginRepo) : ViewModel() {
        private val _registerStateFlow = MutableStateFlow<DataStatus<RegisterResponse>>(DataStatus.loading())
        val registerStateFlow:StateFlow<DataStatus<RegisterResponse>> = _registerStateFlow

    @SuppressLint("SuspiciousIndentation")
    fun register(fullName:String, email:String, password:String) = viewModelScope.launch {
           registerLoginRepo.register(fullName, email, password).collect{
               _registerStateFlow.emit(it)
           }
    }

    //*****************************************************************************

    private val _loginStateFlow = MutableStateFlow<DataStatus<LoginResponse>>(DataStatus.loading())
    val loginStateFlow: StateFlow<DataStatus<LoginResponse>> = _loginStateFlow
    fun login(email:String, password:String) = viewModelScope.launch {
        registerLoginRepo.login(email, password).collect {
            _loginStateFlow.emit(it)
        }
    }
}