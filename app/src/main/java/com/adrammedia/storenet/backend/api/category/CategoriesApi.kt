package com.adrammedia.storenet.backend.api.category

import com.adrammedia.storenet.backend.data.categories.CategoriesResponse
import com.adrammedia.storenet.utils.Constants
import retrofit2.Response
import retrofit2.http.GET

interface CategoriesApi {
    @GET(Constants.CATEGORIES_END_POINT)
    suspend fun getCategories():Response<CategoriesResponse>
}