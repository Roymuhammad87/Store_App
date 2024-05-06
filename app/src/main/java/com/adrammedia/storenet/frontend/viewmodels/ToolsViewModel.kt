package com.adrammedia.storenet.frontend.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrammedia.storenet.backend.api.tool.ToolsApi
import com.adrammedia.storenet.backend.data.tools.alltools.AllToolsResponse
import com.adrammedia.storenet.backend.data.tools.alltools.Tool
import com.adrammedia.storenet.backend.data.tools.storetool.StoreToolResponse
import com.adrammedia.storenet.backend.repos.ToolRepo
import com.adrammedia.storenet.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ToolsViewModel  @Inject constructor(private val toolsApi: ToolsApi,
    private val toolRepo: ToolRepo):ViewModel() {

    private val _toolsStateFlow = MutableStateFlow<StoreToolResponse?>(null)
    val toolsStateFlow: StateFlow<StoreToolResponse?> = _toolsStateFlow
    fun storeTool(
        name: RequestBody, description:RequestBody, price:RequestBody,
        state:RequestBody, userId:RequestBody,
        categoryName:RequestBody, vararg images: MultipartBody.Part
    ) = viewModelScope.launch {
        try {
            val response = toolsApi.storeTool(name, description, price,state, userId,categoryName, *images)
            when(response.code()) {
                in 200..299->{
                    if (response.body() != null) {
                        _toolsStateFlow.emit(response.body())
                    }
                }
                in 400..499->{

                    Log.d("TAG", "insert tool 400: ${response.message()}\n ${response.errorBody()?.string()}")
                }
                in 500..599-> {
                    val errorBody = response.errorBody()?.string()
                    Log.d("TAG", "insert tool 500: ${errorBody}")
                }
                else -> {
                    Log.d("TAG", "insert tool other: ${response.code()}")
                }
            }

        } catch (e:IOException) {
            Log.d("TAG", "storeTool: ${e.message}")
        }
    }
    private val _allToolsStateFlow = MutableStateFlow<DataStatus<List<Tool>>>(DataStatus.loading())
    val allToolsStateFlow: StateFlow<DataStatus<List<Tool>>> = _allToolsStateFlow
    fun getAllTools() = viewModelScope.launch {

        toolRepo.getAllTools().collect{
            _allToolsStateFlow.emit(it)
        }
    }

    //Get user tools
    private val _userToolsStateFlow = MutableStateFlow<DataStatus<List<Tool>>>(DataStatus.loading())
    val userToolsStateFlow: StateFlow<DataStatus<List<Tool>>> = _userToolsStateFlow
    fun getUserTools(userId:Int) = viewModelScope.launch {
        toolRepo.getUserTools(userId).collect{
            _userToolsStateFlow.emit(it)
        }
    }

}