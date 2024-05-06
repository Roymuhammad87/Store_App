package com.adrammedia.storenet.backend.api.tool

import com.adrammedia.storenet.backend.data.tools.alltools.AllToolsResponse
import com.adrammedia.storenet.backend.data.tools.storetool.StoreToolResponse
import com.adrammedia.storenet.utils.Constants
import com.adrammedia.storenet.utils.Constants.ADD_NEW_TOOL
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ToolsApi {
    @Multipart
    @Headers("Accept:application/json","Authorization: Bearer {apiToken}")
    @POST(ADD_NEW_TOOL)
    suspend fun storeTool(
        @Part("name") name:RequestBody,
        @Part("description") description:RequestBody,
        @Part("price") price:RequestBody,
        @Part("state") state:RequestBody,
        @Part("user_id") userId:RequestBody,
        @Part("categoryName") categoryName:RequestBody,
        @Part vararg images: MultipartBody.Part
    ):Response<StoreToolResponse>

    @Headers("Accept:application/json","Authorization: Bearer {apiToken}")
    @GET(Constants.GET_ALL_TOOLS_END_POINT)
    suspend fun getAllTools():Response<AllToolsResponse>

    @Headers("Accept:application/json","Authorization: Bearer {apiToken}")
    @GET(Constants.GET_CURRENT_USER_TOOLS_END_POINT)
    suspend fun getCurrentUserTools(@Path("userId") userId:Int):Response<AllToolsResponse>
}