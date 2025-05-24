package com.tcssj.mbjmb.model

import com.google.gson.annotations.SerializedName

data class VerifyCodeBody(
    @SerializedName(value = "expiry", alternate = ["expire"]) val expiry: Int?,
    @SerializedName(value = "status", alternate = ["state"]) val status: String?,
    @SerializedName(value = "transId", alternate = ["trans_id", "transactionId"]) val transId: String?
) 