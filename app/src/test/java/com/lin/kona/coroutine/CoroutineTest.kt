package com.lin.kona.coroutine

import kotlinx.coroutines.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CoroutineTest {
    @Test
    fun testGlobalScope() {
        GlobalScope.launch(Dispatchers.Default) {
            println(233)
        }
    }

    @Test
    fun testFun() {
        // funTest协程体
        val funTest: suspend CoroutineScope.() -> Unit = {
            println("funTest")
            suspendFun1()
            suspendFun2()
        }
        GlobalScope.launch(Dispatchers.Default, block = funTest)
    }

    // 挂起函数
    private suspend fun suspendFun1() {
        println("suspendFun1")
    }

    // 挂起函数
    private suspend fun suspendFun2() {
        println("suspendFun2")
    }

    @Test
    fun testCoroutine() {
        GlobalScope.launch {
            println(233)
            delay(1000)
            println(23333)
        }
    }

    @Test
    fun test2() = runBlocking {
        delay(1)
    }

    val a by lazy{1}
}