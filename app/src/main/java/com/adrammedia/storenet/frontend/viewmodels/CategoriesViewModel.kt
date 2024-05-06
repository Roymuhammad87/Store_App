package com.adrammedia.storenet.frontend.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrammedia.storenet.backend.api.category.CategoriesApi
import com.adrammedia.storenet.backend.data.categories.CategoriesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
@HiltViewModel
class CategoriesViewModel @Inject constructor(private val categoriesApi: CategoriesApi):ViewModel() {

    private val _categoriesStateFlow = MutableStateFlow<CategoriesResponse?>(null)
    val categoriesStateFlow: StateFlow<CategoriesResponse?> = _categoriesStateFlow

    fun getCategories() = viewModelScope.launch {
        try {
            val response = categoriesApi.getCategories()
            when(response.code()) {
                in 200..299->{
                    if (response.body() != null) {
                        _categoriesStateFlow.emit(response.body())
                        Log.d("TAG", "getCategories: ${response.body()!!.data}")
                    }
                }
                in 400..499->{

                    Log.d("TAG", "category 400: ${response.message()}\n ${response.errorBody()?.string()}")
                }
                in 500..599-> {
                    val errorBody = response.errorBody()?.string()
                    Log.d("TAG", "category 500: ${errorBody}")
                }
                else -> {
                    Log.d("TAG", "category other: ${response.code()}")
                }
            }

        } catch (e:IOException){
            Log.d("TAG", "get categories: ${e.message}")
        }

    }
}