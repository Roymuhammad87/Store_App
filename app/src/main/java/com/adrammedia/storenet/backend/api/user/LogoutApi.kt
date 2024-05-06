package com.adrammedia.storenet.backend.api.user

import com.adrammedia.storenet.backend.data.users.login.LoginResponse
import com.adrammedia.storenet.backend.data.users.logout.LogoutResponse

import com.adrammedia.storenet.utils.Constants
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface LogoutApi {

    @DELETE(Constants.LOGOUT_END_POINT)
    @Headers("Accept:application/json","Authorization: Bearer {apiToken}")
    suspend fun logout(): Response<LogoutResponse>


}