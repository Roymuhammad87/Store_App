package com.adrammedia.storenet.backend.api.profile

import com.adrammedia.storenet.backend.data.users.profile.StoreProfileResponse
import com.adrammedia.storenet.backend.data.users.profile.get.GetProfileResponse
import com.adrammedia.storenet.utils.Constants
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface StoreProfileApi {

@Multipart
@Headers("Accept:application/json","Authorization: Bearer {apiToken}")
@POST(Constants.PROFILE_END_POINT)
suspend fun saveUserInfo(
    @Part("user_id") userId:Int,
    @Part("phone") phone:String,
    @Part("longitude") longitude:Double,
    @Part("latitude") latitude:Double,
    @Part image:MultipartBody.Part

):Response<StoreProfileResponse>

@Headers("Accept:application/json","Authorization: Bearer {apiToken}")
@GET(Constants.GET_PROFILE_END_POINT)
suspend fun getUserProfile(@Path("userId") userId:Int):Response<GetProfileResponse>

}