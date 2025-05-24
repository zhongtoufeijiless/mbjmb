package com.tcssj.mbjmb.network

import com.google.gson.Gson
import com.tcssj.mbjmb.utils.AESUtil2
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type
import java.util.Base64
import okhttp3.MediaType.Companion.toMediaType
import android.util.Log

class AesConverterFactory private constructor(private val gson: Gson, private val aesKey: String) : Converter.Factory() {
    companion object {
        fun create(gson: Gson, aesKey: String): AesConverterFactory = AesConverterFactory(gson, aesKey)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        return Converter<Any, RequestBody> { value ->
            val json = gson.toJson(value)
            Log.d("AesConverterFactory", "加密前json: $json")
            val encrypted = AESUtil2.encrypt(json, Base64.getDecoder().decode(aesKey))
            Log.d("AesConverterFactory", "加密后: $encrypted")
            RequestBody.create("text/plain".toMediaType(), encrypted)
        }
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return Converter<ResponseBody, Any> { body ->
            val encrypted = body.string()
            Log.d("AesConverterFactory", "解密前: $encrypted")
            val decrypted = AESUtil2.decrypt(encrypted, Base64.getDecoder().decode(aesKey))
            Log.d("AesConverterFactory", "解密后: $decrypted")
            gson.fromJson(decrypted, type)
        }
    }
} 