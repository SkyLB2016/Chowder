package com.sky.chowder.utils

import android.os.Handler
import android.os.Message
import com.sky.chowder.model.ChapterEntity
import java.util.*

/**
 * Created by SKY on 2018/6/23 22:26 六月.
 */
class CatalogThread {

    var catalog: ((MutableList<ChapterEntity>) -> Unit)? = null

    private val handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            catalog?.invoke(msg?.obj as MutableList<ChapterEntity>)
        }
    }

    constructor(text: String,catalog:((MutableList<ChapterEntity>) -> Unit)) {
        this.catalog=catalog
        Thread(Runnable {
            val list = text.lines()
            //目录
            val catalog = ArrayList<ChapterEntity>()
            //章节
            var chapter = ChapterEntity()
            chapter.chapter = list[0]
            for (i in list) {
                val text = if (i.startsWith(" ")) i.substring(1) else i
                if (text.startsWith("第") && text.contains("章")
                        || text.startsWith("第") && text.contains("卦")
                        || text.startsWith("卷")
                        || text.startsWith("【")
                        || text.startsWith("●")) {
                    //保存上一章节的内容
                    chapter.content.setLength(chapter.content.length - 1)
                    catalog.add(chapter)
                    //创建新的章节
                    chapter = ChapterEntity()
                    chapter.chapter = text
                }
                //保存内容
                chapter.content.append("$text\n")
            }
            //如章节还是没有拆分且文章大于30行，则按10行一组拆分
            var sign = -1
            if (catalog.isEmpty() && list.size > 30) {
                chapter.content = StringBuilder()
                for (i in list.indices) {
                    val text = if (list[i].startsWith(" ")) list[i].substring(1) else list[i]
                    //以，作为正文起始处
                    if (sign === -1 && text.contains("，")) sign = i % 10
                    //记录章节
                    if (i % 10 === sign) {
                        //保存上一章节的内容
                        chapter.content.setLength(chapter.content.length - 1)
                        catalog.add(chapter)
                        //创建新的章节
                        chapter = ChapterEntity()
                        chapter.chapter = text
                    }
                    //保存内容
                    chapter.content.append("$text\n")
                }
            }
            //保存最后一章的内容
            chapter.content.setLength(chapter.content.length - 1)
            catalog.add(chapter)

            //更新adapter
            val msg = handler.obtainMessage()
            msg.obj = catalog
            handler.sendMessage(msg)
        }).start()

    }
}
