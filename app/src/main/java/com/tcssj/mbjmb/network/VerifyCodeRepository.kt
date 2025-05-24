package com.tcssj.mbjmb.network

import android.util.Log
import com.tcssj.mbjmb.model.SendVerifyCodeRequest
import com.tcssj.mbjmb.model.ResponseData
import com.tcssj.mbjmb.model.VerifyCodeBody

object VerifyCodeRepository {
    suspend fun sendVerifyCode(request: SendVerifyCodeRequest): Result<ResponseData<VerifyCodeBody>> {
        Log.d("VerifyCodeRepository", "请求参数: $request")
        return try {
            val resp = RetrofitClient.instance.sendVerifyCode(request)
            Log.d("VerifyCodeRepository", "接口响应: $resp")
            Result.success(resp)
        } catch (e: Exception) {
            Log.e("VerifyCodeRepository", "接口异常: ", e)
            Result.failure(e)
        }
    }
} 