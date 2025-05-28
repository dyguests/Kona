package com.fanhl.util

import io.mockk.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SharePreferenceExtTest {

    @Before
    fun setup() {
        mockkObject(SharePreferenceUtil)
        clearAllMocks()
    }

    @Test
    fun `test string property`() {
        class TestClass {
            var stringValue by sp<String>("key_string")
        }

        val test = TestClass()
        
        // Test initial value is null when key doesn't exist
        coEvery { SharePreferenceUtil.contains("key_string") } returns false
        assertNull(test.stringValue)
        
        // Test setting value
        coEvery { SharePreferenceUtil.contains("key_string") } returns true
        coEvery { SharePreferenceUtil.setString("key_string", "test") } just Runs
        coEvery { SharePreferenceUtil.getString("key_string", "") } returns "test"
        test.stringValue = "test"
        assertEquals("test", test.stringValue)
        
        // Test removing value
        coEvery { SharePreferenceUtil.remove("key_string") } just Runs
        coEvery { SharePreferenceUtil.contains("key_string") } returns false
        sp<String>("key_string").remove()
        assertNull(test.stringValue)
        
        // Test exists
        coEvery { SharePreferenceUtil.contains("key_string") } returns false
        assertFalse(sp<String>("key_string").exists())
        coEvery { SharePreferenceUtil.contains("key_string") } returns true
        test.stringValue = "test"
        assertTrue(sp<String>("key_string").exists())
    }

    @Test
    fun `test primitive properties`() {
        class TestClass {
            var intValue by sp<Int>("key_int")
            var longValue by sp<Long>("key_long")
            var floatValue by sp<Float>("key_float")
            var booleanValue by sp<Boolean>("key_boolean")
        }

        val test = TestClass()
        
        // Test initial values when keys don't exist
        coEvery { SharePreferenceUtil.contains("key_int") } returns false
        coEvery { SharePreferenceUtil.contains("key_long") } returns false
        coEvery { SharePreferenceUtil.contains("key_float") } returns false
        coEvery { SharePreferenceUtil.contains("key_boolean") } returns false
        
        assertNull(test.intValue)
        assertNull(test.longValue)
        assertNull(test.floatValue)
        assertNull(test.booleanValue)
        
        // Test setting values
        coEvery { SharePreferenceUtil.contains("key_int") } returns true
        coEvery { SharePreferenceUtil.contains("key_long") } returns true
        coEvery { SharePreferenceUtil.contains("key_float") } returns true
        coEvery { SharePreferenceUtil.contains("key_boolean") } returns true
        
        coEvery { SharePreferenceUtil.setInt("key_int", 42) } just Runs
        coEvery { SharePreferenceUtil.setLong("key_long", 42L) } just Runs
        coEvery { SharePreferenceUtil.setFloat("key_float", 42.5f) } just Runs
        coEvery { SharePreferenceUtil.setBoolean("key_boolean", true) } just Runs
        
        coEvery { SharePreferenceUtil.getInt("key_int", 0) } returns 42
        coEvery { SharePreferenceUtil.getLong("key_long", 0L) } returns 42L
        coEvery { SharePreferenceUtil.getFloat("key_float", 0f) } returns 42.5f
        coEvery { SharePreferenceUtil.getBoolean("key_boolean", false) } returns true
        
        test.intValue = 42
        test.longValue = 42L
        test.floatValue = 42.5f
        test.booleanValue = true
        
        assertEquals(42, test.intValue)
        assertEquals(42L, test.longValue)
        assertEquals(42.5f, test.floatValue)
        assertEquals(true, test.booleanValue)
    }

    @Test
    fun `test complex object property`() {
        data class User(val name: String, val age: Int)
        
        class TestClass {
            var user by sp<User>("key_user")
        }

        val test = TestClass()
        
        // Test initial value is null when key doesn't exist
        coEvery { SharePreferenceUtil.contains("key_user") } returns false
        assertNull(test.user)
        
        // Test setting value
        val user = User("John", 25)
        val userJson = GsonUtils.toJson(user)
        coEvery { SharePreferenceUtil.contains("key_user") } returns true
        coEvery { SharePreferenceUtil.setString("key_user", userJson) } just Runs
        coEvery { SharePreferenceUtil.getString("key_user", "") } returns userJson
        test.user = user
        assertEquals(user, test.user)
        
        // Test removing value
        coEvery { SharePreferenceUtil.remove("key_user") } just Runs
        coEvery { SharePreferenceUtil.contains("key_user") } returns false
        sp<User>("key_user").remove()
        assertNull(test.user)
    }

    @Test
    fun `test list property`() {
        data class User(val name: String, val age: Int)
        
        class TestClass {
            var users by sp<List<User>>("key_users")
        }

        val test = TestClass()
        
        // Test initial value is null when key doesn't exist
        coEvery { SharePreferenceUtil.contains("key_users") } returns false
        assertNull(test.users)
        
        // Test setting value
        val users = listOf(
            User("John", 25),
            User("Jane", 30)
        )
        val usersJson = GsonUtils.toJson(users)
        coEvery { SharePreferenceUtil.contains("key_users") } returns true
        coEvery { SharePreferenceUtil.setString("key_users", usersJson) } just Runs
        coEvery { SharePreferenceUtil.getString("key_users", "") } returns usersJson
        test.users = users
        assertEquals(users, test.users)
        
        // Test removing value
        coEvery { SharePreferenceUtil.remove("key_users") } just Runs
        coEvery { SharePreferenceUtil.contains("key_users") } returns false
        sp<List<User>>("key_users").remove()
        assertNull(test.users)
    }

    @Test
    fun `test multiple instances`() {
        class TestClass {
            var value by sp<String>("key")
        }

        val test1 = TestClass()
        val test2 = TestClass()
        
        // Test that different instances share the same value
        coEvery { SharePreferenceUtil.contains("key") } returns true
        coEvery { SharePreferenceUtil.setString("key", "test") } just Runs
        coEvery { SharePreferenceUtil.getString("key", "") } returns "test"
        test1.value = "test"
        assertEquals("test", test2.value)
        
        // Test that changing value in one instance affects the other
        coEvery { SharePreferenceUtil.setString("key", "new value") } just Runs
        coEvery { SharePreferenceUtil.getString("key", "") } returns "new value"
        test2.value = "new value"
        assertEquals("new value", test1.value)
    }

    @Test
    fun `test string property with cache`() {
        class TestClass {
            var stringValue by sp<String>("key_string")
        }

        val test = TestClass()
        
        // Test initial value is null when key doesn't exist
        coEvery { SharePreferenceUtil.contains("key_string") } returns false
        assertNull(test.stringValue)
        
        // Test setting value
        coEvery { SharePreferenceUtil.contains("key_string") } returns true
        coEvery { SharePreferenceUtil.setString("key_string", "test") } just Runs
        coEvery { SharePreferenceUtil.getString("key_string", "") } returns "test"
        test.stringValue = "test"
        assertEquals("test", test.stringValue)
        
        // Verify that subsequent gets use cache and don't call SharePreferenceUtil
        test.stringValue
        test.stringValue
        coVerify(exactly = 1) { SharePreferenceUtil.getString("key_string", "") }
        
        // Test removing value
        coEvery { SharePreferenceUtil.remove("key_string") } just Runs
        coEvery { SharePreferenceUtil.contains("key_string") } returns false
        sp<String>("key_string").remove()
        assertNull(test.stringValue)
        
        // Verify that after remove, next get will check SharedPreferences again
        test.stringValue
        coVerify(exactly = 2) { SharePreferenceUtil.contains("key_string") }
    }

    @Test
    fun `test primitive properties with cache`() {
        class TestClass {
            var intValue by sp<Int>("key_int")
            var longValue by sp<Long>("key_long")
            var floatValue by sp<Float>("key_float")
            var booleanValue by sp<Boolean>("key_boolean")
        }

        val test = TestClass()
        
        // Test initial values when keys don't exist
        coEvery { SharePreferenceUtil.contains("key_int") } returns false
        coEvery { SharePreferenceUtil.contains("key_long") } returns false
        coEvery { SharePreferenceUtil.contains("key_float") } returns false
        coEvery { SharePreferenceUtil.contains("key_boolean") } returns false
        
        assertNull(test.intValue)
        assertNull(test.longValue)
        assertNull(test.floatValue)
        assertNull(test.booleanValue)
        
        // Test setting values
        coEvery { SharePreferenceUtil.contains("key_int") } returns true
        coEvery { SharePreferenceUtil.contains("key_long") } returns true
        coEvery { SharePreferenceUtil.contains("key_float") } returns true
        coEvery { SharePreferenceUtil.contains("key_boolean") } returns true
        
        coEvery { SharePreferenceUtil.setInt("key_int", 42) } just Runs
        coEvery { SharePreferenceUtil.setLong("key_long", 42L) } just Runs
        coEvery { SharePreferenceUtil.setFloat("key_float", 42.5f) } just Runs
        coEvery { SharePreferenceUtil.setBoolean("key_boolean", true) } just Runs
        
        coEvery { SharePreferenceUtil.getInt("key_int", 0) } returns 42
        coEvery { SharePreferenceUtil.getLong("key_long", 0L) } returns 42L
        coEvery { SharePreferenceUtil.getFloat("key_float", 0f) } returns 42.5f
        coEvery { SharePreferenceUtil.getBoolean("key_boolean", false) } returns true
        
        test.intValue = 42
        test.longValue = 42L
        test.floatValue = 42.5f
        test.booleanValue = true
        
        assertEquals(42, test.intValue)
        assertEquals(42L, test.longValue)
        assertEquals(42.5f, test.floatValue)
        assertEquals(true, test.booleanValue)
        
        // Verify that subsequent gets use cache and don't call SharePreferenceUtil
        test.intValue
        test.longValue
        test.floatValue
        test.booleanValue
        coVerify(exactly = 1) { SharePreferenceUtil.getInt("key_int", 0) }
        coVerify(exactly = 1) { SharePreferenceUtil.getLong("key_long", 0L) }
        coVerify(exactly = 1) { SharePreferenceUtil.getFloat("key_float", 0f) }
        coVerify(exactly = 1) { SharePreferenceUtil.getBoolean("key_boolean", false) }
        
        // Test removing values
        coEvery { SharePreferenceUtil.remove("key_int") } just Runs
        coEvery { SharePreferenceUtil.remove("key_long") } just Runs
        coEvery { SharePreferenceUtil.remove("key_float") } just Runs
        coEvery { SharePreferenceUtil.remove("key_boolean") } just Runs
        
        coEvery { SharePreferenceUtil.contains("key_int") } returns false
        coEvery { SharePreferenceUtil.contains("key_long") } returns false
        coEvery { SharePreferenceUtil.contains("key_float") } returns false
        coEvery { SharePreferenceUtil.contains("key_boolean") } returns false
        
        sp<Int>("key_int").remove()
        sp<Long>("key_long").remove()
        sp<Float>("key_float").remove()
        sp<Boolean>("key_boolean").remove()
        
        assertNull(test.intValue)
        assertNull(test.longValue)
        assertNull(test.floatValue)
        assertNull(test.booleanValue)
        
        // Verify that after remove, next gets will check SharedPreferences again
        test.intValue
        test.longValue
        test.floatValue
        test.booleanValue
        coVerify(exactly = 2) { SharePreferenceUtil.contains("key_int") }
        coVerify(exactly = 2) { SharePreferenceUtil.contains("key_long") }
        coVerify(exactly = 2) { SharePreferenceUtil.contains("key_float") }
        coVerify(exactly = 2) { SharePreferenceUtil.contains("key_boolean") }
    }

    @Test
    fun `test complex object property with cache`() {
        data class User(val name: String, val age: Int)
        
        class TestClass {
            var user by sp<User>("key_user")
        }

        val test = TestClass()
        
        // Test initial value is null when key doesn't exist
        coEvery { SharePreferenceUtil.contains("key_user") } returns false
        assertNull(test.user)
        
        // Test setting value
        val user = User("John", 25)
        val userJson = GsonUtils.toJson(user)
        coEvery { SharePreferenceUtil.contains("key_user") } returns true
        coEvery { SharePreferenceUtil.setString("key_user", userJson) } just Runs
        coEvery { SharePreferenceUtil.getString("key_user", "") } returns userJson
        test.user = user
        assertEquals(user, test.user)
        
        // Verify that subsequent gets use cache and don't call SharePreferenceUtil
        test.user
        test.user
        coVerify(exactly = 1) { SharePreferenceUtil.getString("key_user", "") }
        
        // Test removing value
        coEvery { SharePreferenceUtil.remove("key_user") } just Runs
        coEvery { SharePreferenceUtil.contains("key_user") } returns false
        sp<User>("key_user").remove()
        assertNull(test.user)
        
        // Verify that after remove, next get will check SharedPreferences again
        test.user
        coVerify(exactly = 2) { SharePreferenceUtil.contains("key_user") }
    }

    @Test
    fun `test list property with cache`() {
        data class User(val name: String, val age: Int)
        
        class TestClass {
            var users by sp<List<User>>("key_users")
        }

        val test = TestClass()
        
        // Test initial value is null when key doesn't exist
        coEvery { SharePreferenceUtil.contains("key_users") } returns false
        assertNull(test.users)
        
        // Test setting value
        val users = listOf(
            User("John", 25),
            User("Jane", 30)
        )
        val usersJson = GsonUtils.toJson(users)
        coEvery { SharePreferenceUtil.contains("key_users") } returns true
        coEvery { SharePreferenceUtil.setString("key_users", usersJson) } just Runs
        coEvery { SharePreferenceUtil.getString("key_users", "") } returns usersJson
        test.users = users
        assertEquals(users, test.users)
        
        // Verify that subsequent gets use cache and don't call SharePreferenceUtil
        test.users
        test.users
        coVerify(exactly = 1) { SharePreferenceUtil.getString("key_users", "") }
        
        // Test removing value
        coEvery { SharePreferenceUtil.remove("key_users") } just Runs
        coEvery { SharePreferenceUtil.contains("key_users") } returns false
        sp<List<User>>("key_users").remove()
        assertNull(test.users)
        
        // Verify that after remove, next get will check SharedPreferences again
        test.users
        coVerify(exactly = 2) { SharePreferenceUtil.contains("key_users") }
    }

    @Test
    fun `test multiple instances with cache`() {
        class TestClass {
            var value by sp<String>("key")
        }

        val test1 = TestClass()
        val test2 = TestClass()
        
        // Test that different instances share the same value
        coEvery { SharePreferenceUtil.contains("key") } returns true
        coEvery { SharePreferenceUtil.setString("key", "test") } just Runs
        coEvery { SharePreferenceUtil.getString("key", "") } returns "test"
        test1.value = "test"
        assertEquals("test", test2.value)
        
        // Verify that subsequent gets use cache and don't call SharePreferenceUtil
        test1.value
        test2.value
        coVerify(exactly = 1) { SharePreferenceUtil.getString("key", "") }
        
        // Test that changing value in one instance affects the other
        coEvery { SharePreferenceUtil.setString("key", "new value") } just Runs
        coEvery { SharePreferenceUtil.getString("key", "") } returns "new value"
        test2.value = "new value"
        assertEquals("new value", test1.value)
        
        // Test removing value
        coEvery { SharePreferenceUtil.remove("key") } just Runs
        coEvery { SharePreferenceUtil.contains("key") } returns false
        sp<String>("key").remove()
        assertNull(test1.value)
        assertNull(test2.value)
        
        // Verify that after remove, next gets will check SharedPreferences again
        test1.value
        test2.value
        coVerify(exactly = 2) { SharePreferenceUtil.contains("key") }
    }

    @Test
    fun `test invalidate method`() {
        class TestClass {
            var stringValue by sp<String>("key_string")
        }

        val test = TestClass()
        
        // Test initial value
        coEvery { SharePreferenceUtil.contains("key_string") } returns true
        coEvery { SharePreferenceUtil.getString("key_string", "") } returns "initial"
        assertEquals("initial", test.stringValue)
        
        // Verify cache is working
        test.stringValue
        test.stringValue
        coVerify(exactly = 1) { SharePreferenceUtil.getString("key_string", "") }
        
        // Test invalidate
        coEvery { SharePreferenceUtil.getString("key_string", "") } returns "updated"
        sp<String>("key_string").invalidate()
        assertEquals("updated", test.stringValue)
        
        // Verify that invalidate forced a new read from SharePreferenceUtil
        coVerify(exactly = 2) { SharePreferenceUtil.getString("key_string", "") }
    }

    @Test
    fun `test invalidate with complex object`() {
        data class User(val name: String, val age: Int)
        
        class TestClass {
            var user by sp<User>("key_user")
        }

        val test = TestClass()
        
        // Test initial value
        val initialUser = User("John", 25)
        val initialJson = GsonUtils.toJson(initialUser)
        coEvery { SharePreferenceUtil.contains("key_user") } returns true
        coEvery { SharePreferenceUtil.getString("key_user", "") } returns initialJson
        assertEquals(initialUser, test.user)
        
        // Verify cache is working
        test.user
        test.user
        coVerify(exactly = 1) { SharePreferenceUtil.getString("key_user", "") }
        
        // Test invalidate
        val updatedUser = User("Jane", 30)
        val updatedJson = GsonUtils.toJson(updatedUser)
        coEvery { SharePreferenceUtil.getString("key_user", "") } returns updatedJson
        sp<User>("key_user").invalidate()
        assertEquals(updatedUser, test.user)
        
        // Verify that invalidate forced a new read from SharePreferenceUtil
        coVerify(exactly = 2) { SharePreferenceUtil.getString("key_user", "") }
    }

    @Test
    fun `test invalidate with multiple instances`() {
        class TestClass {
            var value by sp<String>("key")
        }

        val test1 = TestClass()
        val test2 = TestClass()
        
        // Test initial value
        coEvery { SharePreferenceUtil.contains("key") } returns true
        coEvery { SharePreferenceUtil.getString("key", "") } returns "initial"
        assertEquals("initial", test1.value)
        assertEquals("initial", test2.value)
        
        // Verify cache is working
        test1.value
        test2.value
        coVerify(exactly = 1) { SharePreferenceUtil.getString("key", "") }
        
        // Test invalidate
        coEvery { SharePreferenceUtil.getString("key", "") } returns "updated"
        sp<String>("key").invalidate()
        assertEquals("updated", test1.value)
        assertEquals("updated", test2.value)
        
        // Verify that invalidate forced a new read from SharePreferenceUtil
        coVerify(exactly = 2) { SharePreferenceUtil.getString("key", "") }
    }
} 