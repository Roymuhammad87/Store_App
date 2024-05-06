package com.adrammedia.storenet.backend.api.user.passwords

import com.adrammedia.storenet.backend.data.users.passwords.ForgetPasswordRequest
import com.adrammedia.storenet.backend.data.users.passwords.ForgetPasswordResponse
import com.adrammedia.storenet.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ForgetPasswordApi {

    @POST(Constants.FORGET_PASSWORD_END_POINT)
    @Headers("Accept:application/json")
    suspend fun forgetPassword(@Body email:ForgetPasswordRequest):Response<ForgetPasswordResponse>
}