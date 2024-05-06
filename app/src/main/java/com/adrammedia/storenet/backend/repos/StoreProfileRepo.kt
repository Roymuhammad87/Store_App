package com.adrammedia.storenet.backend.repos

import android.util.Log
import com.adrammedia.storenet.backend.api.profile.StoreProfileApi
import com.adrammedia.storenet.utils.DataStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import javax.inject.Inject

class StoreProfileRepo @Inject constructor(private val storeProfileApi: StoreProfileApi) {
    suspend fun saveUserInfo(
        userId: Int, phone: String, longitude: Double, latitude: Double, image: MultipartBody.Part
    ) = flow {
        emit(DataStatus.loading())
        val response = storeProfileApi.saveUserInfo(userId, phone, longitude, latitude, image)

        when (response.code()) {
            in 200..299 -> {
                Log.d("TAG", "saveUserInfo 200: ${response.body()}")
                emit(DataStatus.success(response.body()))
            }

            in 400..499 -> {
                Log.d("TAG", "saveUserInfo 400: ${response.errorBody()?.string()}")
                emit(DataStatus.error(response.message()))
            }

            in 500..599 -> {
                Log.d("TAG", "saveUserInfo 500: ${response.errorBody()?.string()}")
                emit(DataStatus.error(response.message()))
            }
        }
    }.catch {
        Log.d("TAG", "saveUserInfo 400: ${it.message}")
        emit(DataStatus.error(it.message))

    }.flowOn(Dispatchers.IO)

    suspend fun getUserProfile(userId: Int) = flow {
        emit(DataStatus.loading())
        val response = storeProfileApi.getUserProfile(userId)
        when (response.code()) {
            in 200..299 -> {
                Log.d("TAG", "getUserProfile 200: ${response.body()}")
                emit(DataStatus.success(response.body()))
            }
            in 400..499 -> {
                Log.d("TAG", "getUserProfile 400: ${response.errorBody()?.string()}")
                emit(DataStatus.error(response.message()))
            }
            in 500..599 -> {
                Log.d("TAG", "getUserProfile 500: ${response.errorBody()?.string()}")
                emit(DataStatus.error(response.message()))
            }
        }
    }.catch {
        Log.d("TAG", "getUserProfile 400: ${it.message}")
        emit(DataStatus.error(it.message))
    }.flowOn(Dispatchers.IO)
}