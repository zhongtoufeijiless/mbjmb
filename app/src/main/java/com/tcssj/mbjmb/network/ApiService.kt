package com.tcssj.mbjmb.network

import com.tcssj.mbjmb.model.SendVerifyCodeRequest
import com.tcssj.mbjmb.model.ResponseData
import com.tcssj.mbjmb.model.VerifyCodeBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: text/plain")
    @POST("/auth/v3.1/user/sendVerifiyCode")
    suspend fun sendVerifyCode(@Body request: SendVerifyCodeRequest): ResponseData<VerifyCodeBody>
} 