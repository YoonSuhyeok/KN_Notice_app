package com.example.noticekangwon.Retrofit

import android.widget.TextView
import com.example.noticekangwon.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class JsoupExample {
    fun fetchData(id: Int){
//        var textView = findViewById<TextView>(R.id.textView)
        var str:String = ""
        CoroutineScope(Dispatchers.Main).launch {
            val doc: Document? = withContext(Dispatchers.IO){
                Jsoup.connect("https://www.kangwon.ac.kr/www/selectBbsNttList.do?bbsNo=37&key=1176").get()
            }
            var contents: Elements
            if (doc != null) {
                contents = doc.select("table.bbs_default.list tbody tr")

                for( content in contents){
                    // 링크
                    val url = "http://www.kangwon.ac.kr/www/" + content.select("td")[2].select("a").attr(
                        "href"
                    ).substring(2)
                    // 제목
                    val title = content.select("td")[2].text()
                    // 첨부파일 유무 <td class="web_block"> </td> 의 size값에 따라 다르게 해줘야할 것 같음
//                val extension = content.select("td")[4]
                    val extension = false;
                    // 날짜
                    val date = content.select("td")[5].text()

                    str += "$title $date"
//                db.noticeDao().insert(title, url, date, extension)
                }
            }
//            textView.text = str
        }
    }
}