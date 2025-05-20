package com.fanhl.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GsonUtils {
    val gson = Gson()

    fun <T> toMap(obj: T): Map<String, Any?> {
        val json = gson.toJson(obj)
        val type = object : TypeToken<Map<String, Any?>>() {}.type
        return gson.fromJson(json, type)
    }
} 