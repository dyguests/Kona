package com.fanhl.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Type-safe SharedPreferences property delegate
 */
class SharedPreferenceProperty<T>(
    private val key: String,
    private val defaultValue: T
) : ReadWriteProperty<Any, T> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return when (defaultValue) {
            is String -> SharePreferenceUtil.getString(key, defaultValue) as T
            is Int -> SharePreferenceUtil.getInt(key, defaultValue) as T
            is Long -> SharePreferenceUtil.getLong(key, defaultValue) as T
            is Float -> SharePreferenceUtil.getFloat(key, defaultValue) as T
            is Boolean -> SharePreferenceUtil.getBoolean(key, defaultValue) as T
            else -> {
                val json = SharePreferenceUtil.getString(key, "")
                if (json.isEmpty()) defaultValue
                else GsonUtils.fromJson(json, defaultValue::class.java) ?: defaultValue
            }
        }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        when (value) {
            is String -> SharePreferenceUtil.setString(key, value)
            is Int -> SharePreferenceUtil.setInt(key, value)
            is Long -> SharePreferenceUtil.setLong(key, value)
            is Float -> SharePreferenceUtil.setFloat(key, value)
            is Boolean -> SharePreferenceUtil.setBoolean(key, value)
            else -> {
                val json = GsonUtils.toJson(value)
                SharePreferenceUtil.setString(key, json)
            }
        }
    }

    fun remove() {
        SharePreferenceUtil.remove(key)
    }

    fun exists(): Boolean {
        return SharePreferenceUtil.contains(key)
    }
}

/**
 * Creates a type-safe SharedPreferences property
 */
fun <T> sp(
    key: String,
    defaultValue: T
): SharedPreferenceProperty<T> {
    return SharedPreferenceProperty(key, defaultValue)
}

// Usage examples:
/*
class MyClass {
    var myString by sp("KEY_STRING", "default")
    var myInt by sp("KEY_INT", 0)
    var myBoolean by sp("KEY_BOOLEAN", false)
    var myUser by sp("KEY_USER", User("default", 0))
    var myList by sp("KEY_LIST", listOf<User>())
    
    fun example() {
        // Get value
        val value = myString
        
        // Set value
        myString = "new value"
        myUser = User("John", 25)
        
        // Remove value
        sp("KEY_STRING", "").remove()
        
        // Check if exists
        val exists = sp("KEY_STRING", "").exists()
    }
}
*/
