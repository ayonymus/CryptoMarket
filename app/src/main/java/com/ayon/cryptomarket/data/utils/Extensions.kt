package com.ayon.cryptomarket.data.utils

import retrofit2.Response

// This is not the best way of handling network errors, but will suffice for now.
// A more robust method is described here https://proandroiddev.com/modeling-retrofit-responses-with-sealed-classes-and-coroutines-9d6302077dfe
fun <T> Response<T>.toResult(): Result<T> {
    return if (this.isSuccessful) {
        Result.success(this.body()!!)
    } else {
        Result.failure(NetworkError(this.message(), this.errorBody()?.toString()))
    }
}

class NetworkError(
    message: String? = null,
    val errorBody: String? = null
): Exception(message)
