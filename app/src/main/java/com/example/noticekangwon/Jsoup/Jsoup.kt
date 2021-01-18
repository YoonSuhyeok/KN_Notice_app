package com.example.noticekangwon.Jsoup

import android.content.Context
import androidx.room.Room
import com.example.noticekangwon.DataBase.AppDataBase
import com.example.noticekangwon.DataBase.Notice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class Jsoup(val applicationContext: Context) {

    fun fetchData(id: Int):ArrayList<Notice> {
        var db: AppDataBase = if(id == 1) Room.databaseBuilder(applicationContext, AppDataBase::class.java, "Major-DB").allowMainThreadQueries().build()
        else Room.databaseBuilder(applicationContext, AppDataBase::class.java, "Major-DB").build()

        val oldlists = db.noticeDao().all
        val newlists = ArrayList<Notice>()
        CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
            val fk = 1
            val doc: Document? =
                Jsoup.connect("https://www.kangwon.ac.kr/www/selectBbsNttList.do?bbsNo=37&key=1176")
                    .get()
            var contents: Elements
            if (doc != null) {
                contents = doc.select("table.bbs_default.list tbody tr")

                for (content in contents) {
                    // 링크
                    val url = "http://www.kangwon.ac.kr/www/" + content.select("td")[2].select("a")
                        .attr("href").substring(2)
                    // 제목
                    val title = content.select("td")[2].text()
                    // 첨부파일 유무 <td class="web_block"> </td> 의 size값에 따라 다르게 해줘야할 것 같음
                    // val extension = content.select("td")[4]
                    val extension = false;
                    // 날짜
                    val date = content.select("td")[5].text()

                    newlists.add(Notice(fk, title, url, date, extension))
                    db.noticeDao().insert(Notice(fk, title, url, date, extension))
                }
            }
        }
        db.close()

        val returnLists = ArrayList<Notice>()
        returnLists.add(Notice(0, "테스트1", "url", "date", false))
        for( x in newlists){
            if(oldlists.contains(x) == false){
                returnLists.add(x)
            }
        }
        return returnLists
    }
}