package com.fanhl.http

import androidx.annotation.CheckResult
import com.fanhl.util.GsonUtils
import kotlinx.coroutines.flow.Flow
import rxhttp.toAwait
import rxhttp.toFlow
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.parse.Parser
import rxhttp.wrapper.parse.SmartParser
import rxhttp.wrapper.utils.javaTypeOf

@CheckResult(suggest = "Use await() on the returned GetParam")
fun get(url: String): GetParam {
    return GetParam(url)
}

@CheckResult(suggest = "Use await() on the returned PostParam")
fun post(url: String): PostParam {
    return PostParam(url)
}

class GetParam(private val url: String) {
    private val rxHttpParam = RxHttp.get(url)

    fun domain(domain: String): GetParam {
        rxHttpParam.setDomainIfAbsent(domain)
        return this
    }

    fun header(vararg pairs: Pair<String, Any>): GetParam {
        for (pair in pairs) {
            rxHttpParam.addHeader(pair.first, pair.second.toString())
        }
        return this
    }

    fun header(headers: Map<String, Any>): GetParam {
        for ((key, value) in headers) {
            rxHttpParam.addHeader(key, value.toString())
        }
        return this
    }

    fun query(vararg pairs: Pair<String, Any?>): GetParam {
        for (pair in pairs) {
            rxHttpParam.addQuery(pair.first, pair.second)
        }
        return this
    }

    fun <T> query(data: T): GetParam {
        rxHttpParam.addAll(GsonUtils.toMap(data))
        return this
    }

    @JvmName("awaitUnit")
    suspend fun await() = rxHttpParam.toAwait<Unit>().await()

    suspend inline fun <reified T> await(): T = await(SmartParser.wrap(javaTypeOf<T>()))

    inline fun <reified T> toFlow(): Flow<T> = toFlow(SmartParser.wrap(javaTypeOf<T>()))

    suspend fun <T> await(parser: Parser<T>): T = rxHttpParam.toAwait(parser).await()

    fun <T> toFlow(parser: Parser<T>): Flow<T> = rxHttpParam.toFlow(parser)
}

class PostParam(private val url: String) {
    private val rxHttpParam = RxHttp.postJson(url)

    fun domain(domain: String): PostParam {
        rxHttpParam.setDomainIfAbsent(domain)
        return this
    }

    fun header(vararg pairs: Pair<String, Any>): PostParam {
        for (pair in pairs) {
            rxHttpParam.addHeader(pair.first, pair.second.toString())
        }
        return this
    }

    fun header(headers: Map<String, Any>): PostParam {
        for ((key, value) in headers) {
            rxHttpParam.addHeader(key, value.toString())
        }
        return this
    }

    fun body(vararg pairs: Pair<String, Any?>): PostParam {
        for (pair in pairs) {
            rxHttpParam.add(pair.first, pair.second)
        }
        return this
    }

    fun <T> body(data: T): PostParam {
        rxHttpParam.addAll(GsonUtils.toMap(data))
        return this
    }

    @JvmName("awaitUnit")
    suspend fun await() = rxHttpParam.toAwait<Unit>().await()

    suspend inline fun <reified T> await(): T = await(SmartParser.wrap(javaTypeOf<T>()))

    inline fun <reified T> toFlow(): Flow<T> = toFlow(SmartParser.wrap(javaTypeOf<T>()))

    suspend fun <T> await(parser: Parser<T>): T = rxHttpParam.toAwait(parser).await()

    fun <T> toFlow(parser: Parser<T>): Flow<T> = rxHttpParam.toFlow(parser)
}
