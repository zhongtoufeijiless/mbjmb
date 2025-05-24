package com.tcssj.mbjmb.model

import com.google.gson.annotations.SerializedName

open class ResponseData<T>(
    @SerializedName(value = "status", alternate = ["code"]) val code: String? = null,
    @SerializedName(value = "message", alternate = ["msg"]) val msg: String? = null,
    @SerializedName(value = "body", alternate = ["data"]) val data: T? = null
) 