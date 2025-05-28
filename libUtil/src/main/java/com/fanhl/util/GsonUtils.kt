package com.fanhl.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import kotlin.reflect.KClass

object GsonUtils {
    val gson = Gson()

    fun <T> toMap(obj: T): Map<String, Any?> {
        val json = gson.toJson(obj)
        val type = object : TypeToken<Map<String, Any?>>() {}.type
        return gson.fromJson(json, type)
    }

    /**
     * Convert object to JSON string
     */
    fun <T> toJson(obj: T): String {
        return gson.toJson(obj)
    }

    /**
     * Convert JSON string to object
     */
    inline fun <reified T> fromJson(json: String): T? {
        return try {
            gson.fromJson(json, T::class.java)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Convert JSON string to object with KClass type
     */
    fun <T : Any> fromJson(json: String, type: KClass<T>): T? {
        return try {
            gson.fromJson(json, type.java)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Convert JSON string to object with Class type
     */
    fun <T> fromJson(json: String, type: Class<T>): T? {
        return try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Convert JSON string to object with TypeToken
     */
    fun <T> fromJson(json: String, typeToken: TypeToken<T>): T? {
        return try {
            gson.fromJson(json, typeToken.type)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Convert JSON string to object with Type
     */
    fun <T> fromJson(json: String, type: Type): T? {
        return try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            null
        }
    }
} 