package com.adrammedia.storenet.utils

data class DataStatus<out T>(
    val status: Status,
    val data: T? = null,
    val errorMsg: String? = null
) {
    enum class Status {
        LOADING, SUCCESS,ERROR
    }
    companion object {
        fun <T> loading():DataStatus<T> = DataStatus(Status.LOADING)
        fun <T> success(data:T?):DataStatus<T> = DataStatus(Status.SUCCESS, data)
        fun <T> error(errorMsg: String?):DataStatus<T> = DataStatus(Status.ERROR, errorMsg = errorMsg)
    }

}
