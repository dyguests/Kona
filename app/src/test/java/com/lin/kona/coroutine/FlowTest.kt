package com.lin.kona.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Test
import kotlin.system.measureTimeMillis

class FlowTest {
    @Test
    fun fun1() = runBlocking {
        simple().forEach { println(it) }
    }

    private suspend fun simple() = sequence {
        for (i in 1..3) {
            Thread.sleep(100)
            yield(i)
        }
    }

    @Test
    fun fun2() = runBlocking {
        simple2().forEach { value -> println(value) }
    }

    private suspend fun simple2(): List<Int> {
        delay(100) // 假装我们在这里做了一些异步的事情
        return listOf(1, 2, 3)
    }

    @Test
    fun fun3() = runBlocking {
        simple3().collect { value -> println(value) }
    }

    private fun simple3() = flow {
        for (i in 1..3) {
            delay(100)
            emit(i)
        }
    }

    @Test
    fun fun4() = runBlocking<Unit> {
        println("Calling simple function...")
        val flow = simple4()
        println("Calling collect...")
        flow.collect { value -> println(value) }
        println("Calling collect again...")
        flow.collect { value -> println(value) }
    }

    fun simple4(): Flow<Int> = flow {
        println("Flow started")
        for (i in 1..3) {
            delay(100)
            emit(i)
        }
    }

    fun simple5(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(100)
            println("Emitting $i")
            emit(i)
        }
    }

    @Test
    fun fun5() = runBlocking<Unit> {
        withTimeoutOrNull(250) { // 在 250 毫秒后超时
            simple5().collect { value -> println(value) }
        }
        println("Done")
    }

    @Test
    fun fun6() = runBlocking {
        (1..3).asFlow().collect { value -> println(value) }
    }


    suspend fun performRequest(request: Int): String {
        delay(1000) // 模仿长时间运行的异步工作
        return "response $request"
    }

    @Test
    fun fun7() = runBlocking<Unit> {
        (1..3).asFlow() // 一个请求流
            .map { request -> performRequest(request) }
            .collect { response -> println(response) }
    }

    @Test
    fun fun8() = runBlocking {
        (1..3).asFlow() // 一个请求流
            .transform { request ->
                emit("Making request $request")
                emit(performRequest(request))
            }
            .collect { response -> println(response) }
    }

    fun numbers(): Flow<Int> = flow {
        try {
            emit(1)
            emit(2)
            println("This line will not execute")
            emit(3)
        } finally {
            println("Finally in numbers")
        }
    }

    @Test
    fun main9() = runBlocking<Unit> {
        numbers()
            .take(2) // 只获取前两个
            .collect { value -> println(value) }
    }

    @Test
    fun test10() = runBlocking {
        val sum = (1..5).asFlow()
            .map { it * it } // 数字 1 至 5 的平方
            .reduce { a, b -> a + b } // 求和（末端操作符）
        println(sum)

    }

    fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

    fun simple11(): Flow<Int> = flow {
        log("Started simple flow")
        for (i in 1..3) {
            emit(i)
        }
    }

    @Test
    fun main11() = runBlocking<Unit> {
        simple11().collect { value -> log("Collected $value") }
    }

    fun simple12(): Flow<Int> = flow {
        for (i in 1..3) {
            Thread.sleep(100) // 假装我们以消耗 CPU 的方式进行计算
            log("Emitting $i")
            emit(i) // 发射下一个值
        }
    }.flowOn(Dispatchers.Default) // 在流构建器中改变消耗 CPU 代码上下文的正确方式

    @Test
    fun main12() = runBlocking<Unit> {
        simple12().collect { value ->
            log("Collected $value")
        }
    }

    fun simple13(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(100) // 假装我们异步等待了 100 毫秒
            emit(i) // 发射下一个值
        }
    }

    @Test
    fun main13() = runBlocking<Unit> {
        val time = measureTimeMillis {
            simple13().collect { value ->
                delay(300) // 假装我们花费 300 毫秒来处理它
                println(value)
            }
        }
        println("Collected in $time ms")
    }

    @Test
    fun main13_2() = runBlocking<Unit> {
        val time = measureTimeMillis {
            simple13()
                .buffer()
                .collect { value ->
                    delay(300) // 假装我们花费 300 毫秒来处理它
                    println(value)
                }
        }
        println("Collected in $time ms")
    }

    @Test
    fun main13_3() = runBlocking<Unit> {
        val time = measureTimeMillis {
            simple13()
                .conflate()
                .collect { value ->
                    delay(300) // 假装我们花费 300 毫秒来处理它
                    println(value)
                }
        }
        println("Collected in $time ms")
    }

    @Test
    fun main13_4() = runBlocking<Unit> {
        val time = measureTimeMillis {
            simple13()
                .collectLatest { value ->
                    delay(300) // 假装我们花费 300 毫秒来处理它
                    println(value)
                }
        }
        println("Collected in $time ms")
    }

    @Test
    fun main14() = runBlocking<Unit> {
        val nums = (1..3).asFlow() // 数字 1..3
        val strs = flowOf("one", "two", "three") // 字符串
        nums.zip(strs) { a, b -> "$a -> $b" } // 组合单个字符串
            .collect { println(it) } // 收集并打印

    }

    @Test
    fun main14_2() = runBlocking<Unit> {
        val nums = (1..3).asFlow() // 数字 1..3
        val strs = flowOf("one", "two", "three") // 字符串
        nums.combine(strs) { a, b -> "$a -> $b" } // 组合单个字符串
            .collect { println(it) } // 收集并打印

    }

    @Test
    fun main14_3() = runBlocking<Unit> {
        val nums = (1..3).asFlow().onEach { delay(300) } // 发射数字 1..3，间隔 300 毫秒
        val strs = flowOf("one", "two", "three").onEach { delay(400) } // 每 400 毫秒发射一次字符串
        val startTime = System.currentTimeMillis() // 记录开始的时间
        nums.combine(strs) { a, b -> "$a -> $b" } // 使用“combine”组合单个字符串
            .collect { value -> // 收集并打印
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }
    }

    fun requestFlow(i: Int): Flow<String> = flow {
        emit("$i: First")
        delay(500) // 等待 500 毫秒
        emit("$i: Second")
    }

    @OptIn(FlowPreview::class)
    @Test
    fun main15() = runBlocking {
        val startTime = System.currentTimeMillis() // 记录开始时间
        (1..3).asFlow().onEach { delay(100) } // 每 100 毫秒发射一个数字
            .flatMapConcat { requestFlow(it) }
            .collect { value -> // 收集并打印
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }
    }

    @OptIn(FlowPreview::class)
    @Test
    fun main16() = runBlocking {
        val startTime = System.currentTimeMillis() // 记录开始时间
        (1..3).asFlow().onEach { delay(100) } // 每 100 毫秒发射一个数字
            .flatMapMerge { requestFlow(it) }
            .collect { value -> // 收集并打印
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun main17() = runBlocking {
        val startTime = System.currentTimeMillis() // 记录开始时间
        (1..3).asFlow().onEach { delay(100) } // 每 100 毫秒发射一个数字
            .flatMapLatest { requestFlow(it) }
            .collect { value -> // 收集并打印
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }
    }

    fun simple18(): Flow<Int> = flow {
        for (i in 1..3) {
            println("Emitting $i")
            emit(i) // 发射下一个值
        }
    }

    @Test
    fun main18() = runBlocking<Unit> {
        try {
            simple18().collect { value ->
                println(value)
                check(value <= 1) { "Collected $value" }
            }
        } catch (e: Throwable) {
            println("Caught $e")
        }
    }

    fun simple19(): Flow<String> =
        flow {
            for (i in 1..3) {
                println("Emitting $i")
                emit(i) // 发射下一个值
            }
        }
            .map { value ->
                check(value <= 1) { "Crashed on $value" }
                "string $value"
            }

    @Test
    fun main19() = runBlocking<Unit> {
        try {
            simple19().collect { value -> println(value) }
        } catch (e: Throwable) {
            println("Caught $e")
        }
    }

    @Test
    fun main19_2() = runBlocking<Unit> {
        simple19()
            .catch { e -> emit("Caught $e") } // 发射一个异常
            .collect { value -> println(value) }
    }

    fun simple20(): Flow<Int> = flow {
        for (i in 1..3) {
            println("Emitting $i")
            emit(i)
        }
    }

    @Test
    fun main20() = runBlocking<Unit> {
        simple20()
            .catch { e -> println("Caught $e") } // 不会捕获下游异常
            .collect { value ->
                check(value <= 1) { "Collected $value" }
                println(value)

            }
    }

    @Test
    fun main21() = runBlocking<Unit> {
        simple20()
            .onEach { value ->
                check(value <= 1) { "Collected $value" }
                println(value)

            }
            .catch { e -> println("Caught $e") } // 不会捕获下游异常
            .collect()
    }

    fun simple22(): Flow<Int> = (1..3).asFlow()

    @Test
    fun main22() = runBlocking<Unit> {
        try {
            simple22().collect { value -> println(value) }
        } finally {
            println("Done")
        }
    }

    @Test
    fun main22_2() = runBlocking<Unit> {
        try {
            simple22()
                .onCompletion { println("Done") }
                .collect { value -> println(value) }
        } finally {
            println("Done")
        }
    }

    @Test
    fun main23() = runBlocking<Unit> {
        (1..5).asFlow().cancellable().collect { value ->
            if (value == 3) cancel()
            println(value)
        }
    }
}