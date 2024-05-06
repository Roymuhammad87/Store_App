package com.adrammedia.storenet.backend.repos

import android.util.Log
import com.adrammedia.storenet.backend.api.tool.ToolsApi
import com.adrammedia.storenet.utils.DataStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ToolRepo @Inject constructor(private val toolsApi: ToolsApi) {
    //Get all tools

    suspend fun getAllTools() = flow {
        emit(DataStatus.loading())
        val allToolsResponse =toolsApi.getAllTools()
        when(allToolsResponse.code()){
            in 200..299-> emit(DataStatus.success(allToolsResponse.body()?.data))
            in 400..499->{
                emit(DataStatus.error(allToolsResponse.message()))
            }
            in 500..599->{
                emit(DataStatus.error(allToolsResponse.message()))
            }
        }
    }.catch {
        emit(DataStatus.error(it.message))
        Log.d("TAG", "getAllTools: ${it.message}")
    }.flowOn(Dispatchers.IO)


        //Get user tools

    suspend fun getUserTools(userId: Int) = flow {
        emit(DataStatus.loading())
        val userToolsResponse = toolsApi.getCurrentUserTools(userId)
        when(userToolsResponse.code()){
            in 200..299-> {
                Log.d("TAG", "getUserTools200: ${userToolsResponse.message()}")
                emit(DataStatus.success(userToolsResponse.body()?.data))
            }
            in 400..499->{
                Log.d("TAG", "getUserTools400: ${userToolsResponse.message()}")
                emit(DataStatus.error(userToolsResponse.message()))

            }
            in 500..599->{
                Log.d("TAG", "getUserTools500: ${userToolsResponse.errorBody()?.string()}")
                emit(DataStatus.error(userToolsResponse.message()))
            }
        }
    }.catch {
        emit(DataStatus.error(it.message))
    }.flowOn(Dispatchers.IO)

}