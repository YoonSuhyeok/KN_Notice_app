package com.example.noticekangwon.Jsoup

import com.example.noticekangwon.DataBase.AppDataBase
import com.example.noticekangwon.DataBase.Notice
import com.example.noticekangwon.Recyclerviews.NoticeAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class SoupClient(private val AppDataBase: AppDataBase) {
    private lateinit var mNoticeAdapter: NoticeAdapter
    constructor(AppDataBase: AppDataBase, noticeAdapter: NoticeAdapter) : this(AppDataBase){
         mNoticeAdapter = noticeAdapter
    }

    fun fetchData(fk:Int, baseUrl:String, cutNumberBaseUrl:Int, cutNumberFetchUrl:Int) {

        CoroutineScope(Dispatchers.IO).launch {
            // 2. SSL 체크
            if (baseUrl.indexOf("https://") >= 0) setSSL()
            // 3. HTML 가져오기
            val doc = Jsoup.connect(baseUrl).header("Content-Type", "application/json;charset=UTF-8").userAgent(USER_AGENT).method(Connection.Method.GET).ignoreContentType(true).get()
            delay(500)
            var contents: Elements
            if (doc != null) {
                contents = doc.select("tr")
                var titleNumber:Int = 0
                var dateNumber:Int = 0
                var content = contents[0].select("th")
                for( y in content.indices){
                    if(content[y].text() == "제목") titleNumber = y
                    else if( content[y].text() == "등록일" || content[y].text() == "작성일") dateNumber = y
                }
                for (x in 1 until contents.size){
                    content = contents[x].select("td")
                    var title: String = ""; var url:String= ""; var date:String= "";
                    for( y in content.indices ){
                        if( y == titleNumber ){
                            title = "${content[y].text()}"
                            url = "${baseUrl.substring(0..cutNumberBaseUrl)}${content[y].select("a").attr("href").substring(cutNumberFetchUrl)}"
                        }
                        if( y == dateNumber ){
                            date = content[y].text().replace('-', '.')
                        }
                    }
                    AppDataBase.noticeDao().insert(Notice(fk, title, url, date))
                }
        }
            // contents = doc.select("li.tbody")
            // contents = doc.select("li.has-new")
        }
    }

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

    companion object {
        private const val USER_AGENT =
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36"
    }
    val fetchInfoList = arrayListOf<FetchMajorInfo>(
        FetchMajorInfo(1, 1, false, "", 0, 0),
        FetchMajorInfo(2, 1, false, "", 0, 0),
        FetchMajorInfo(3, 1, false, "", 0, 0),
        FetchMajorInfo(4, 1, false, "", 0, 0),
        FetchMajorInfo(5, 1, false, "", 0, 0),
        FetchMajorInfo(6, 1, false, "", 0, 0),
        FetchMajorInfo(7, 1, false, "", 0, 0),
        FetchMajorInfo(8, 1, false, "", 0, 0),
        FetchMajorInfo(9, 1, false, "", 0, 0),
        FetchMajorInfo(10, 1, false, "", 0, 0),
        FetchMajorInfo(11, 1, false, "", 0, 0),
        FetchMajorInfo(12, 1, false, "", 0, 0),
        FetchMajorInfo(13, 1, false, "", 0, 0),
        FetchMajorInfo(14, 1, false, "", 0, 0),
        FetchMajorInfo(15, 1, false, "", 0, 0),
        FetchMajorInfo(16, 1, false, "", 0, 0),
        FetchMajorInfo(17, 1, false, "", 0, 0),
        FetchMajorInfo(18, 1, false, "", 0, 0),
        FetchMajorInfo(19, 1, false, "", 0, 0),
        FetchMajorInfo(20, 1, false, "", 0, 0),
        FetchMajorInfo(21, 1, false, "", 0, 0),
        FetchMajorInfo(22, 1, false, "", 0, 0),
        FetchMajorInfo(23, 1, false, "", 0, 0),
        FetchMajorInfo(24, 1, false, "", 0, 0),
        FetchMajorInfo(25, 1, false, "", 0, 0),
        FetchMajorInfo(26, 1, false, "", 0, 0),
        FetchMajorInfo(27, 1, false, "", 0, 0),
        FetchMajorInfo(28, 1, false, "", 0, 0),
        FetchMajorInfo(29, 1, false, "", 0, 0),
        FetchMajorInfo(30, 1, false, "", 0, 0),
        FetchMajorInfo(31, 1, false, "", 0, 0),
        FetchMajorInfo(32, 1, false, "", 0, 0),
        FetchMajorInfo(33, 1, false, "", 0, 0),
        FetchMajorInfo(34, 1, false, "", 0, 0),
        FetchMajorInfo(35, 1, false, "", 0, 0),
        FetchMajorInfo(36, 1, false, "", 0, 0),
        FetchMajorInfo(37, 1, false, "", 0, 0),
        FetchMajorInfo(38, 1, false, "", 0, 0),
        FetchMajorInfo(39, 1, false, "", 0, 0),
        FetchMajorInfo(40, 1, false, "", 0, 0),
        FetchMajorInfo(41, 1, false, "", 0, 0),
        FetchMajorInfo(42, 1, false, "", 0, 0),
        FetchMajorInfo(43, 1, false, "", 0, 0),
        FetchMajorInfo(44, 1, false, "", 0, 0),
        FetchMajorInfo(45, 1, false, "", 0, 0),
        FetchMajorInfo(46, 1, false, "", 0, 0),
        FetchMajorInfo(47, 1, false, "", 0, 0),
        FetchMajorInfo(48, 1, false, "", 0, 0),
        FetchMajorInfo(49, 1, false, "", 0, 0),
        FetchMajorInfo(50, 1, false, "", 0, 0),
        FetchMajorInfo(51, 1, false, "", 0, 0),
        FetchMajorInfo(52, 1, false, "", 0, 0),
        FetchMajorInfo(53, 1, false, "", 0, 0),
        FetchMajorInfo(54, 1, false, "", 0, 0),
        FetchMajorInfo(55, 1, false, "", 0, 0),
        FetchMajorInfo(56, 1, false, "", 0, 0),
        FetchMajorInfo(57, 1, false, "", 0, 0),
        FetchMajorInfo(58, 1, false, "", 0, 0),
        FetchMajorInfo(59, 1, false, "", 0, 0),
        FetchMajorInfo(60, 1, false, "", 0, 0),
        FetchMajorInfo(61, 1, false, "", 0, 0),
        FetchMajorInfo(62, 1, false, "", 0, 0),
        FetchMajorInfo(63, 1, false, "", 0, 0),
        FetchMajorInfo(64, 1, false, "", 0, 0),
        FetchMajorInfo(65, 1, false, "", 0, 0),
        FetchMajorInfo(66, 1, false, "", 0, 0),
        FetchMajorInfo(67, 1, false, "", 0, 0),
        FetchMajorInfo(68, 1, false, "", 0, 0),
        FetchMajorInfo(69, 1, false, "", 0, 0),
        FetchMajorInfo(70, 1, false, "", 0, 0),
        FetchMajorInfo(71, 1, false, "", 0, 0),
        FetchMajorInfo(72, 1, false, "", 0, 0),
        FetchMajorInfo(73, 1, false, "", 0, 0),
        FetchMajorInfo(74, 1, false, "", 0, 0),
        FetchMajorInfo(75, 1, false, "", 0, 0),
        FetchMajorInfo(76, 1, false, "", 0, 0),
        FetchMajorInfo(77, 1, false, "", 0, 0),
        FetchMajorInfo(78, 1, false, "", 0, 0),
        FetchMajorInfo(79, 1, false, "", 0, 0),
        FetchMajorInfo(80, 1, false, "", 0, 0),
        FetchMajorInfo(81, 1, false, "", 0, 0),
        FetchMajorInfo(82, 1, false, "", 0, 0),
        FetchMajorInfo(83, 1, false, "", 0, 0),
        FetchMajorInfo(84, 1, false, "", 0, 0),
        FetchMajorInfo(85, 1, false, "", 0, 0),
        FetchMajorInfo(86, 1, false, "", 0, 0),
        FetchMajorInfo(87, 15, false, "https://computer.kangwon.ac.kr/index.php?mp=5_1_1", 30, 0),
        FetchMajorInfo(88, 1, false, "", 0, 0),
        FetchMajorInfo(89, 1, false, "", 0, 0),
        FetchMajorInfo(90, 1, false, "", 0, 0),
        FetchMajorInfo(91, 17, false, "https://www.kangwon.ac.kr/www/selectBbsNttList.do?bbsNo=37&key=1176", 29, 2),
        FetchMajorInfo(92, 17, false, "https://www.kangwon.ac.kr/www/selectBbsNttList.do?bbsNo=34&key=232", 29, 2),
        FetchMajorInfo(93, 17, false, "https://sw.kangwon.ac.kr:44315/index.php?mp=5_1", 30, 0),
        FetchMajorInfo(94, 1, false, "", 0, 0),
        FetchMajorInfo(95, 1, false, "", 0, 0)
    )
}