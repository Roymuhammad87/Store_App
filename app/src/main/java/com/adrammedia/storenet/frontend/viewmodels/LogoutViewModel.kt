package com.adrammedia.storenet.frontend.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrammedia.storenet.backend.api.user.LogoutApi
import com.adrammedia.storenet.backend.data.users.logout.LogoutResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogoutViewModel @Inject constructor(private val logoutApi: LogoutApi) :ViewModel(){
    private val _logoutStateFlow = MutableStateFlow<LogoutResponse?>(null)
    val logoutStateFlow: StateFlow<LogoutResponse?> = _logoutStateFlow
    fun logout() = viewModelScope.launch {
        val response = logoutApi.logout()
        when(response.code()){
            in 200..299->{
                if (response.body() != null) {
                    _logoutStateFlow.emit(response.body())
                }
            }
            in 400..499->{

                Log.d("TAG", "logout 400: ${response.code()}")
            }
            in 500..599-> {
                val errorBody = response.errorBody()?.string()
                Log.d("TAG", "logout 500: ${errorBody}")
            }
            else -> {
                Log.d("TAG", "logout other: ${response.code()}")
            }
        }
    }


}