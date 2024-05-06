package com.adrammedia.storenet.frontend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrammedia.storenet.backend.data.users.profile.StoreProfileResponse
import com.adrammedia.storenet.backend.data.users.profile.get.GetProfileResponse
import com.adrammedia.storenet.backend.repos.StoreProfileRepo
import com.adrammedia.storenet.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class StoreProfileViewModel @Inject constructor(private val storeProfileRepo: StoreProfileRepo):ViewModel() {

    private val _storeProfileStateFlow = MutableStateFlow<DataStatus<StoreProfileResponse>>(DataStatus.loading())
    val storeProfileStateFlow:StateFlow<DataStatus<StoreProfileResponse>> = _storeProfileStateFlow

    fun saveUserInfo(userId: Int, phone: String, longitude: Double, latitude: Double, image: MultipartBody.Part)=
        viewModelScope.launch {
            storeProfileRepo.saveUserInfo(userId, phone, longitude, latitude, image).collect{
                _storeProfileStateFlow.emit(it)
            }
        }

    private val _getProfileStateFlow = MutableStateFlow<DataStatus<GetProfileResponse>>(DataStatus.loading())
    val getProfileStateFlow:StateFlow<DataStatus<GetProfileResponse>> = _getProfileStateFlow

    fun getUserProfile(userId: Int)=
        viewModelScope.launch {
            storeProfileRepo.getUserProfile(userId).collect{
                _getProfileStateFlow.emit(it)
            }
        }

}