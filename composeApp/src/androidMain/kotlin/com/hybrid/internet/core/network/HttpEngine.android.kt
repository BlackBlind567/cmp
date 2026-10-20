package com.hybrid.internet.core.network

import com.hybrid.internet.BuildConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

actual fun provideHttpEngine(): HttpClientEngine {
    return OkHttp.create {
        if (BuildConfig.DEBUG) {
            val trustManager = DebugTrustManager
            val sslContext = SSLContext.getInstance("TLS").apply {
                init(null, arrayOf(trustManager), SecureRandom())
            }

            config {
                sslSocketFactory(sslContext.socketFactory, trustManager)
                hostnameVerifier { _, _ -> true }
            }
        }
    }
}

private object DebugTrustManager : X509TrustManager {
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) = Unit

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) = Unit

    override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
}
