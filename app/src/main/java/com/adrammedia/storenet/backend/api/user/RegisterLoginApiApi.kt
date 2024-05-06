package com.adrammedia.storenet.backend.api.user

import com.adrammedia.storenet.backend.data.users.login.LoginResponse
import com.adrammedia.storenet.backend.data.users.register.RegisterResponse
import com.adrammedia.storenet.utils.Constants
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface RegisterLoginApiApi {
    @FormUrlEncoded
    @POST(Constants.REGISTER_END_POINT)
    @Headers("Accept:application/json")
    suspend fun register(
        @Field("fullName") fullName: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @POST(Constants.LOGIN_END_POINT)
    @Headers("Accept:application/json","Authorization: Bearer {apiToken}")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<LoginResponse>

}