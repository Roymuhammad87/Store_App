package com.adrammedia.storenet.backend.repos

import android.util.Log
import com.adrammedia.storenet.backend.api.user.passwords.ForgetPasswordApi
import com.adrammedia.storenet.backend.data.users.passwords.ForgetPasswordRequest
import com.adrammedia.storenet.utils.DataStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PasswordRepo @Inject constructor(private val forgetPasswordApi: ForgetPasswordApi) {

    suspend fun forgetPassword(email:ForgetPasswordRequest) = flow {
        emit(DataStatus.loading())
        val response = forgetPasswordApi.forgetPassword(email)
        when(response.code()){
            in 200..299->{
                Log.d("TAG", "forgetPassword 200: ${response.body()?.message}")
                emit(DataStatus.success(response.body()))
            }
            in 400..499->{
                Log.d("TAG", "forgetPassword 400: ${response.errorBody()?.string()}")
                emit(DataStatus.error(response.message()))
            }
            in 500..599->{
                Log.d("TAG", "forgetPassword 500: ${response.errorBody()?.string()}")
                emit(DataStatus.error(response.errorBody()?.string()))
            }
        }
    }.catch {
        Log.d("TAG", "forgetPassword: ${it.message}")
        emit(DataStatus.error(it.message))
    }.flowOn(Dispatchers.IO)
}