package com.tcssj.mbjmb.network

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import com.tcssj.mbjmb.utils.AESUtil2
import org.json.JSONObject
import java.util.Base64
import com.google.gson.Gson

object RetrofitClient {
    private const val BASE_URL = "http://47.117.39.225:10018/"
    private const val PACKAGE_ALIAS = "mbjmb"
    private const val HEADER_KEY = "xDBrgJdnnY2w1Do7Ik6otonXQRgQyt46"
    private const val HEADER_NAME = "HCFQ"
    private const val PACKAGE_NAME = "com.tcssj.mbjmb"
    private const val VERSION = "12.0.0"
    private const val PARAM_KEY = "allWUzg1eFJ3ekpNQklUeQ=="

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val tokenJson = JSONObject()
            tokenJson.put("sourceChannel", "Orange")
            tokenJson.put("packageName", PACKAGE_NAME)
            tokenJson.put("adid", "")
            tokenJson.put("version", VERSION)
            tokenJson.put("uuId", "")
            tokenJson.put("userId", "")
            val tokenStr = tokenJson.toString()
            val encryptedToken = AESUtil2.encrypt(tokenStr, Base64.getDecoder().decode(HEADER_KEY))
            val request: Request = chain.request().newBuilder()
                .addHeader("packageName", PACKAGE_ALIAS)
                .addHeader(HEADER_NAME, encryptedToken)
                .build()
            chain.proceed(request)
        }
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(AesConverterFactory.create(Gson(), PARAM_KEY))
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
} 