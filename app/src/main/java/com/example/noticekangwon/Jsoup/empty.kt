package com.example.noticekangwon.Jsoup

import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.IOException
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object empty {
    private const val USER_AGENT =
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36"

    // SSL 우회 등록
    @Throws(NoSuchAlgorithmException::class, KeyManagementException::class)
    fun setSSL() {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                return null
            }

            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
        })
        val sc = SSLContext.getInstance("SSL")
        sc.init(null, trustAllCerts, SecureRandom())
        HttpsURLConnection.setDefaultHostnameVerifier { hostname, session -> true }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            // 1. URL 선언
            val connUrl = "https://www.daum.net"
            // 2. SSL 체크
            if (connUrl.indexOf("https://") >= 0) {
                setSSL()
            }
            // 3. HTML 가져오기
            val conn =
                Jsoup.connect(connUrl).header("Content-Type", "application/json;charset=UTF-8")
                    .userAgent(
                        USER_AGENT
                    ).method(Connection.Method.GET).ignoreContentType(true)
            val doc = conn.get()
            // 4. 가져온 HTML Document 를 확인하기
            println(doc.toString())
        } catch (e: IOException) {
            // Exp : Connection Fail
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }
}