package com.adrammedia.storenet.utils

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

object Constants {
    const val BASE_URL ="http://127.0.0.1:8000/Store/public/api/"
    const val IMAGES_BASE_URL ="http://127.0.0.1:8000/Store/public/"
    const val REGISTER_END_POINT ="users/register-new-user"
    const val LOGIN_END_POINT ="users/login"
    const val LOGOUT_END_POINT ="logout"
    const val PROFILE_END_POINT ="profile/save-user-info"
    const val GET_PROFILE_END_POINT ="profile/{userId}"
    const val FORGET_PASSWORD_END_POINT ="users/forget-password"
    const val CATEGORIES_END_POINT ="categories"
    const val GET_ALL_TOOLS_END_POINT ="tools"
    const val GET_CURRENT_USER_TOOLS_END_POINT ="tools/{userId}/user-tools"
    const val NETWORK_TIMEOUT = 60L
    const val ADD_NEW_TOOL ="tools/add-new-tool"
    const val AVAILABLE = "Available"
    const val NOT_AVAILABLE = "Not Available"
    val TOOL_AVAILABILITY = arrayOf(AVAILABLE, NOT_AVAILABLE)




    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val STORAGE_PERMISSION = if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.R){
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
     val AUDIO_PERMISSION = if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
     val VIDEO_PERMISSION = if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
        Manifest.permission.READ_MEDIA_VIDEO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
     const val ACCESS_LOCATION =  Manifest.permission.ACCESS_FINE_LOCATION
     const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
     const val WRITE_PERMISSION = Manifest.permission.CAMERA
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val PERMISSIONS_ARRAY = arrayOf(STORAGE_PERMISSION, AUDIO_PERMISSION, VIDEO_PERMISSION, WRITE_PERMISSION)


}