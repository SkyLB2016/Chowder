package com.sky.oa.activity

import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.sky.oa.R
import com.sky.oa.model.PeopleEntity
import com.sky.oa.utils.ExcelPOiUtils
import kotlinx.android.synthetic.main.activity_excel.*
import java.io.File


class ExcelPOIActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excel)
        bt01.setOnClickListener {
            creatExcel()
        }
        bt02.setOnClickListener {
            var filePath =
                Environment.getExternalStorageDirectory().path + "/老旧线路评估/测试.xlsx"//文件的全路径
            //读取Excel的内容
//            val resultFromXLS = ExcelJXLUtils.getExcelContent(filePath)
//            LogUtils.i(resultFromXLS.toString())
        }
        bt03.setOnClickListener {
        }
        bt04.setOnClickListener {
        }
    }

    private fun creatExcel() {
        //创建文件夹
        var filePath = Environment.getExternalStorageDirectory().path + "/老旧线路评估"
        val file = File(filePath)
        if (!file.exists()) {
            file.mkdirs()
        }

        val excelFileName = "/测试.xlsx"//Excel文件名
        filePath += excelFileName//文件的全路径
        val sheetName = "页签1"
        val columnTitles: Array<String> = arrayOf("姓名", "性别", "年龄", "地址")//Excel的列字段

        //初始化 Excel的文件、表单名称、列字段
        ExcelPOiUtils.initExcelAndXLSX(this,filePath, sheetName, columnTitles)

        val list: MutableList<PeopleEntity> = ArrayList()
        list.add(PeopleEntity("张三", "男", "22", "北京"))
        list.add(PeopleEntity("李四", "女", "33", "上海"))
        list.add(PeopleEntity("赵武", "女", "44", "广州"))
        list.add(PeopleEntity("王六", "男", "55", "深圳"))
        val datas: MutableList<MutableList<String>> = ArrayList()
        var data: MutableList<String>
        for (people in list) {
            data = ArrayList()
            data.add(people.name)
            data.add(people.gender)
            data.add(people.age)
            data.add(people.local)
            datas.add(data)
        }

        //写入到excel文件里内容
        ExcelPOiUtils.writeToExcelXLSX(datas, filePath, sheetName)
    }
}
