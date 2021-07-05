package com.sky.oa.utils

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.sky.oa.model.ChapterEntity
import java.util.*

/**
 * Created by SKY on 2018/6/23 22:26 六月.
 */
class CatalogThread {

    var cataloglistener: ((MutableList<ChapterEntity>) -> Unit)? = null

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            cataloglistener?.invoke(msg?.obj as MutableList<ChapterEntity>)
        }
    }

    constructor(source: String, catalogListener: ((MutableList<ChapterEntity>) -> Unit)) {
        this.cataloglistener = catalogListener
        Thread(Runnable {
            //行列表
            val list = source.lines()
            //目录
            val catalog = ArrayList<ChapterEntity>()
            //单一章节
            var chapter = ChapterEntity()
            chapter.chapter = list[0]//最开始的是文件名
            for (text in list) {
                //放在string.xml里的文档，开头会有个空格，需要先去除
//                val text = if (i.startsWith(" ")) i.substring(1) else i
                //是否是一章的开始
                if (text.startsWith("第") && text.contains("章")
                    || text.startsWith("第") && text.contains("节")
                    || text.startsWith("第") && text.contains("卦")
                    || text.contains("篇") && text.contains("第")
                    || text.startsWith("卷")
                    || text.startsWith("【")
                    || text.startsWith("●")
                ) {
                    //进入此处代表又是一个新的章节开始，所以需要先保存上一章节的内容，同时也需要移除最后一个换行符
//                    if (chapter.content != null && chapter.content.isNotEmpty())
                    chapter.content.setLength(chapter.content.length - 1)
                    catalog.add(chapter)

                    //创建新的章节
                    chapter = ChapterEntity()
                    chapter.chapter = text//设置章节
                }
                //不是一章的开始就把内容保存在content中，因为是一行一行存的所以需要在末尾加入换行符号\n
                chapter.content.append("$text\n")//内容包含，章节名称与实际内容。
            }
            //如章节还是没有拆分且文章大于30行，则按10行一组拆分
//            var sign = -1
//            if (catalog.isEmpty() && list.size > 30) {
//                chapter.content = StringBuilder()
//                for ((i,text) in list.withIndex()) {
////                    val text = if (list[i].startsWith(" ")) list[i].substring(1) else list[i]
//                    //以，作为正文起始处
//                    if (sign === -1 && text.contains("，")) sign = i % 10
//                    //记录章节
//                    if (i % 10 === sign) {
//                        //保存上一章节的内容
//                        chapter.content.setLength(chapter.content.length - 1)
//                        catalog.add(chapter)
//                        //创建新的章节
//                        chapter = ChapterEntity()
//                        chapter.chapter = text
//                    }
//                    //保存内容
//                    chapter.content.append("$text\n")
//                }
//            }
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