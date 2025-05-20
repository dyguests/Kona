package com.fanhl.http.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import java.io.IOException
import java.nio.charset.StandardCharsets

class HttpLoggingInterceptor : Interceptor {
    companion object {
        private const val TAG = "HttpLog"
        private const val MAX_BODY_LENGTH = 4096L
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBody = request.body

        // 打印请求信息
        val requestLog = StringBuilder()
        requestLog.append("\n")
            .append("┌───────────────────────────────────────────────────────────────────────────────────────────────────────\n")
            .append("│ Request: ${request.method} ${request.url}\n")
            .append("├───────────────────────────────────────────────────────────────────────────────────────────────────────\n")

        // 打印请求头
        request.headers.forEach { (name, value) ->
            requestLog.append("│ $name: $value\n")
        }

        // 打印请求体
        if (requestBody != null) {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            val bodyString = buffer.readString(StandardCharsets.UTF_8)
            requestLog.append("│ Body: $bodyString\n")
        }

        requestLog.append("└───────────────────────────────────────────────────────────────────────────────────────────────────────")

        Log.d(TAG, requestLog.toString())

        // 执行请求
        val response = chain.proceed(request)
        val responseBody = response.body

        // 打印响应信息
        val responseLog = StringBuilder()
        responseLog.append("\n")
            .append("┌───────────────────────────────────────────────────────────────────────────────────────────────────────\n")
            .append("│ Response: ${response.code} ${response.message}\n")
            .append("├───────────────────────────────────────────────────────────────────────────────────────────────────────\n")

        // 打印响应头
        response.headers.forEach { (name, value) ->
            responseLog.append("│ $name: $value\n")
        }

        // 打印响应体
        if (responseBody != null) {
            val bodyString = responseBody.string()
            val truncatedBody = if (bodyString.length > MAX_BODY_LENGTH) {
                bodyString.substring(0, MAX_BODY_LENGTH.toInt()) + "... (truncated)"
            } else {
                bodyString
            }
            responseLog.append("│ Body: $truncatedBody\n")

            // 重新创建响应体，因为string()方法会消耗原始的responseBody
            val newResponseBody = ResponseBody.create(responseBody.contentType(), bodyString)
            responseLog.append("└───────────────────────────────────────────────────────────────────────────────────────────────────────")
            Log.d(TAG, responseLog.toString())

            return response.newBuilder()
                .body(newResponseBody)
                .build()
        }

        responseLog.append("└───────────────────────────────────────────────────────────────────────────────────────────────────────")
        Log.d(TAG, responseLog.toString())

        return response
    }
} 