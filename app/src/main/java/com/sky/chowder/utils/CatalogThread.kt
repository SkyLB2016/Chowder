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
            val catalog = ArrayList<ChapterEntity>()
            var chapter = ChapterEntity()
            chapter.chapter = list[0]
            for (i in list) {
                val text = if (i.startsWith(" ")) i.substring(1) else i
                if (text.startsWith("第") && text.contains("章")
                        || text.startsWith("第") && text.contains("卦")
                        || text.startsWith("卷")
                        || text.startsWith("【")
                        || text.startsWith("●")
                        || text.startsWith("·")) {
                    chapter.content.setLength(chapter.content.length - 1)
                    catalog.add(chapter)
                    chapter = ChapterEntity()
                    chapter.chapter = text
                }
                chapter.content.append("$text\n")
            }
            var sign = -1
            if (catalog.isEmpty() && list.size > 30) {
                chapter.content = StringBuilder()
                for (i in list.indices) {
                    val text = if (list[i].startsWith(" ")) list[i].substring(1) else list[i]
                    if (sign === -1 && text.contains("，")) sign = i % 10
                    if (i % 10 === sign) {
                        chapter.content.setLength(chapter.content.length - 1)
                        catalog.add(chapter)
                        chapter = ChapterEntity()
                        chapter.chapter = text
                    }
                    chapter.content.append("$text\n")
                }
            }
            chapter.content.setLength(chapter.content.length - 1)
            catalog.add(chapter)
            val msg = handler.obtainMessage()
            msg.obj = catalog
            handler.sendMessage(msg)
        }).start()

    }
}
