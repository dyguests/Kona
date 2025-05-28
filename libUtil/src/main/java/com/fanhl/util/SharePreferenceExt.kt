package com.fanhl.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Type-safe SharedPreferences property delegate
 */
class SharedPreferenceProperty<T>(
    private val key: String
) : ReadWriteProperty<Any, T?> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        val type = property.returnType.classifier as? KClass<*> ?: return null
        return when (type) {
            String::class -> SharePreferenceUtil.getString(key, "") as T?
            Int::class -> SharePreferenceUtil.getInt(key, 0) as T?
            Long::class -> SharePreferenceUtil.getLong(key, 0L) as T?
            Float::class -> SharePreferenceUtil.getFloat(key, 0f) as T?
            Boolean::class -> SharePreferenceUtil.getBoolean(key, false) as T?
            else -> {
                val json = SharePreferenceUtil.getString(key, "")
                if (json.isEmpty()) null
                else GsonUtils.fromJson(json, type) as T?
            }
        }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        when (value) {
            is String -> SharePreferenceUtil.setString(key, value)
            is Int -> SharePreferenceUtil.setInt(key, value)
            is Long -> SharePreferenceUtil.setLong(key, value)
            is Float -> SharePreferenceUtil.setFloat(key, value)
            is Boolean -> SharePreferenceUtil.setBoolean(key, value)
            null -> SharePreferenceUtil.remove(key)
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
 *
 * Usage examples:
 * ```
 * class MyClass {
 *     var myString by sp<String>("KEY_STRING")
 *     var myInt by sp<Int>("KEY_INT")
 *     var myBoolean by sp<Boolean>("KEY_BOOLEAN")
 *     var myUser by sp<User>("KEY_USER")
 *     var myList by sp<List<User>>("KEY_LIST")
 *     
 *     fun example() {
 *         // Get value
 *         val value = myString
 *         
 *         // Set value
 *         myString = "new value"
 *         myUser = User("John", 25)
 *         
 *         // Remove value
 *         sp<String>("KEY_STRING").remove()
 *         
 *         // Check if exists
 *         val exists = sp<String>("KEY_STRING").exists()
 *     }
 * }
 * ```
 */
fun <T> sp(key: String): SharedPreferenceProperty<T> {
    return SharedPreferenceProperty(key)
}
