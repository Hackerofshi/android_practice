package com.shixin.base

data class BaseResponse<T>(
    val code: Int,
    val errInfo: String,
    val info: T
)