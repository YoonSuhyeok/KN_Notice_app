package com.example.notice.Jsoup

import com.example.notice.dataBase.AppDataBase
import com.example.notice.dataBase.Notice
import kotlinx.coroutines.*
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class SoupClient(private val AppDataBase: AppDataBase) {

    private val currentTime = Date()
    private val mYear: String = SimpleDateFormat("yyyy", Locale.KOREA).format ( currentTime )
    private val mDate: String = SimpleDateFormat("MM.dd", Locale.KOREA).format ( currentTime )

    fun fetchData(fk:Int, baseUrl:String, cutNumberBaseUrl:Int, cutNumberFetchUrl:Int) {

        CoroutineScope(Dispatchers.IO).launch {
            // 2. SSL 체크
            if (baseUrl.indexOf("https://") >= 0) setSSL()
            // 3. HTML 가져오기

            val doc = Jsoup.connect(baseUrl).header("Content-Type", "application/json;charset=UTF-8").userAgent(USER_AGENT).method(Connection.Method.GET).ignoreContentType(true).get()
            delay(100)

            var contents: Elements
            if (doc != null)
            {
                contents = doc.select("tr")
                if(fk == 74) // 교육혁신원
                {
                    contents = doc.select("li.tbody")
                    var title:String; var url:String; var date:String
                    for( x in contents){
                        title = x.select(".title").text()
                        url   = "${baseUrl.substring(0..cutNumberBaseUrl)}${x.select("a").attr("href")}"
                        date  = x.select(".reg_date").text().replace('-', '.')
                        AppDataBase.noticeDao().insert(Notice(fk, title, url, date))
                    }
                }
                else if(fk == 20) // 건축학과
                {
                    var content = contents[4].select("td")
                    var titleNumber = 0
                    var dateNumber = 0
                    for (y in content.indices) {
                        if (content[y].text().replace(" ", "") == "제목") {
                            titleNumber = y
                        } else if (content[y].text() == "등록일" || content[y].text() == "작성일" || content[y].text() == "날짜") {
                            dateNumber = y
                        }
                    }
                    for (x in 5 until contents.size) {
                        content = contents[x].select("td")
                        var title = ""; var url = ""; var date = ""
                        for (y in content.indices) {
                            if (y == titleNumber) {
                                title = content[y].text()
                                url =  "${baseUrl.substring(0..cutNumberBaseUrl)}${content[y].select("a").attr("href").substring(cutNumberFetchUrl)}"
                            }
                            if (y == dateNumber) {
                                date = content[y].text().replace('-', '.').replace('/', '.')
                                if( date.length == 5){
                                    val old:Int = date.substring(0..1).toInt()
                                    val current:Int = mDate.substring(0..1).toInt()
                                    date = if( current >= old )
                                        "$mYear.$date"
                                    else
                                        "${mYear.toInt()-1}.$date"
                                } else if( date.length == 8) {
                                    date = "${mYear.substring(0..1)}$date"
                                }
                            }
                        }
                        AppDataBase.noticeDao().insert(Notice(fk, title, url, date))
                    }
                }
                else if(fk == 27 || fk == 29)
                {
                    contents = doc.select("table.bbs_list").select("tr")
                    var titleNumber = 0
                    var dateNumber = 0
                    val content = contents.select("th")
                    for (y in content.indices) {
                        if (content[y].select("img").attr("alt") == "제목") {
                            titleNumber = y
                        } else if (content[y].select("img").attr("alt") == "작성일") {
                            dateNumber = y
                        }
                    }

                    for (x in 1 until contents.size) {
                        val content = contents[x].select("td")
                        var title = ""; var url = ""; var date = ""
                        for (y in content.indices) {
                            if (y == titleNumber) {
                                title = content[y].text()
                                url = "${baseUrl.substring(0..cutNumberBaseUrl)}${content[y].select("a").attr("href").substring(cutNumberFetchUrl)}"
                            }
                            if (y == dateNumber) {
                                date = content[y].text().replace('-', '.').replace('/', '.')
                                if( date.length == 5){
                                    val old:Int = date.substring(0..1).toInt()
                                    val current:Int = mDate.substring(0..1).toInt()
                                    date = if( current >= old )
                                        "$mYear.$date"
                                    else
                                        "${mYear.toInt()-1}.$date"
                                } else if( date.length == 8) {
                                    date = "${mYear.substring(0..1)}$date"
                                }
                            }
                        }
                        AppDataBase.noticeDao().insert(Notice(fk, title, url, date))
                    }
                }
                else if(fk == 32) // 미술학과
                {
                    var content = contents[0].select("td")
                    var titleNumber = 0
                    var dateNumber = 0
                    for( y in content.indices){
                        if(content[y].text() == "제목"){
                            titleNumber = y
                        }
                        if(content[y].text() == "작성일"){
                            dateNumber = y
                        }
                    }
                    for (x in 1 until contents.size) {
                        content = contents[x].select("td")
                        var title = ""; var url = ""; var date = ""
                        for (y in content.indices) {
                            if (y == titleNumber) {
                                title = content[y].text()
                                url = "${baseUrl.substring(0..cutNumberBaseUrl)}${content[y].select("a").attr("href").substring(cutNumberFetchUrl)}"
                            }
                            if (y == dateNumber) {
                                date = content[y].text().replace('-', '.').replace('/', '.')
                                if( date.length == 5){
                                    val old:Int = date.substring(0..1).toInt()
                                    val current:Int = mDate.substring(0..1).toInt()
                                    date = if( current >= old )
                                        "$mYear.$date"
                                    else
                                        "${mYear.toInt()-1}.$date"
                                } else if( date.length == 8) {
                                    date = "${mYear.substring(0..1)}$date"
                                }
                            }
                        }
                        AppDataBase.noticeDao().insert(Notice(fk, title, url, date))
                    }
                }
                else if(fk == 34) // 수학교육과
                {
                    contents = contents.select("[height=25]")
                    val titleNumber = 1
                    val dateNumber = 4
                    var title:String; var url:String; var date:String
                    for (x in 0 until contents.size) {
                        val content = contents[x].select("td")
                        title = content[titleNumber].text()
                        url = "${baseUrl.substring(0..cutNumberBaseUrl)}${content[titleNumber].select("a").attr("href").substring(cutNumberFetchUrl)}"
                        date = content[dateNumber].text().replace('-', '.').replace('/', '.')
                        when (date.length) {
                            5 -> date = if (date.substring(0..1) < mDate.substring(0..1) ) "${mYear.toInt()-1}.$date"
                            else "$mYear.$date"
                            8 -> date = "${mYear.substring(0..1)}$date"
                        }
                        AppDataBase.noticeDao().insert(Notice(fk, title, url, date))
                    }
                }
                else if(fk == 37) // 행정학과
                {
                    contents = doc.select("li.list-item")
                    val titleNumber = 1
                    val dateNumber = 4
                    var title:String; var url:String; var date:String
                    for( x in contents){
                        val content = x.select("div")
                        title = content[titleNumber].text()
                        url = content[titleNumber].select("a").attr("href")
                        date = content[dateNumber].text()
                        date = "$mYear.$date"
                        AppDataBase.noticeDao().insert(Notice(fk, title, url, date))
                    }
                }
                else if(fk == 54) //국어국문과
                {
                    val contents = doc.select("tr").select("[height=28]")
                    var title:String; var url:String; var date:String
                    for( x in contents ){
                        val content = x.select("td")
                        title = content[1].text()
                        url = "${baseUrl.subSequence(0..cutNumberBaseUrl)}${content[1].select("a").attr("href").substring(cutNumberFetchUrl)}"
                        date = content[4].text().replace('-', '.')
                        AppDataBase.noticeDao().insert(Notice(fk, title, url, date))
                    }
                }
                else if(contents.size > 0)
                {
                    var titleNumber = 0
                    var dateNumber = 0
                    var content = contents[0].select("th")
                    for( y in content.indices){
                        if(content[y].text() == "제목" || content[y].text() == "제 목" ) titleNumber = y
                        else if( content[y].text() == "등록일" || content[y].text() == "작성일" || content[y].text() == "날짜") dateNumber = y
                    }
                    for (x in 1 until contents.size){
                        content = contents[x].select("td")
                        var title = ""; var url = ""; var date = ""
                        for( y in content.indices ){
                            if( y == titleNumber ){
                                title = content[y].text()
                                url = baseUrl.substring(0..cutNumberBaseUrl)
                                url += content[y].select("a").attr("href").replace("amp;", "").substring(cutNumberFetchUrl)
                            }
                            if( y == dateNumber ){
                                date = content[y].text().replace('-', '.').replace('/', '.')
                                if( date.length == 5){
                                    val old:Int = date.substring(0..1).toInt()
                                    val current:Int = mDate.substring(0..1).toInt()
                                    date = if( current >= old )
                                        if( date.substring(3..4).toInt() >= mDate.substring(3..4).toInt())
                                            "$mYear.$date"
                                        else
                                            "${mYear.toInt()-1}.$date"
                                    else
                                        "${mYear.toInt()-1}.$date"
                                } else if( date.length == 8) {
                                    date = "${mYear.substring(0..1)}$date"
                                }
                            }
                        }
                        AppDataBase.noticeDao().insert(Notice(fk, title, url, date))
                    }
                }

            }
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

    val fetchInfoList = arrayListOf(
        FetchMajorInfo(1, 1, false, "https://nurse.kangwon.ac.kr/nurse/bbs_list.php?code=sub07a&keyvalue=sub07", 34, 1),
        FetchMajorInfo(2, 2, false, "https://biz.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1", 55, 32),
        FetchMajorInfo(3, 2, false, "http://account.kangwon.ac.kr/bbs/board.php?bo_table=notice", -1, 0),
        FetchMajorInfo(4, 2, false, "https://economics.kangwon.ac.kr/bbs/board.php?bo_table=sub07_1", -1, 0),
        FetchMajorInfo(5, 2, false, "https://statistics.kangwon.ac.kr/board/bbs/board.php?bo_table=notice_bbs", 38, 3),
        FetchMajorInfo(6, 2, false, "https://tourism.kangwon.ac.kr/bbs/board.php?bo_table=notice", -1, 0),
        // 국제무역학과는 날짜 조회를 다르게해야함으로 일단 넘어감.
        FetchMajorInfo(7, 2, false, "https://itb.kangwon.ac.kr/bbs/board.php?bo_table=notice", 0, 0),
        FetchMajorInfo(8, 3, false, "https://cll.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1", -1, 0),
        FetchMajorInfo(9, 3, false, "https://bse.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1", -1, 0),
        FetchMajorInfo(10, 3, false, "https://foodtech.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1", -1, 0),
        FetchMajorInfo(11, 3, false, "https://appliedplant.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1", -1, 0),
        FetchMajorInfo(12, 3, false, "https://applybio.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1", -1, 0),
        FetchMajorInfo(13, 3, false, "https://horti.kangwon.ac.kr/bbs/board.php?bo_table=sub5_1", -1, 0),
        FetchMajorInfo(14, 3, false, "https://agecon.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1", -1, 0),
        FetchMajorInfo(15, 3, false, "https://dbe.kangwon.ac.kr/dbe/bbs_list.php?code=sub07a&keyvalue=sub07", 29, 0),
        FetchMajorInfo(16, 3, false, "https://ecoenv.kangwon.ac.kr/ecoenv/bbs_list.php?code=sub07a&keyvalue=sub07", 35, 0),
        FetchMajorInfo(17, 4, false, "https://animal.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1", -1, 0),
        FetchMajorInfo(18, 4, false, "https://applanimalsci.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1", -1, 0),
        FetchMajorInfo(19, 4, false, "https://aniscience.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1", -1, 0),
        FetchMajorInfo(20, 5, false, "https://architecture.kangwon.ac.kr/nano/www/board/list.php?bid_=comm_notice", 49, 0),
        FetchMajorInfo(21, 5, false, "https://archi.kangwon.ac.kr/index.php?mp=4_1_1", 27, 0),
        FetchMajorInfo(22, 5, false, "https://civil.kangwon.ac.kr/2014/index.php?mp=6_1", 32, 0),
        FetchMajorInfo(23, 5, false, "https://environ.kangwon.ac.kr/environ/bbs_list.php?code=sub07a&keyvalue=sub07", 37, 0),
        FetchMajorInfo(24, 5, false, "https://mechanical.kangwon.ac.kr/index.php?mp=5_1", 32, 0),
        FetchMajorInfo(25, 5, false, "https://mecha.kangwon.ac.kr/mecha/bbs_list.php?code=sub07a&keyvalue=sub07", 33, 0),
        FetchMajorInfo(26, 5, false, "https://material.kangwon.ac.kr/index.php?mp=5_1_1", 30, 0),
        FetchMajorInfo(27, 5, false, "https://enre.kangwon.ac.kr/index.php?mt=page&mp=5_1&mm=oxbbs&oxid=1", 49, 0),
        FetchMajorInfo(28, 5, false, "https://sme.kangwon.ac.kr/index.php?mp=5_1", 25, 0),
        FetchMajorInfo(29, 5, false, "https://chemeng.kangwon.ac.kr/index.php?mt=page&mp=5_1&mm=oxbbs&oxid=1", 29, 0),
        FetchMajorInfo(30, 5, false, "https://bioeng.kangwon.ac.kr/index.php?mp=6_1_1", 28, 0),
        FetchMajorInfo(31, 5, false, "https://design.kangwon.ac.kr/design/bbs_list.php?code=sub07a&keyvalue=sub07", 35, 0),
        FetchMajorInfo(32, 5, false, "http://art.kangwon.ac.kr/wp/?page_id=1782", 24, 0),
        FetchMajorInfo(33, 5, false, "https://vculture.kangwon.ac.kr/vculture/bbs_list.php?code=sub07a&keyvalue=sub07", 39, 0),
        FetchMajorInfo(34, 6, false, "http://mathedu.kangwon.ac.kr/index.php?mt=page&mp=3_1&mm=oxbbs&oxid=2", 28, 0),
        FetchMajorInfo(35, 6, false, "https://history.kangwon.ac.kr/index.php?mp=5_1", 29, 0),
        FetchMajorInfo(36, 6, false, "https://engedu.kangwon.ac.kr/twb_bbs/user_bbs_list.php?bcd=01_06_04_00_00", 36, 0),
        FetchMajorInfo(37, 7, false, "https://padm.kangwon.ac.kr/bbs/board.php?bo_table=sub3_2", -1, 0),
        FetchMajorInfo(38, 7, false, "https://psych.kangwon.ac.kr/gnuboard4/bbs/board.php?bo_table=bbs41", 0, 0),
        FetchMajorInfo(39, 7, false, "https://masscom.kangwon.ac.kr/bbs/board.php?bo_table=sub3_1", 29, 2),
        FetchMajorInfo(40, 7, false, "https://politics.kangwon.ac.kr/bbs/board.php?bo_table=sub9_3", 30, 2),
        FetchMajorInfo(41, 8, false, "https://fm.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1", -1, 0),
        FetchMajorInfo(42, 8, false, "https://forestry.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1", -1, 0),
        FetchMajorInfo(43, 8, false, "http://fep.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1", 23, 2),
        FetchMajorInfo(44, 8, false, "https://wood.kangwon.ac.kr/bbs/board.php?bo_table=sub07_1", -1, 0),
        FetchMajorInfo(45, 8, false, "https://paper.kangwon.ac.kr/board/bbs/board.php?bo_table=sub03_1", 33, 2),
        FetchMajorInfo(46, 8, false, "https://lands.kangwon.ac.kr/bbs/board.php?bo_table=sub03_01", -1, 0),
        FetchMajorInfo(47, 9, false, "http://vetmed.kangwon.ac.kr/bbs/board.php?bo_table=sub07_1", 26, 2),
        FetchMajorInfo(48, 10, false, "https://pharmacy.kangwon.ac.kr/bbs/board.php?bo_table=sub02_05", -1, 0),
        FetchMajorInfo(49, 11, false, "https://si.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1", -1, 0),
        FetchMajorInfo(50, 11, false, "https://dmbt.kangwon.ac.kr/bbs/board.php?bo_table=sub07_1", -1, 0),
        FetchMajorInfo(51, 11, false, "https://molscien.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1", -1, 0),
        FetchMajorInfo(52, 11, false, "https://bio-health.kangwon.ac.kr/bbs/board.php?bo_table=sub04_1", -1, 0),
        FetchMajorInfo(53, 11, false, "https://bme.kangwon.ac.kr/bbs/board.php?bo_table=sub04_1", -1, 0),
        FetchMajorInfo(54, 12, false, "https://korean.kangwon.ac.kr/2013/bbs/board.php?bo_table=sub05_01", 32, 2),
        FetchMajorInfo(55, 12, false, "https://english.kangwon.ac.kr/new/board/bbs/board.php?bo_table=info", -1, 0),
        FetchMajorInfo(56, 12, false, "https://german.kangwon.ac.kr/bbs/board.php?bo_table=notice", -1, 0),
        FetchMajorInfo(57, 12, false, "https://chinese.kangwon.ac.kr/bbs/board.php?bo_table=sub5_1", -1, 0),
        FetchMajorInfo(58, 12, false, "http://www.kw-japan.com/bbs/board.php?bo_table=4_1", -1, 0),
        FetchMajorInfo(59, 12, false, "https://knuhisto.kangwon.ac.kr/bbs/board.php?bo_table=sub7_1", -1, 0),
        FetchMajorInfo(60, 13, false, "https://geophysics.kangwon.ac.kr/geophysics/bbs_list.php?code=sub07a&keyvalue=sub07", 43, 0),
        FetchMajorInfo(61, 13, false, "https://geology.kangwon.ac.kr/geology/bbs_list.php?code=sub07a&keyvalue=sub07", 37, 0),
        FetchMajorInfo(62, 13, false, "https://biochem.kangwon.ac.kr/biochem/bbs_list.php?code=sub07a&keyvalue=sub07", 37, 0),
        FetchMajorInfo(63, 13, false, "https://chemis.kangwon.ac.kr/board/bbs/board.php?bo_table=chemis_table04", 34, 2),
        FetchMajorInfo(64, 13, false, "https://biology.kangwon.ac.kr/biology/bbs_list.php?code=sub07a&keyvalue=sub07", 37, 0),
        FetchMajorInfo(65, 14, false, "https://eee.kangwon.ac.kr/eee/bbs_list.php?code=sub07a&keyvalue=sub07", 29, 0),
        FetchMajorInfo(66, 14, false, "https://ee.kangwon.ac.kr/index.php?mp=5_1", 24, 0),
        FetchMajorInfo(67, 14, false, "https://computer.kangwon.ac.kr/index.php?mp=5_1_1", 30, 0),
        FetchMajorInfo(68, 15, false, "https://multimajor.kangwon.ac.kr/bbs/board.php?bo_table=notice", -1, 0),
        FetchMajorInfo(69, 15, false, "https://liberal.kangwon.ac.kr/liberal/bbs_list.php?code=sub07a&keyvalue=sub07", 37, 0),
        FetchMajorInfo(70, 15, false, "https://www.kangwon.ac.kr/www/selectBbsNttList.do?bbsNo=37&key=1176", 29, 2),
        FetchMajorInfo(71, 16, false, "https://www.kangwon.ac.kr/www/selectBbsNttList.do?bbsNo=34&key=232", 29, 2),
        FetchMajorInfo(72, 16, false, "https://sw.kangwon.ac.kr:44315/index.php?mp=5_1", 30, 0),
        FetchMajorInfo(73, 16, false, "https://linc.kangwon.ac.kr/index.php?mp=4_1", 26, 0),
        FetchMajorInfo(74, 16, false, "https://itl.kangwon.ac.kr/ko/community/notice/list/1", 24, 0),
    )
}