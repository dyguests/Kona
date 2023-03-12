package com.lin.kona.coroutine

import kotlinx.coroutines.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class Coroutine2Test {

    @Test
    fun testFun0() {
        GlobalScope.launch {
            println(111)
            launch {
                println(222)
            }
            println(333)
            launch {
                println(444)
            }
            println(555)
        }
    }

    @Test
    fun testFun() {
        GlobalScope.launch {
            println(111)
            delay(1000)
            launch {
                println(222)
            }
            delay(1000)
            println(333)
            delay(1000)
            launch {
                println(444)
            }
            delay(1000)
            println(555)
        }
    }

    @Test
    fun testFun2() {
        GlobalScope.launch(Dispatchers.Default) {
            println(111)
            launch(Dispatchers.IO) {
                println(222)
            }
            println(333)
            launch(Dispatchers.Main) {
                println(444)
            }
            println(555)
        }
    }

    @Test
    fun testFun3() {
        runBlocking {
            println(111)
            withContext(Dispatchers.IO) {
                println(222)
            }
            println(333)
        }
    }
}