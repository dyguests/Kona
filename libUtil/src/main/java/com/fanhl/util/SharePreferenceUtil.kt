package com.fanhl.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * SharedPreferences utility class
 */
object SharePreferenceUtil {
    private lateinit var prefs: SharedPreferences
    private const val DEFAULT_PREFS_NAME = "kona_prefs"

    fun init(context: Context, prefsName: String? = null) {
        val name = prefsName ?: context.packageName
        prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun setString(key: String, value: String) {
        prefs.edit {
            putString(key, value)
        }
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return prefs.getString(key, defaultValue) ?: defaultValue
    }

    fun setInt(key: String, value: Int) {
        prefs.edit {
            putInt(key, value)
        }
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return prefs.getInt(key, defaultValue)
    }

    fun setLong(key: String, value: Long) {
        prefs.edit {
            putLong(key, value)
        }
    }

    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return prefs.getLong(key, defaultValue)
    }

    fun setBoolean(key: String, value: Boolean) {
        prefs.edit {
            putBoolean(key, value)
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    fun setFloat(key: String, value: Float) {
        prefs.edit {
            putFloat(key, value)
        }
    }

    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return prefs.getFloat(key, defaultValue)
    }

    fun remove(key: String) {
        prefs.edit {
            remove(key)
        }
    }

    fun clear() {
        prefs.edit {
            clear()
        }
    }

    fun contains(key: String): Boolean {
        return prefs.contains(key)
    }
} 