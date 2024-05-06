package com.adrammedia.storenet.backend.repos

import android.util.Log
import com.adrammedia.storenet.backend.api.user.RegisterLoginApiApi
import com.adrammedia.storenet.utils.DataStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RegisterLoginRepo @Inject constructor(private val registerLoginApiApi: RegisterLoginApiApi) {

    suspend fun register(fullName:String, email:String, password:String) = flow {
        emit(DataStatus.loading())
        val response = registerLoginApiApi.register(fullName, email, password)

        when(response.code()){
            in 200..299->{
                Log.d("TAG", "register 200: ${response.body()}")
                emit(DataStatus.success(response.body()))
            }
            in 400..499->{
                Log.d("TAG", "register 400: ${response.body()}")
                emit(DataStatus.error(response.errorBody()?.string()))
            }
            in 500..599->{
                Log.d("TAG", "register 500: ${response.body()}")
                emit(DataStatus.error(response.errorBody()?.string()))
            }
        }
    }.catch {
        Log.d("TAG", "register catch: ${it.message}")
        emit(DataStatus.error(it.message))
    }.flowOn(Dispatchers.IO)

    suspend fun login( email:String, password:String) = flow {
        emit(DataStatus.loading())
        val response = registerLoginApiApi.login( email, password)

        when(response.code()){
            in 200..299->{
                Log.d("TAG", "login 200: ${response.body()}")
                emit(DataStatus.success(response.body()))
            }
            in 400..499->{
                Log.d("TAG", "login 400: ${response.body()}")
                emit(DataStatus.error(response.errorBody()?.string()))
            }
            in 500..599->{
                Log.d("TAG", "login 500: ${response.body()}")
                emit(DataStatus.error(response.errorBody()?.string()))
            }
        }
    }.catch {
        Log.d("TAG", "login catch: ${it.message}")
        emit(DataStatus.error(it.message))
    }.flowOn(Dispatchers.IO)

}