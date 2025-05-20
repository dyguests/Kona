package com.fanhl.http

import androidx.annotation.CheckResult
import kotlinx.coroutines.flow.Flow
import rxhttp.toAwait
import rxhttp.toFlow
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.parse.Parser
import rxhttp.wrapper.parse.SmartParser
import rxhttp.wrapper.utils.javaTypeOf

@CheckResult(suggest = "Use await() on the returned TcHttpParam")
fun fetch(url: String): TcHttpParam {
    return TcHttpParam(url)
}

/**
 * 注意： json 的装箱和拆箱是在拦截器中做的，所以这里不需要再做转换
 */
class TcHttpParam(
    url: String,
) {
    private val rxHttpJsonParam = RxHttp.postJson(url)

    private val bodyMap = mutableMapOf<String, Any?>()

    fun header(vararg pairs: Pair<String, Any>): TcHttpParam {
        for (pair in pairs) {
            rxHttpJsonParam.addHeader(pair.first, pair.second.toString())
        }
        return this
    }

    fun header(headers: Map<String, Any>): TcHttpParam {
        for ((key, value) in headers) {
            rxHttpJsonParam.addHeader(key, value.toString())
        }
        return this
    }

    fun body(vararg pairs: Pair<String, Any?>): TcHttpParam {
        for (pair in pairs) {
            rxHttpJsonParam.add(pair.first, pair.second)
            // bodyMap[pair.first] = pair.second
        }
        return this
    }

    fun <T> body(data: T): TcHttpParam {
        rxHttpJsonParam.addAll(GsonUtils.toMap(data))
        // bodyMap.putAll(GsonUtils.toMap(data))
        return this
    }

    @JvmName("awaitUnit")
    suspend fun await() = rxHttpJsonParam.toAwait<Unit>().await()

    suspend inline fun <reified T> await(): T = await(SmartParser.wrap(javaTypeOf<T>()))

    inline fun <reified T> toFlow(): Flow<T> = toFlow(SmartParser.wrap(javaTypeOf<T>()))

    /**
     * todo 注意这里和 RxHttp 耦合，之后改
     */
    suspend fun <T> await(parser: Parser<T>): T = rxHttpJsonParam.toAwait(parser).await()

    /**
     * todo 注意这里和 RxHttp 耦合，之后改
     */
    fun <T> toFlow(parser: Parser<T>): Flow<T> = rxHttpJsonParam.toFlow(parser)
}
